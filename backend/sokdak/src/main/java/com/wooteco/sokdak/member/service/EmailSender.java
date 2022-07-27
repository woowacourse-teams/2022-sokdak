package com.wooteco.sokdak.member.service;

public interface EmailSender {

    void send(String email, String authCode);
}
