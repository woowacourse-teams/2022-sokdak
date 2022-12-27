package com.wooteco.sokdak.board.event;

public class PostHotBoardEvent {

    private final Long notificationTargetMemberId;
    private final Long postId;

    public PostHotBoardEvent(Long notificationTargetMemberId, Long postId) {
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
