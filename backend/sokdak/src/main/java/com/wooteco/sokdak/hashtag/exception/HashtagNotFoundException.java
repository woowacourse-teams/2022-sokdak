package com.wooteco.sokdak.hashtag.exception;

import com.wooteco.sokdak.advice.NotFoundException;

public class HashtagNotFoundException extends NotFoundException {

    private static final String MESSAGE = "해당 이름의 해시태그를 찾을 수 없습니다.";

    public HashtagNotFoundException() {
        super(MESSAGE);
    }
}
