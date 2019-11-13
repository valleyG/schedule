package com.yss.schedule.service;

import com.yss.schedule.emue.TaskStatusEnum;
import com.yss.schedule.entity.TaskEntity;
import com.yss.schedule.mapper.ReportTaskDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TaskService {
    @Autowired
    private ReportTaskDao reportTaskDao;
    @Autowired
    private MailService mailService;

    private Logger logger = LoggerFactory.getLogger(TaskService.class);

    public Boolean sendEmailToUnGenerate() {
        Map<String, StringBuilder> emailToContent = new HashMap<>();
        try {

            List<TaskEntity> taskList = reportTaskDao.getTaskListByStatus(TaskStatusEnum.UNGENERATE.getStatus());
            for (TaskEntity taskEntity : taskList) {
                List<String> emails = reportTaskDao.getTheEmailForUnGenerateTask(taskEntity.getReportId());
                transEmail(emailToContent, taskEntity, emails);
            }

            for (Map.Entry<String, StringBuilder> entry : emailToContent.entrySet()) {
                List<String> emailAttrs = new ArrayList<>();
                emailAttrs.add(entry.getKey());
                mailService.send("系统检测到您有以下报表未生成:\n" + entry.getValue().toString()+"\n如有打扰请谅解！",emailAttrs);
                logger.info("邮件发送成功，接收人：{},发送内容：{}",emailAttrs.get(0),"系统检测到您有以下报表未生成:\n" + entry.getValue().toString()+"\n如有打扰请谅解！");
            }
            //mailService.send(reportNames.substring(0, reportNames.length() - 1).toString(), emailAttrs);
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    public Boolean sendEmailToUnAudit() {
        Map<String, StringBuilder> EmailToContent = new HashMap<>();
        List<TaskEntity> tasks = reportTaskDao.getTaskListByStatus(TaskStatusEnum.UNAUDIT.getStatus());
        for (TaskEntity taskEntity : tasks) {
            List<String> emails = reportTaskDao.getTheEmailForUnAuditTask(taskEntity.getReportId());
            transEmail(EmailToContent, taskEntity, emails);
        }

        for (Map.Entry<String, StringBuilder> entry : EmailToContent.entrySet()) {
            List<String> emailAttrs = new ArrayList<>();
            emailAttrs.add(entry.getKey());
            mailService.send("系统检测到您有以下报表未审核:\n" + entry.getValue().toString()+"\n如有打扰请谅解！",emailAttrs);
            logger.info("邮件发送成功，接收人：{},发送内容：{}",emailAttrs.get(0),"系统检测到您有以下报表未生成:\n" + entry.getValue().toString()+"\n如有打扰请谅解！");
        }
        return true;
    }

    private void transEmail(Map<String, StringBuilder> emailToContent, TaskEntity taskEntity, List<String> emails) {
        for (String email : emails) {
            if (null == emailToContent.get(email)) {
                StringBuilder content = new StringBuilder();
                content.append(taskEntity.getReportId()).append(taskEntity.getReportName()).append("\n");
                emailToContent.put(email, content);
            } else {
                emailToContent.get(email).append(taskEntity.getReportId()).append(taskEntity.getReportName()).append("\n");
            }
        }
    }


}
