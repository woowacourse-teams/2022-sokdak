package com.wooteco.sokdak.member.dto;

import lombok.Getter;

@Getter
public class EmailRequest {

    private String email;

    public EmailRequest() {
    }

    public EmailRequest(String email) {
        this.email = email;
    }
}
