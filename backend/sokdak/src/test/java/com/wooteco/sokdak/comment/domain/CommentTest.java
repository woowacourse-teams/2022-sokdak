package com.wooteco.sokdak.comment.domain;

import static com.wooteco.sokdak.util.fixture.MemberFixture.VALID_NICKNAME;
import static com.wooteco.sokdak.util.fixture.MemberFixture.VALID_PASSWORD;
import static com.wooteco.sokdak.util.fixture.MemberFixture.VALID_USERNAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.wooteco.sokdak.auth.exception.AuthenticationException;
import com.wooteco.sokdak.member.domain.Member;
import com.wooteco.sokdak.post.domain.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class CommentTest {

    private Comment comment;

    @BeforeEach
    void setUp() {
        Member member = Member.builder()
                .id(1L)
                .username(VALID_USERNAME)
                .password(VALID_PASSWORD)
                .nickname(VALID_NICKNAME)
                .build();
        Post post = Post.builder()
                .title("제목")
                .content("본문")
                .member(member)
                .build();
        comment = Comment.builder()
                .member(member)
                .post(post)
                .nickname(VALID_NICKNAME)
                .message("댓글")
                .build();
    }

    @DisplayName("댓글을 작성한 작성자가 아니면 예외발생")
    @Test
    void validateOwner() {
        Long invalidOwnerId = 2L;

        assertThatThrownBy(() -> comment.validateOwner(invalidOwnerId))
                .isInstanceOf(AuthenticationException.class);
    }

    @DisplayName("내가 작성한 댓글이면 true를 반환")
    @ParameterizedTest
    @CsvSource({"1, true", "2, false"})
    void isAuthenticated(Long userId, boolean expected) {
        assertThat(comment.isAuthenticated(userId)).isEqualTo(expected);
    }
}
