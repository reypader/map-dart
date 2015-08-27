package com.dart.common.service.mail;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * A mail sender service that purely uses the classes in javax.mail
 */
public class GenericMailSenderService implements MailSenderService {

    private static Logger logger = Logger.getLogger(GenericMailSenderService.class.getName());

    @Override
    public void sendMail(String recipientAddress, String recipientName, String subject, String message) throws MessagingException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress("reynaldmpader@gmail.com", "no-reply-pings-admin"));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientAddress));
            msg.setSubject(subject);
            msg.setText(message);
            Transport.send(msg);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
