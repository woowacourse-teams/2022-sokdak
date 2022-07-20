package com.wooteco.sokdak.comment.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.wooteco.sokdak.comment.exception.InvalidMessageException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

class MessageTest {

    @DisplayName("댓글 메시지 255자 초과 시 예외 발생")
    @Test
    void create_Exception_MessageLength() {
        String longerThan256 = "a".repeat(256);
        assertThatThrownBy(() -> new Message(longerThan256))
                .isInstanceOf(InvalidMessageException.class);
    }

    @DisplayName("댓글 메시지 없을 시 예외 발생")
    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    @NullSource
    void create_Exception_NoMessage(String invalid) {
        assertThatThrownBy(() -> new Message(invalid))
                .isInstanceOf(InvalidMessageException.class);
    }
}