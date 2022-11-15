package com.wooteco.sokdak.comment.event;

public class NewReplyEvent {

    private final Long commentMemberId;
    private final Long postId;
    private final Long commentId;
    private final Long replyMemberId;

    public NewReplyEvent(Long commentMemberId, Long postId, Long commentId, Long replyMemberId) {
        this.commentMemberId = commentMemberId;
        this.postId = postId;
        this.commentId = commentId;
        this.replyMemberId = replyMemberId;
    }

    public Long getCommentMemberId() {
        return commentMemberId;
    }

    public Long getPostId() {
        return postId;
    }

    public Long getCommentId() {
        return commentId;
    }

    public Long getReplyMemberId() {
        return replyMemberId;
    }
}
