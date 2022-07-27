package com.wooteco.sokdak.member.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailSenderImpl implements EmailSender {

    public static final String FROM = "sokdakX2@gmail.com";

    private final JavaMailSender javaMailSender;

    public EmailSenderImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void send(String email, String authCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(FROM);
        message.setTo(email);
        message.setSubject("속닥속닥 인증코드입니다.");
        message.setText(authCode);

        javaMailSender.send(message);
    }
}
