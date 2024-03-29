package com.wooteco.sokdak.comment.dto;

import com.wooteco.sokdak.comment.domain.Comment;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class ReplyResponse {

    private final Long id;
    private final String nickname;
    private final String content;
    private final LocalDateTime createdAt;
    private final boolean blocked;
    private final boolean postWriter;
    private final boolean authorized;
    private final int likeCount;
    private final boolean like;

    public ReplyResponse(Long id, String nickname, String content, LocalDateTime createdAt, boolean blocked,
                         boolean postWriter, boolean authorized, int likeCount, boolean like) {
        this.id = id;
        this.nickname = nickname;
        this.content = content;
        this.createdAt = createdAt;
        this.blocked = blocked;
        this.postWriter = postWriter;
        this.authorized = authorized;
        this.likeCount = likeCount;
        this.like = like;
    }

    public static ReplyResponse of(Comment reply, Long accessMemberId, boolean like) {
        return new ReplyResponse(reply.getId(), reply.getNickname(), reply.getMessage(), reply.getCreatedAt(),
                reply.isBlocked(), reply.isPostWriter(), reply.isAuthorized(accessMemberId),
                reply.getCommentLikesCount(), like);
    }
}
