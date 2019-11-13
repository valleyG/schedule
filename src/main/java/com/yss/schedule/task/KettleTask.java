package com.yss.schedule.task;

import com.yss.schedule.service.KettleService;
import org.pentaho.di.core.exception.KettleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author yss
 */
@Component
public class KettleTask {
    private Logger logger = LoggerFactory.getLogger(KettleTask.class);

    private static final String ON = "1";


    @Autowired
    private KettleService kettleService;


    @Value("${com.yss.kettle.trans.switch}")
    private String callTransTaskSwitch;

    @Value("${com.yss.kettle.job.switch}")
    private String callJobTaskSwitch;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    @Scheduled(cron = "${com.yss.kettle.trans.cron}")
    public void callTransTask(){
        if (ON.equals(callTransTaskSwitch)){

            logger.info("调度开始执行：{}",sdf.format(new Date()));
            try {
                kettleService.callTransferTask();
            } catch (KettleException e) {
                logger.error("kettle调用异常",e);
            }
            logger.info("调度结束：{}",sdf.format(new Date()));
        }
    }



    @Scheduled(cron = "${com.yss.kettle.job.cron}")
    public void callJobTask(){
        if (ON.equals(callJobTaskSwitch)){
            try {
                logger.info("kettle任务调用开始：{}",sdf.format(new Date()));
                kettleService.callJobTask();
                logger.info("kettle任务调用结束：{}",sdf.format(new Date()));
            } catch (Exception e) {
                logger.error("kettle调用异常",e);
            }
        }
    }

    /**
     * 为了出人行报表调用kettle
     */
    @Scheduled(cron = "${com.yss.kettle.month.job.cron}")
    public void callMonthKettleJob(){
        try {
            kettleService.callMonthKettleJob();
        }catch (Exception e){
            logger.error("kettle调用失败",e);
        }
    }
}
