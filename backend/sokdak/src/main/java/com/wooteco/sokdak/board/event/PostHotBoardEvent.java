package com.wooteco.sokdak.board.event;

public class PostHotBoardEvent {

    private final Long targetMemberId;
    private final Long postId;

    public PostHotBoardEvent(Long targetMemberId, Long postId) {
        this.targetMemberId = targetMemberId;
        this.postId = postId;
    }

    public Long getTargetMemberId() {
        return targetMemberId;
    }

    public Long getPostId() {
        return postId;
    }
}
