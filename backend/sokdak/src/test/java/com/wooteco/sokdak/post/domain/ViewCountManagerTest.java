package com.wooteco.sokdak.post.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ViewCountManagerTest {

    private ViewCountManager viewCountManager = new ViewCountManager();

    @DisplayName("오늘 방문한 post인지 확인한다. 방문했으면 true, 아니면 false를 반환한다.")
    @ParameterizedTest
    @MethodSource("argsOfIsFirstAccess")
    void isFirstAccess(String logs, Long postId, boolean expected) {
        boolean result = viewCountManager.isFirstAccess(logs, postId);

        assertThat(result).isEqualTo(expected);
    }

    // <DATE>:1/2/3&
    static Stream<Arguments> argsOfIsFirstAccess() {
        int today = LocalDateTime.now().getDayOfMonth();
        int yesterday = today - 1;
        return Stream.of(
                Arguments.of("", 1L, true),
                Arguments.of(today + ":1/2/3", 4L, true),
                Arguments.of(today+":1/2/3", 3L, false),
                Arguments.of(yesterday + ":1/2/3", 3L, true),
                Arguments.of(yesterday + ":1/2/3&" + today + ":1/2", 3L, true),
                Arguments.of(yesterday + ":1/2/3&" + today + ":1/2/3", 3L, false)
        );
    }

    @DisplayName("쿠키에 방문한 post에 대한 log를 추가한다.")
    @ParameterizedTest
    @MethodSource("argsOfAddAccessLogToCookie")
    void addAccessLogToCookie(String logs, Long postId, String expectedUpdatedLog) {
        String updatedLog = viewCountManager.getUpdatedLog(logs, postId);

        assertThat(updatedLog).isEqualTo(expectedUpdatedLog);
    }

    static Stream<Arguments> argsOfAddAccessLogToCookie() {
        int today = LocalDateTime.now().getDayOfMonth();
        int yesterday = today - 1;
        return Stream.of(
                Arguments.of("", 1L, today + ":1"),
                Arguments.of(yesterday + ":1/2/3", 1L, today + ":1"),
                Arguments.of(today + ":1/2/3", 4L, today + ":1/2/3/4"),
                Arguments.of(yesterday + ":1/2/3&" + today + ":1/2", 3L, today + ":1/2/3")
        );
    }
}
