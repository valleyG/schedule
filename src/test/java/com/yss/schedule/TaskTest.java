package com.yss.schedule;

import com.yss.schedule.mapper.WorkDayDao;
import com.yss.schedule.service.MailService;
import com.yss.schedule.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootTest
class TaskTest {
    @Autowired
    private TaskService TaskService;
    @Test
    void contextLoads() {
        TaskService.sendEmailToUnGenerate();
    }

}
