package com.wooteco.sokdak.comment.exception;

import com.wooteco.sokdak.advice.NotFoundException;

public class CommentNotFoundException extends NotFoundException {

    private static final String MESSAGE = "댓글을 찾을 수 없습니다.";

    public CommentNotFoundException() {
        super(MESSAGE);
    }
}
