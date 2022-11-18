package com.wooteco.sokdak.report.event;

public class PostReportEvent {

    private final Long targetMemberId;
    private final Long postId;

    public PostReportEvent(Long targetMemberId, Long postId) {
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
