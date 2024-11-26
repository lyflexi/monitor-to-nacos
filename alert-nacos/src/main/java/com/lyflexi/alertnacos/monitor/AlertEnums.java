package com.lyflexi.alertnacos.monitor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * @Description:
 * @Author: lyflexi
 * @project: monitor-to-timedTask
 * @Date: 2024/11/26 10:25
 */

@Getter
public enum AlertEnums {

    ;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public enum ModuleType {
        JOB("JOB", "定时任务"),
        MQ_PUB("MQ_PUB", "消息发布"),
        MQ_CSM("MQ_CSM", "消息消费"),
        MONITOR("MONITOR", "健康监控"),
        DURATION_MONITOR("DURATION_MONITOR", "系统任务执行时间超长"),
        ;

        private String code;
        private String msg;

        public static String getMsg (String code) {
            for (ModuleType type : ModuleType.values()) {
                if (StringUtils.equals(type.code, code)) {
                    return type.msg;
                }
            }
            return "UNKNOWN";
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public enum Level {
        RISK("RISK", "高危"),
        HIGH("HIGH", "高"),
        MID("MID", "中"),
        LOW("LOW", "低");

        private String code;
        private String msg;

        public static String getMsg (String code) {
            for (Level level : Level.values()) {
                if (StringUtils.equals(level.code, code)) {
                    return level.msg;
                }
            }
            return "UNKNOWN";
        }
    }

}