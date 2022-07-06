package com.wooteco.sokdak.post.dto;

import com.wooteco.sokdak.post.domain.Post;
import lombok.Builder;
import lombok.Getter;

@Getter
public class NewPostRequest {

    private String title;
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
