package com.wooteco.sokdak.post.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.wooteco.sokdak.post.exception.InvalidTitleException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

class TitleTest {

    @DisplayName("제목 50자 초과 시 예외 발생")
    @Test
    void create_Exception_ContentLength() {
        String longerThanFiveThousands = "a".repeat(51);
        assertThatThrownBy(() -> new Title(longerThanFiveThousands))
                .isInstanceOf(InvalidTitleException.class);
    }

    @DisplayName("제목 내용 없을 시 예외 발생")
    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    @NullSource
    void create_Exception_NoContent(String invalid) {
        assertThatThrownBy(() -> new Title(invalid))
                .isInstanceOf(InvalidTitleException.class);
    }
}
