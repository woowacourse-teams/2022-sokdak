package com.wooteco.sokdak.post.dto;

import com.wooteco.sokdak.post.domain.Post;
import lombok.Getter;

@Getter
public class PostResponse {

    private Long id;
    private String title;
    private String content;
    private DateResponse localDate;

    private PostResponse(Long id, String title, String content, DateResponse localDate) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.localDate = localDate;
    }

    public static PostResponse from(Post post) {
        return new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                DateResponse.from(post.getCreatedAt()));
    }
}
