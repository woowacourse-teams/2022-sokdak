package com.wooteco.sokdak.post.dto;

import com.wooteco.sokdak.post.domain.Post;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class PostsElementResponse {

    private final Long id;
    private final String title;
    private final String content;
    private final LocalDateTime createdAt;
    private final boolean modified;

    public PostsElementResponse(Long id, String title, String content, LocalDateTime createdAt, boolean modified) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.modified = modified;
    }

    public static PostsElementResponse from(Post post) {
        return new PostsElementResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getCreatedAt(),
                post.isModified());
    }
}
