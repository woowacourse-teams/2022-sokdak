package com.wooteco.sokdak.comment.domain;

import static com.wooteco.sokdak.util.fixture.MemberFixture.VALID_NICKNAME;
import static com.wooteco.sokdak.util.fixture.MemberFixture.VALID_PASSWORD;
import static com.wooteco.sokdak.util.fixture.MemberFixture.VALID_USERNAME;
import static com.wooteco.sokdak.util.fixture.MemberFixture.getMembersForReport;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.wooteco.sokdak.auth.exception.AuthorizationException;
import com.wooteco.sokdak.member.domain.Member;
import com.wooteco.sokdak.post.domain.Post;
import com.wooteco.sokdak.report.domain.CommentReport;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

class CommentTest {

    private Post post;
    private Comment comment;
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
                .isInstanceOf(AuthorizationException.class);
    }

    @DisplayName("신고가 5개 이상이면 isBlocked()가 true를 반환 그 이외는 False반환")
    @ParameterizedTest
    @CsvSource({"4, false", "5, true"})
    void isBlocked(int reportCount, boolean expected) {
        List<Member> members = getMembersForReport();
        for (int i = 0; i < reportCount; ++i) {
            CommentReport.builder()
                    .comment(comment)
                    .reporter(members.get(i))
                    .reportMessage("신고")
                    .build();
        }

        assertThat(comment.isBlocked()).isEqualTo(expected);
    }

    @DisplayName("내가 작성한 댓글이면 true를 반환")
    @ParameterizedTest
    @CsvSource({"1, true", "2, false"})
    void isAuthorized(Long userId, boolean expected) {
        assertThat(comment.isAuthorized(userId)).isEqualTo(expected);
    }

    @DisplayName("게시글 작성자의 댓글인지 반환")
    @ParameterizedTest
    @MethodSource("provideMemberAndExpected")
    void isPostWriter_True(Member member, boolean expected) {
        Comment comment = Comment.builder()
                .member(member)
                .post(post)
                .nickname(VALID_NICKNAME)
                .message("댓글")
                .build();

        boolean actual = comment.isPostWriter();

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("블락 처리된 Comment는 실제 내용을 반환하지 않는다.")
    @ParameterizedTest
    @CsvSource({"5, true", "4, false"})
    void getMessage_Blocked(int reportCount, boolean expected) {
        List<Member> members = getMembersForReport();
        for (int i = 0; i < reportCount; ++i) {
            CommentReport.builder()
                    .comment(comment)
                    .reporter(members.get(i))
                    .reportMessage("신고")
                    .build();
        }

        boolean nicknameActual = comment.getNickname().equals("블라인드 처리된 댓글입니다.");
        boolean messageActual = comment.getMessage().equals("블라인드 처리된 댓글입니다.");

        assertAll(
                () -> assertThat(nicknameActual).isEqualTo(expected),
                () -> assertThat(messageActual).isEqualTo(expected)
        );
    }

    private static Stream<Arguments> provideMemberAndExpected() {
        Member member1 = Member.builder()
                .id(1L)
                .username(VALID_USERNAME)
                .password(VALID_PASSWORD)
                .nickname(VALID_NICKNAME)
                .build();
        Member member2 = Member.builder()
                .id(2L)
                .username(VALID_USERNAME)
                .password(VALID_PASSWORD)
                .nickname(VALID_NICKNAME)
                .build();
        return Stream.of(
                Arguments.of(member1, true),
                Arguments.of(member2, false)
        );
    }
}
