package com.wooteco.sokdak.comment.event;

public class NewReplyEvent {

    private final Long notificationTargetMemberId;
    private final Long postId;
    private final Long commentId;
    private final Long replyWritingMemberId;

    public NewReplyEvent(Long notificationTargetMemberId, Long postId, Long commentId, Long replyMemberId) {
        this.notificationTargetMemberId = notificationTargetMemberId;
        this.postId = postId;
        this.commentId = commentId;
        this.replyWritingMemberId = replyMemberId;
    }

    public Long getNotificationTargetMemberId() {
        return notificationTargetMemberId;
    }

    public Long getPostId() {
        return postId;
    }

    public Long getCommentId() {
        return commentId;
    }

    public Long getReplyWritingMemberId() {
        return replyWritingMemberId;
    }
}
