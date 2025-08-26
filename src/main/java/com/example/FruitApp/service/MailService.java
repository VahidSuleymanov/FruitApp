package com.example.FruitApp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    public void sendOtp(String toEmail, String otp) {
        SimpleMailMessage m = new SimpleMailMessage();
        m.setTo(toEmail);
        m.setSubject("FruitApp - OTP Kodu");
        m.setText("""
                Şifrə yeniləmə üçün OTP kodunuz: %s
                Kod 15 dəqiqə ərzində keçərlidir.
                """.formatted(otp));
        mailSender.send(m);
    }

}

