package com.spring.trelloclone.service;

import com.itextpdf.text.DocumentException;
import com.spring.trelloclone.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
public class MailService {

    private JavaMailSender emailSender;


    @Autowired
    public MailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void sendNotificationToAssignee(String recepientMail, Task task) throws MessagingException, DocumentException {
        MimeMessage message = emailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom("florianaralucadeftu@gmail.com");
        helper.setTo(recepientMail);

        helper.setSubject("Task assigned");
        helper.setText("You have been assigned a new task:  " + task.getTitle());
        emailSender.send(message);
    }

    public void sendAssignmentNotification(String email, Task task) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("New Task Assignment");
        message.setText("You have been assigned to the task: " + task.getTitle());
        emailSender.send(message);
    }


}
