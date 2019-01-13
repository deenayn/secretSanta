package ru.omnicomm.santa.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.omnicomm.santa.ServerProperty;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Component
public class SmtpServer {

    @Autowired
    private ServerProperty serverProperty;

    public void sendMessage(String mail, String subject, String messageText) throws MessagingException {
        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", serverProperty.getSmtp());
        Session session = Session.getDefaultInstance(properties);
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(serverProperty.getEmail()));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(mail));
        message.setSubject(subject);
        message.setText(messageText);
        Transport.send(message);
    }

}
