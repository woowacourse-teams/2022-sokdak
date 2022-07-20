package com.wooteco.sokdak.comment.domain;

import com.wooteco.sokdak.comment.exception.InvalidMessageException;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.Getter;

@Getter
@Embeddable
public class Message {

    private static final int MAX_MESSAGE_LENGTH = 255;

    @Column(name = "message", nullable = false)
    private String value;

    protected Message() {
    }

    public Message(String value) {
        validate(value);
        this.value = value;
    }

    private void validate(String value) {
        if (value == null || value.isBlank()) {
            throw new InvalidMessageException();
        }
        if (value.length() > MAX_MESSAGE_LENGTH) {
            throw new InvalidMessageException();
        }
    }
}
