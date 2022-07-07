package com.wooteco.sokdak.post.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.wooteco.sokdak.post.exception.InvalidPostException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

class PostTest {

    @DisplayName("제목이 없는 글을 등록할 시 예외 발생")
    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    @NullSource
    void create_Exception_NoTitle(String invalidTitle) {
        assertThatThrownBy(
                () -> Post.builder()
                        .title(invalidTitle)
                        .content("본문")
                        .build())
                .isInstanceOf(InvalidPostException.class);
    }

    @DisplayName("본문이 없는 글을 등록할 시 예외 발생")
    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    @NullSource
    void create_Exception_NoContent(String invalidContent){
        assertThatThrownBy(
                () -> Post.builder()
                        .title("제목")
                        .content(invalidContent)
                        .build())
                .isInstanceOf(InvalidPostException.class);
    }
}