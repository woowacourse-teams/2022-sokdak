package com.wooteco.sokdak.member.service;

import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailSenderImpl implements EmailSender {

    private final MailProperties properties;
    private final JavaMailSender javaMailSender;

    public EmailSenderImpl(MailProperties properties, JavaMailSender javaMailSender) {
        this.properties = properties;
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void send(String email, String authCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(properties.getUsername());
        message.setTo(email);
        message.setSubject("속닥속닥 인증코드입니다.");
        message.setText(authCode);

        javaMailSender.send(message);
    }
}
