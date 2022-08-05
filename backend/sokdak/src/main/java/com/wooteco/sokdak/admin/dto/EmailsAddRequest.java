package com.wooteco.sokdak.admin.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class EmailsAddRequest {
    private List<String> emails;

    public EmailsAddRequest() {
    }

    public EmailsAddRequest(List<String> emails) {
        this.emails = emails;
    }
}
