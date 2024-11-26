package com.lyflexi.alertnacos.service.impl;

import com.lyflexi.alertxxljob.service.IMessageAlertService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Author: lyflexi
 * @project: monitor-to-timedTask
 * @Date: 2024/11/26 10:21
 */
@Component
@Slf4j
public class IAlertService implements IMessageAlertService {
    @Override
    public void sendAlert(String body) {
        log.info("发送alert:{}",body);
    }
}
