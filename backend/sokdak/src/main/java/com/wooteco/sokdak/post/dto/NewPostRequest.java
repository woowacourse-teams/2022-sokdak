package com.wooteco.sokdak.post.dto;

import java.util.List;
import javax.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class NewPostRequest {

    @NotBlank(message = "제목은 1자 이상 50자 이하여야 합니다.")
    private String title;
    @NotBlank(message = "본문은 1자 이상 5000자 이하여야 합니다.")
    private String content;
    private boolean anonymous;
    private List<String> hashtags;

    public NewPostRequest() {
    }

    public NewPostRequest(String title, String content, boolean anonymous, List<String> hashtags) {
        this.title = title;
        this.content = content;
        this.anonymous = anonymous;
        this.hashtags = hashtags;
    }
}
