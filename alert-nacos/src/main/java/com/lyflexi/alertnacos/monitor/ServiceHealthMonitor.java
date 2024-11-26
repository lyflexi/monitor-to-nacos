package com.lyflexi.alertnacos.monitor;

import com.alibaba.fastjson2.TypeReference;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lyflexi.alertnacos.monitor.AlertEnums;
import com.lyflexi.alertnacos.service.impl.IAlertService;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author: lyflexi
 * @project: monitor-to-nacos
 * @Date: 2024/11/26 10:41
 */

@Component
@Slf4j
public class ServiceHealthMonitor {
    @Value("${spring.cloud.nacos.username}")
    private String username;

    @Value("${spring.cloud.nacos.password}")
    private String password;

    @Value("${spring.cloud.nacos.discovery.server-addr}")
    private String serverAddress;

    @Value("${spring.cloud.nacos.discovery.namespace}")
    private String serviceNamespace;

    private static NamingService namingService;


    @Autowired
    private IAlertService alertService;

    /**
     *
     *
     * services_info
     [
     {
     "serviceName": "les-dis-service",
     "instances": 1
     },
     {
     "serviceName": "les-plan-service",
     "instances": 1
     },
     {
     "serviceName": "les-demand-service",
     "instances": 1
     },
     {
     "serviceName": "les-system-service",
     "instances": 1
     },
     {
     "serviceName": "les-picking-service",
     "instances": 1
     },
     {
     "serviceName": "les-pull-service",
     "instances": 1
     },
     {
     "serviceName": "les-transport-service",
     "instances": 1
     },
     {
     "serviceName": "les-inventory-service",
     "instances": 1
     },
     {
     "serviceName": "les-bridge-service",
     "instances": 1
     },
     {
     "serviceName": "msp-system",
     "instances": 1
     },
     {
     "serviceName": "msp-gateway",
     "instances": 1
     },
     {
     "serviceName": "xxl-job-admin",
     "instances": 1
     },
     {
     "serviceName": "les-message-service",
     "instances": 1
     }
     ]
     */
    private static final String SERVICES_INFO = "services_info";

    @Autowired
    private CacheSettingFacade settingFacade;

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    // @PostConstruct
    public ServiceHealthMonitor() {
        scheduler.scheduleWithFixedDelay(this::fetchServiceList, 240, 30, TimeUnit.SECONDS);
    }

    private void fetchServiceList() {
        log.info("[服务健康监控开始执行]schedule time: {}", LocalDateTime.now());
        //初始化nacos链接
        initNamingService();
        MspSettingVo settingVo = settingFacade.getSetting(LesUserContextHolder.getInstance().getTenantId(), SERVICES_INFO);
        Map<String, Integer> serviceMap = getServiceMap(settingVo);
        if (serviceMap == null) {
            return;
        }
        List<String> servicesNameList = new ArrayList<>(serviceMap.keySet());

        for (String serviceName : servicesNameList) {
            try {
                List<Instance> instances = namingService.getAllInstances(serviceName);
                int healthInstances = (int) instances.stream().filter(Instance::isHealthy).count();
                if (CollectionUtils.isEmpty(instances) || (healthInstances < serviceMap.get(serviceName)) || healthInstances == 0) {
                    log.error("[服务健康监控开始执行]....服务挂了");
                    String desc = String.format("系统监测到服务[%s]有实例节点宕机，节点数量：健康%d|期望%d。", serviceName, instances.size(), serviceMap.get(serviceName));
                    alertService.sendAlert(desc);
                }
            } catch (NacosException e) {
                throw new RuntimeException(e);
            }
        }
    }
    /*
     * @param settingVo
     * @return
     */
    private Map<String, Integer> getServiceMap(MspSettingVo settingVo) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Map<String, Object>> serviceList = null;
        try {
            serviceList = objectMapper.readValue(settingVo.getConfigValue(), new TypeReference<List<Map<String, Object>>>() {});
        } catch (IOException e) {
            log.error("微服务参数配置错误");
            return null;
        }
        Map<String, Integer> serviceMap = serviceList.stream()
                .collect(Collectors.toMap(
                        map -> (String) map.get("serviceName"), // key映射
                        map -> (Integer) map.get("instances")    // value映射
                ));
        log.info("serviceMap: {}", serviceMap);
        return serviceMap;
    }

    private void initNamingService () {
        if (Objects.nonNull(namingService)) {
            return;
        }
        Properties properties = new Properties();
        properties.put("serverAddr", serverAddress);
        properties.put("namespace", serviceNamespace);
        properties.put("username", username);
        properties.put("password", password);

        // 获取NamingService实例
        try {
            namingService = NacosFactory.createNamingService(properties);
        } catch (Exception e) {
            log.error(e.toString());
        }
    }

    @PreDestroy
    public void destroy() {
        scheduler.shutdown();
    }
}
