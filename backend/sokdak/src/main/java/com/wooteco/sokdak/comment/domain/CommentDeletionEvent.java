package com.wooteco.sokdak.comment.domain;

public class CommentDeletionEvent {

    private final Long commentId;

    public CommentDeletionEvent(Long commentId) {
        this.commentId = commentId;
    }

    public Long getCommentId() {
        return commentId;
    }
}

