package com.wooteco.sokdak.post.exception;

public class PostNotFoundException extends NotFoundException {

    private static final String MESSAGE = "게시물을 찾을 수 없습니다. : id = %d";

    public PostNotFoundException(Long id) {
        super(String.format(MESSAGE, id));
    }
}
