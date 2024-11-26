package com.lyflexi.alertnacos.monitor;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description:
 * @Author: lyflexi
 * @project: monitor-to-nacos
 * @Date: 2024/11/26 11:03
 */
@Data
public class SysSettingVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;

    /**
     * 配置中文名称
     */
    private String configName;

    /**
     * 配置键
     */
    private String configKey;
    /**
     * 配置值：JSON
     */
    private String configValue;

    private String configType;
}
