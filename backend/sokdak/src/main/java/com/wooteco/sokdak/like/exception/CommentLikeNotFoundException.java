package com.wooteco.sokdak.like.exception;

public class CommentLikeNotFoundException extends RuntimeException {

    private static final String MESSAGE = "해당 회원이 누른 좋아요가 존재하지 않습니다.";

    public CommentLikeNotFoundException() {
        super(MESSAGE);
    }
}
