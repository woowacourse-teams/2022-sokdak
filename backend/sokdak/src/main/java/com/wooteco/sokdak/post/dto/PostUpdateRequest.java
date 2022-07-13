package com.wooteco.sokdak.post.dto;

import javax.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class PostUpdateRequest {

    @NotBlank(message = "제목 혹은 본문이 없습니다.")
    private String title;
    @NotBlank(message = "제목 혹은 본문이 없습니다.")
    private String content;

    public PostUpdateRequest() {
    }

    public PostUpdateRequest(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
