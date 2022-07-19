package com.wooteco.sokdak.post.domain;

import static com.wooteco.sokdak.util.fixture.MemberFixture.VALID_NICKNAME;
import static com.wooteco.sokdak.util.fixture.MemberFixture.VALID_PASSWORD;
import static com.wooteco.sokdak.util.fixture.MemberFixture.VALID_USERNAME;
import static org.assertj.core.api.Assertions.assertThat;

import com.wooteco.sokdak.member.domain.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PostTest {

    private Post post;

    @BeforeEach
    void setUp() {
        Member member = Member.builder()
                .id(1L)
                .username(VALID_USERNAME)
                .password(VALID_PASSWORD)
                .nickname(VALID_NICKNAME)
                .build();
        post = Post.builder()
                .title("제목")
                .content("본문")
                .member(member)
                .build();
    }

    @DisplayName("게시글 제목 수정")
    @Test
    void updateTitle() {
        post.updateTitle("변경된 제목", 1L);

        assertThat(post.getTitle()).isEqualTo("변경된 제목");
    }

    @DisplayName("게시글 본문 수정")
    @Test
    void updateContent() {
        post.updateContent("변경된 본문", 1L);

        assertThat(post.getContent()).isEqualTo("변경된 본문");
    }
}
