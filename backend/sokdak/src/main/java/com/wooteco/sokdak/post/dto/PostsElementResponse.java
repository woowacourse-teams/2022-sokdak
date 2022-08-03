package com.wooteco.sokdak.post.dto;

import com.wooteco.sokdak.post.domain.Post;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostsElementResponse {

    private final Long id;
    private final String title;
    private final String content;
    private final LocalDateTime createdAt;
    private final int likeCount;
    private final int commentCount;
    private final boolean modified;
    private final boolean blocked;

    @Builder
    private PostsElementResponse(Long id, String title, String content, LocalDateTime createdAt,
                                 int likeCount, int commentCount, boolean modified, boolean blocked) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.modified = modified;
        this.blocked = blocked;
    }

    public static PostsElementResponse from(Post post) {
        return PostsElementResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .createdAt(post.getCreatedAt())
                .likeCount(post.getLikeCount())
                .commentCount(post.getCommentCount())
                .modified(post.isModified())
                .blocked(post.isBlocked())
                .build();
    }
}
