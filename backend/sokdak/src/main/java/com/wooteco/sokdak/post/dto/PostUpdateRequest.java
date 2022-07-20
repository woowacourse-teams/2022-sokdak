package com.wooteco.sokdak.post.dto;

import java.util.List;
import javax.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class PostUpdateRequest {

    @NotBlank(message = "제목 혹은 본문이 없습니다.")
    private String title;
    @NotBlank(message = "제목 혹은 본문이 없습니다.")
    private String content;
    private List<String> hashtags;

    public PostUpdateRequest() {
    }

    public PostUpdateRequest(String title, String content, List<String> hashtags) {
        this.title = title;
        this.content = content;
        this.hashtags = hashtags;
    }
}
