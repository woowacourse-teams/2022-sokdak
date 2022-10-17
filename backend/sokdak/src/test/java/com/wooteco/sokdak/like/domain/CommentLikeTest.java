package com.wooteco.sokdak.like.domain;

import static com.wooteco.sokdak.util.fixture.MemberFixture.VALID_NICKNAME;
import static com.wooteco.sokdak.util.fixture.MemberFixture.VALID_PASSWORD;
import static com.wooteco.sokdak.util.fixture.MemberFixture.VALID_USERNAME;
import static org.assertj.core.api.Assertions.assertThat;

import com.wooteco.sokdak.comment.domain.Comment;
import com.wooteco.sokdak.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class CommentLikeTest {

    @DisplayName("특정 회원이 누른 댓글 좋아요인지 반환한다.")
    @ParameterizedTest
    @CsvSource({"1, true", "2, false"})
    void isLikeOf(Long memberId, boolean expected) {
        Comment comment = Comment.builder()
                .nickname(VALID_NICKNAME)
                .message("댓글")
                .build();
        Member member = Member.builder()
                .id(1L)
                .username(VALID_USERNAME)
                .password(VALID_PASSWORD)
                .nickname(VALID_NICKNAME)
                .build();
        CommentLike commentLike = CommentLike.builder()
                .member(member)
                .comment(comment)
                .build();

        boolean actual = commentLike.isLikeOf(memberId);

        assertThat(actual).isEqualTo(expected);
    }
}
