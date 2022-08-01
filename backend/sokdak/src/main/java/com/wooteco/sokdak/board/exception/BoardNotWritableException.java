package com.wooteco.sokdak.board.exception;

import com.wooteco.sokdak.advice.BadRequestException;

public class BoardNotWritableException extends BadRequestException {

    private static final String MESSAGE = "해당 게시판에는 직접 게시글 작성이 불가능합니다.";

    public BoardNotWritableException() {
        super(MESSAGE);
    }
}
