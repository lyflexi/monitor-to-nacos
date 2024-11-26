package com.lyflexi.alertnacos;

import com.lyflexi.alertnacos.mock.MockMonitorTask;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AlertNacosApplicationTests {
@Autowired
    MockMonitorTask mockMonitorTask;
    @Test
    void contextLoads() throws Exception {
        mockMonitorTask.mockMonitorTask();
        Thread.sleep(10000L);
    }

}
