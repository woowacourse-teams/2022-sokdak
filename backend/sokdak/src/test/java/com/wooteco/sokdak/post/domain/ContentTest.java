package com.wooteco.sokdak.post.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.wooteco.sokdak.post.exception.InvalidContentException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

class ContentTest {

    @DisplayName("본문 5000자 초과 시 예외 발생")
    @Test
    void create_Exception_ContentLength() {
        String longerThanFiveThousands = "a".repeat(5001);
        assertThatThrownBy(() -> new Content(longerThanFiveThousands))
                .isInstanceOf(InvalidContentException.class);
    }

    @DisplayName("본문 내용 없을 시 예외 발생")
    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    @NullSource
    void create_Exception_NoContent(String invalid) {
        assertThatThrownBy(() -> new Content(invalid))
                .isInstanceOf(InvalidContentException.class);
    }
}
