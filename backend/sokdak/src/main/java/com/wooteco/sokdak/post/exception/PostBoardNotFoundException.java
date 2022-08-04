package com.wooteco.sokdak.post.exception;

import com.wooteco.sokdak.advice.BadRequestException;

public class PostBoardNotFoundException extends BadRequestException {

    private static final String MESSAGE = "게시글의 게시판이 존재하지 않습니다.";

    public PostBoardNotFoundException() {
        super(MESSAGE);
    }
}
