package com.wooteco.sokdak.board.exception;

import com.wooteco.sokdak.advice.BadRequestException;

public class BoardNotFoundException extends BadRequestException {

    private static final String MESSAGE = "해당 게시판이 존재이지 않습니다";

    public BoardNotFoundException() {
        super(MESSAGE);
    }
}
