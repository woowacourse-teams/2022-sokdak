package com.wooteco.sokdak.post.domain;

import static com.wooteco.sokdak.util.fixture.MemberFixture.VALID_NICKNAME;
import static com.wooteco.sokdak.util.fixture.MemberFixture.VALID_PASSWORD;
import static com.wooteco.sokdak.util.fixture.MemberFixture.VALID_USERNAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.wooteco.sokdak.auth.exception.AuthenticationException;
import com.wooteco.sokdak.member.domain.Member;
import com.wooteco.sokdak.report.domain.PostReport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PostTest {

    private Post post;
    private Member member;

    @BeforeEach
    void setUp() {
        member = Member.builder()
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

    @DisplayName("권한이 없는 게시글의 제목을 수정하려할 시 예외 발생")
    @Test
    void updateTitle_Exception_ForbiddenId() {
        Long forbiddenMemberId = 2L;

        assertThatThrownBy(() -> post.updateTitle("변경된 제목", forbiddenMemberId))
                .isInstanceOf(AuthenticationException.class);
    }

    @DisplayName("게시글 본문 수정")
    @Test
    void updateContent() {
        post.updateContent("변경된 본문", 1L);

        assertThat(post.getContent()).isEqualTo("변경된 본문");
    }

    @DisplayName("권한이 없는 게시글의 본문을 수정하려할 시 예외 발생")
    @Test
    void updateContent_Exception_ForbiddenId() {
        Long forbiddenMemberId = 2L;

        assertThatThrownBy(() -> post.updateContent("변경된 본문", forbiddenMemberId))
                .isInstanceOf(AuthenticationException.class);
    }

    @DisplayName("게시글의 회원 정보가 일치하는지 반환")
    @ParameterizedTest
    @CsvSource({"1, true", "2, false"})
    void isAuthenticated(Long accessMemberId, boolean expected) {
        boolean actual = post.isAuthenticated(accessMemberId);

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("신고가 5개 이상이면 isBlocked()가 true를 반환")
    @ParameterizedTest
    @CsvSource({"4, false", "5, true"})
    void isBlocked(int reportCount, boolean expected) {
        for (int i = 0; i < reportCount; ++i) {
            PostReport.builder()
                    .post(post)
                    .reporter(member)
                    .reportMessage("신고")
                    .build();
        }

        assertThat(post.isBlocked()).isEqualTo(expected);
    }
}
