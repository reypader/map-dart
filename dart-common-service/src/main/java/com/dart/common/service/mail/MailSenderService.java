package com.dart.common.service.mail;

import javax.mail.MessagingException;

/**
 * @author RMPader
 */
public interface MailSenderService {

    void sendMail(String recipientAddress, String recipientName, String subject, String message) throws MessagingException;

}
