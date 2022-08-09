package com.wooteco.sokdak.post.dto;

import com.wooteco.sokdak.post.domain.Post;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostsElementResponse {

    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private int likeCount;
    private int commentCount;
    private boolean modified;
    private boolean blocked;

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
