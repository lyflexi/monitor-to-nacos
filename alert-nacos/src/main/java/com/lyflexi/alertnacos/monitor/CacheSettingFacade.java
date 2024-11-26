package com.lyflexi.alertnacos.monitor;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: lyflexi
 * @project: monitor-to-nacos
 * @Date: 2024/11/26 11:01
 */

/**
 * 系统参数获取组件
 */
@Component
public class CacheSettingFacade {
    public SysSettingVo getSetting(String servicesInfo) {
        SysSettingVo sysSettingVo = new SysSettingVo();
        List<Map<String,Object>> value = new ArrayList<>();
        HashMap<String, Object> valueItem1 = new HashMap<>();
        valueItem1.put("serviceName","nacos-service1");
        valueItem1.put("instances","1");
        value.add(valueItem1);
        HashMap<String, Object> valueItem2 = new HashMap<>();
        valueItem2.put("serviceName","nacos-service2");
        valueItem2.put("instances","1");
        value.add(valueItem2);
        sysSettingVo.setConfigKey("services_info");
        sysSettingVo.setConfigValue(value.toString());
        return sysSettingVo;
    }
}
