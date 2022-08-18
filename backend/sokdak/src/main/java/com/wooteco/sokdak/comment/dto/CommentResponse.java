package com.wooteco.sokdak.comment.dto;

import com.wooteco.sokdak.comment.domain.Comment;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;

@Getter
public class CommentResponse {

    private final Long id;
    private final String nickname;
    private final String content;
    private final LocalDateTime createdAt;
    private final boolean blocked;
    private final boolean postWriter;
    private final boolean authorized;
    private final int likeCount;
    private final boolean like;
    private final List<ReplyResponse> replies;

    public CommentResponse(Long id, String nickname, String content, LocalDateTime createdAt, boolean blocked,
                           boolean postWriter, boolean authorized, int likeCount, boolean like,
                           List<ReplyResponse> replies) {
        this.id = id;
        this.nickname = nickname;
        this.content = content;
        this.createdAt = createdAt;
        this.blocked = blocked;
        this.postWriter = postWriter;
        this.authorized = authorized;
        this.likeCount = likeCount;
        this.like = like;
        this.replies = replies;
    }

    public static CommentResponse of(Comment comment, Long accessMemberId, List<ReplyResponse> replyResponses,
                                     boolean isLike) {
        return new CommentResponse(comment.getId(), comment.getNickname(), comment.getMessage(),
                comment.getCreatedAt(), comment.isBlocked(), comment.isPostWriter(),
                comment.isAuthorized(accessMemberId), comment.getCommentLikesCount(), isLike, replyResponses);
    }

    public static CommentResponse softRemovedOf(Comment comment, List<ReplyResponse> replyResponses) {
        return new CommentResponse(comment.getId(), null, null, null, false,
                false, false, 0, false, replyResponses);
    }
}
