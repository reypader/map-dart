package com.dart.common.service.mail;

/**
 * @author RMPader
 */
public interface MailSenderService {

    void sendMail(String recipientAddress, String recipientName, String subject, String message);

}
