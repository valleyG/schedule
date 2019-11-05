package com.yss.schedule.kettle;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class MailService {
    static String HOST = "hwsmtp.qiye.163.com";
    static Integer PORT = 994;
    static String FROM = "zhuhongmin@ysstech.com";
    static String TO = "";
    static String AFFIX = "";
    static String AFFIXNAME = "";
    static String USER = "zhuhongmin@ysstech.com";
    static String PWD = "Zhm092324";
    static String SUBJECT = "测试一下邮件";
    static String[] TOS = {"zhm1230982006@qq.com"};


    /**
     * 发送邮件
     *
     */
    public static void send(String context) {
        Properties props = new Properties();
        //设置发送邮件的邮件服务器的属性（这里使用网易的smtp服务器）
        props.put("mail.smtp.host", HOST);
        props.put("mail.smtp.port", PORT);
        //打开ssl验证服务
        props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        //需要经过授权，也就是有户名和密码的校验，这样才能通过验证（一定要有这一条）
        props.put("mail.smtp.auth", "true");
        //用props对象构建一个session
        Session session = Session.getDefaultInstance(props);
        session.setDebug(true);
        //用session为参数定义消息对象
        MimeMessage message = new MimeMessage(session);
        try {
            // 加载发件人地址
            message.setFrom(new InternetAddress(FROM));
            // 加载收件人地址
            InternetAddress[] sendTo = new InternetAddress[TOS.length];
            for (int i = 0; i < TOS.length; i++) {
                sendTo[i] = new InternetAddress(TOS[i]);
            }
            message.addRecipients(Message.RecipientType.TO, sendTo);
            //设置在发送给收信人之前给自己（发送方）抄送一份，不然会被当成垃圾邮件，报554错
            // message.addRecipients(MimeMessage.RecipientType.CC, InternetAddress.parse(FROM));
            //加载标题
            message.setSubject(SUBJECT);
            //向multipart对象中添加邮件的各个部分内容，包括文本内容和附件
            Multipart multipart = new MimeMultipart();
            //设置邮件的文本内容
            BodyPart contentPart = new MimeBodyPart();
            contentPart.setText(context);
            multipart.addBodyPart(contentPart);
            if (!AFFIX.isEmpty()) {//添加附件
                BodyPart messageBodyPart = new MimeBodyPart();
                DataSource source = new FileDataSource(AFFIX);
                messageBodyPart.setDataHandler(new DataHandler(source));//添加附件的内容
                sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();//添加附件的标题
                messageBodyPart.setFileName("=?GBK?B?" + enc.encode(AFFIXNAME.getBytes()) + "?=");
                multipart.addBodyPart(messageBodyPart);
            }
            message.setContent(multipart);//将multipart对象放到message中
            message.saveChanges(); //保存邮件
            Transport transport = session.getTransport("smtp");//发送邮件
            transport.connect(HOST, USER, PWD);//连接服务器的邮箱
            transport.sendMessage(message, message.getAllRecipients());//把邮件发送出去
            transport.close();//关闭连接
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
