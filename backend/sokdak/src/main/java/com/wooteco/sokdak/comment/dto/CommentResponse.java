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

    public CommentResponse(Long id, String nickname, String content, LocalDateTime createdAt, boolean blocked) {
        this.id = id;
        this.nickname = nickname;
        this.content = content;
        this.createdAt = createdAt;
        this.blocked = blocked;
    }

    public static CommentResponse of(Comment comment) {
        return new CommentResponse(comment.getId(), comment.getNickname(), comment.getMessage(),
                comment.getCreatedAt(), comment.isBlocked());
    }
}
