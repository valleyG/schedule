package com.yss.schedule;

import com.yss.schedule.entity.WorkDay;
import com.yss.schedule.mapper.WorkDayDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootTest
class ScheduleApplicationTests {
    @Autowired
    private WorkDayDao workDayDao;
    @Test
    void contextLoads() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String currentDate = sdf.format(new Date());
        System.out.println(workDayDao.getWorKDay(currentDate, 1));
    }

}
