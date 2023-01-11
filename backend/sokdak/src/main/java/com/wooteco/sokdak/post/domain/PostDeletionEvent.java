package com.wooteco.sokdak.post.domain;

public class PostDeletionEvent {

    private final Long postId;

    public PostDeletionEvent(Long postId) {
        this.postId = postId;
    }

    public Long getPostId() {
        return postId;
    }
}
