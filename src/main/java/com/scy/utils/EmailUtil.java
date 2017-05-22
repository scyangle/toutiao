package com.scy.utils;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.ui.velocity.VelocityEngineUtils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by Shichengyao on 5/14/17.
 */
@Component
public class EmailUtil {
    @Value("${mail.smtp.auth}")
    private String auth;
    @Value("${mail.transport.protocol}")
    private String protocol;
    @Value("${mail.smtp.host}")
    private String host;
    @Value("{mail.smtp.port}")
    private String port;
    @Value("${mail.Authenticator.userName}")
    private String userName;
    @Value("${mail.Authenticator.password}")
    private String password;
    @Autowired
    private VelocityEngine velocityEngine;

    /**
     * send a register email
     *
     * @param to : the received address
     */
    public void sendRegisterEmail(String to) throws Exception {
        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", protocol);
        props.put("mail.smtp.auth", auth);
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        Session session = Session.getDefaultInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, password);
            }
        });
        session.setDebug(true);
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(userName));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
        message.setSubject("code");
        Map map = new HashMap();
        map.put("userName", to);
        String result = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine,"mail/welcome.html","UTF-8",map);
        message.setContent(result, "text/html;charset=utf-8");
        Transport.send(message);
    }
}
