package com.wooteco.sokdak.post.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PostTest {

    @DisplayName("게시글 제목 수정")
    @Test
    void updateTitle() {
        Post post = Post.builder()
                .title("제목")
                .content("본문")
                .build();

        post.updateTitle("변경된 제목");

        assertThat(post.getTitle()).isEqualTo("변경된 제목");
    }

    @DisplayName("게시글 본문 수정")
    @Test
    void updateContent() {
        Post post = Post.builder()
                .title("제목")
                .content("본문")
                .build();

        post.updateContent("변경된 본문");

        assertThat(post.getContent()).isEqualTo("변경된 본문");
    }
}
