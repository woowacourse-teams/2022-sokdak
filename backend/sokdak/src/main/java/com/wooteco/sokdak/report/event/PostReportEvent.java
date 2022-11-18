package com.wooteco.sokdak.report.event;

public class PostReportEvent {

    private final Long notificationTargetMemberId;
    private final Long postId;

    public PostReportEvent(Long notificationTargetMemberId, Long postId) {
        this.notificationTargetMemberId = notificationTargetMemberId;
        this.postId = postId;
    }

    public Long getNotificationTargetMemberId() {
        return notificationTargetMemberId;
    }

    public Long getPostId() {
        return postId;
    }
}
