package com.wooteco.sokdak.post.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ViewCountTest {

    @DisplayName("조회수를 1 증가시킨다.")
    @Test
    void addViewCount() {
        ViewCount viewCount = new ViewCount();
        int originViewCount = viewCount.getViewCount();

        viewCount.addViewCount();

        assertThat(originViewCount + 1).isEqualTo(viewCount.getViewCount());
    }
}
