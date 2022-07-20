package com.wooteco.sokdak.post.dto;

import com.wooteco.sokdak.post.domain.Post;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import lombok.Getter;

@Getter
public class PostDetailResponse {

    private final Long id;
    private final String title;
    private final String content;
    private final List<HashtagResponse> hashtags;
    private final LocalDateTime createdAt;
    private final boolean authorized;
    private final boolean modified;

    public PostDetailResponse(Long id, String title, String content, List<HashtagResponse> hashtagResponses,
                              LocalDateTime createdAt, boolean authorized, boolean modified) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.hashtags = hashtagResponses;
        this.createdAt = createdAt;
        this.authorized = authorized;
        this.modified = modified;
    }

    public static PostDetailResponse of(Post post, boolean authorized) {
        return new PostDetailResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                Collections.emptyList(),
                post.getCreatedAt(),
                authorized,
                post.isModified());
    }
}
