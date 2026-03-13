package com.niranjan.serviceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    public void sendMail(String to, String link){
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject("Reset Password of expense Tracker");
        simpleMailMessage.setText("""
                Hi, 
                We received a request to reset your password.
                Click the link below to reset it (Valid for 15 mins only).
                %s
                If you did not request this, please ignore this email.
                
                Thanks
                """.formatted(link));

        javaMailSender.send(simpleMailMessage);
    }

}
