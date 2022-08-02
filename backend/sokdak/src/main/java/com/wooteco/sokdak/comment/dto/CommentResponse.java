package com.wooteco.sokdak.comment.dto;

import com.wooteco.sokdak.comment.domain.Comment;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CommentResponse {

    private final Long id;
    private final String nickname;
    private final String content;
    private final LocalDateTime createdAt;
    private final boolean blocked;
    private final boolean authorized;

    public CommentResponse(Long id, String nickname, String content, LocalDateTime createdAt, boolean blocked, boolean authorized) {
        this.id = id;
        this.nickname = nickname;
        this.content = content;
        this.createdAt = createdAt;
        this.blocked = blocked;
        this.authorized = authorized;
    }

    public static CommentResponse of(Comment comment, Long accessMemberId) {
        return new CommentResponse(comment.getId(), comment.getNickname(), comment.getMessage(),
                comment.getCreatedAt(), comment.isBlocked(), comment.isAuthenticated(accessMemberId));
    }
}
