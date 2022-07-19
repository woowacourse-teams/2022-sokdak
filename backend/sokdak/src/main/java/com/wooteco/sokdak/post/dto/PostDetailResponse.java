package com.wooteco.sokdak.post.dto;

import com.wooteco.sokdak.post.domain.Post;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class PostDetailResponse {

    private final Long id;
    private final String title;
    private final String content;
    private final LocalDateTime createdAt;
    private final boolean authorized;
    private final boolean modified;

    public PostDetailResponse(Long id, String title, String content, LocalDateTime createdAt, boolean authorized,
                              boolean modified) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.authorized = authorized;
        this.modified = modified;
    }

    public static PostDetailResponse of(Post post, boolean authorized) {
        return new PostDetailResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getCreatedAt(),
                authorized,
                post.isModified());
    }
}
