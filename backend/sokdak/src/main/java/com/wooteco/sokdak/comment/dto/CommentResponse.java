package com.wooteco.sokdak.comment.dto;

import com.wooteco.sokdak.comment.domain.Comment;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.Getter;

@Getter
//@JsonInclude(Include.NON_NULL)
public class CommentResponse {

    private final Long id;
    private final String nickname;
    private final String content;
    private final LocalDateTime createdAt;
    private final boolean blocked;
    private final boolean postWriter;
    private final boolean authorized;
    private final List<ReplyResponse> replies;

    public CommentResponse(Long id, String nickname, String content, LocalDateTime createdAt, boolean blocked,
                           boolean postWriter, boolean authorized, List<ReplyResponse> replies) {
        this.id = id;
        this.nickname = nickname;
        this.content = content;
        this.createdAt = createdAt;
        this.blocked = blocked;
        this.postWriter = postWriter;
        this.authorized = authorized;
        this.replies = replies;
    }

    public static CommentResponse of(Comment comment, Long accessMemberId, Map<Comment, Long> accessMemberIdByReply) {
        List<ReplyResponse> replyResponses = new ArrayList<>();
        for (Comment reply : accessMemberIdByReply.keySet()) {
            replyResponses.add(ReplyResponse.of(reply, accessMemberIdByReply.get(reply)));
        }

        return new CommentResponse(comment.getId(), comment.getNickname(), comment.getMessage(),
                comment.getCreatedAt(), comment.isBlocked(), comment.isPostWriter(),
                comment.isAuthenticated(accessMemberId), replyResponses);
    }

    public static CommentResponse softRemovedOf(Comment comment, Map<Comment, Long> accessMemberIdByReply) {
        List<ReplyResponse> replyResponses = new ArrayList<>();
        for (Comment reply : accessMemberIdByReply.keySet()) {
            replyResponses.add(ReplyResponse.of(reply, accessMemberIdByReply.get(reply)));
        }

        return new CommentResponse(comment.getId(), null, null, null, false,
                false, false, replyResponses);
    }
}
