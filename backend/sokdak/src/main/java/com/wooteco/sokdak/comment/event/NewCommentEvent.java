package com.wooteco.sokdak.comment.event;

public class NewCommentEvent {

    private final Long notificationTargetMemberId;
    private final Long postId;
    private final Long commentId;
    private final Long commentWritingMemberId;

    public NewCommentEvent(Long targetMemberId, Long postId, Long commentId, Long commentWritingMemberId) {
        this.notificationTargetMemberId = targetMemberId;
        this.postId = postId;
        this.commentId = commentId;
        this.commentWritingMemberId = commentWritingMemberId;
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

    public Long getCommentWritingMemberId() {
        return commentWritingMemberId;
    }
}
