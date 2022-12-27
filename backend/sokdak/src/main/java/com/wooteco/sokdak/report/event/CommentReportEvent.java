package com.wooteco.sokdak.report.event;

public class CommentReportEvent {

    private final Long notificationTargetMemberId;
    private final Long postId;
    private final Long commentId;

    public CommentReportEvent(Long notificationTargetMemberId, Long postId, Long commentId) {
        this.notificationTargetMemberId = notificationTargetMemberId;
        this.postId = postId;
        this.commentId = commentId;
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
}
