package com.user.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender
            mailSender;

    public void sendOtp(
            String to,
            String otp) {

        SimpleMailMessage message =
                new SimpleMailMessage();

        message.setTo(to);

        message.setSubject(
                "Password Reset OTP");

        message.setText(
                "Your OTP is: " + otp);

        mailSender.send(message);
    }
}
