package com.yss.schedule.service;

import org.apache.ibatis.javassist.bytecode.annotation.BooleanMemberValue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.List;
import java.util.Properties;
/**
 * @author yss
 */
@Service
public class MailService {
    @Value("${com.yss.email.host}")
    private String host;
    @Value("${com.yss.email.port}")
    private Integer port;
    @Value("${com.yss.email.from}")
    private String from;
    @Value("${com.yss.email.username}")
    private String user;
    @Value("${com.yss.email.pwd}")
    private String pwd;
    @Value("${com.yss.email.title}")
    private String title;


    /**
     * 发送邮件
     */
    public void send(String content, List<String> emailAttrs) {
        Properties props = new Properties();
        //设置发送邮件的邮件服务器的属性（这里使用网易的smtp服务器）
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        //打开ssl验证服务
        props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        //需要经过授权，也就是有户名和密码的校验，这样才能通过验证（一定要有这一条）
        props.put("mail.smtp.auth", "true");
        //用props对象构建一个session
        Session session = Session.getDefaultInstance(props);
        session.setDebug(false);
        //用session为参数定义消息对象
        MimeMessage message = new MimeMessage(session);
        try {
            // 加载发件人地址
            message.setFrom(new InternetAddress(from));
            // 加载收件人地址
            InternetAddress[] sendTo = new InternetAddress[emailAttrs.size()];
            for (int i = 0; i < emailAttrs.size(); i++) {
                sendTo[i] = new InternetAddress(emailAttrs.get(i));
            }
            message.addRecipients(Message.RecipientType.TO, sendTo);
            //设置在发送给收信人之前给自己（发送方）抄送一份，不然会被当成垃圾邮件，报554错
            // message.addRecipients(MimeMessage.RecipientType.CC, InternetAddress.parse(FROM));
            //加载标题
            message.setSubject(title);
            //向multipart对象中添加邮件的各个部分内容，包括文本内容和附件
            Multipart multipart = new MimeMultipart();
            //设置邮件的文本内容
            BodyPart contentPart = new MimeBodyPart();
            contentPart.setText(content);
            multipart.addBodyPart(contentPart);
            //将multipart对象放到message中
            message.setContent(multipart);
            //保存邮件
            message.saveChanges();
            //发送邮件
            Transport transport = session.getTransport("smtp");
            //连接服务器的邮箱
            transport.connect(host, user, pwd);
            //把邮件发送出去
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();//关闭连接
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
