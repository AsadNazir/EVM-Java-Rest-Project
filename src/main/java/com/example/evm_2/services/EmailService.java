package com.example.evm_2.services;

import com.example.evm_2.commons.Credentials;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;


//Email Service class use to send emails
public class EmailService {

    private static volatile EmailService instance;

    private final String username = "nazirgill1054@gmail.com"; // Your Gmail address
    private final String password = Credentials.EMAIL_PWD; // Your Gmail password

    private EmailService() {
        // Private constructor to prevent instantiation
    }

    public static EmailService getInstance() {
        if (instance == null) {
            synchronized (EmailService.class) {
                if (instance == null) {
                    instance = new EmailService();
                }
            }
        }
        return instance;
    }

    public boolean sendMail(String recipient, String Msg) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject("Subject");
            message.setText(Msg);

            Transport.send(message);

            return true; // Email sent successfully

        } catch (MessagingException e) {
            e.printStackTrace();
            return false; // Failed to send email
        }
    }
}
