package com.wooteco.sokdak.admin.dto;

import java.util.List;
import javax.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class EmailsAddRequest {

    @NotBlank(message = "추가할 이메일을 입력해주세요.")
    private List<String> emails;

    public EmailsAddRequest() {
    }

    public EmailsAddRequest(List<String> emails) {
        this.emails = emails;
    }
}
