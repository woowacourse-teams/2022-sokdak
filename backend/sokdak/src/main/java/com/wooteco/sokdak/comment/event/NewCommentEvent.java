package com.wooteco.sokdak.comment.event;

public class NewCommentEvent {

    private final Long targetMemberId;
    private final Long postId;
    private final Long commentId;
    private final Long commentMemberId;

    public NewCommentEvent(Long targetMemberId, Long postId, Long commentId, Long commentMemberId) {
        this.targetMemberId = targetMemberId;
        this.postId = postId;
        this.commentId = commentId;
        this.commentMemberId = commentMemberId;
    }

    public Long getTargetMemberId() {
        return targetMemberId;
    }

    public Long getPostId() {
        return postId;
    }

    public Long getCommentId() {
        return commentId;
    }

    public Long getCommentMemberId() {
        return commentMemberId;
    }
}
