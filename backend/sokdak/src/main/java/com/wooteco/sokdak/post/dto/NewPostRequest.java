package com.wooteco.sokdak.post.dto;

import com.wooteco.sokdak.post.domain.Post;
import javax.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
public class NewPostRequest {

    @NotBlank(message = "제목은 1자 이상 50자 이하여야 합니다.")
    private String title;
    @NotBlank(message = "본문은 1자 이상 5000자 이하여야 합니다.")
    private String content;

    public NewPostRequest() {
    }

    @Builder
    public NewPostRequest(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public Post toEntity() {
        return Post.builder()
                .title(title)
                .content(content)
                .build();
    }
}
