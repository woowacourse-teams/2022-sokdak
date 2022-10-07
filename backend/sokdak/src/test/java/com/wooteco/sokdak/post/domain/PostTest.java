package com.wooteco.sokdak.post.domain;

import static com.wooteco.sokdak.util.fixture.MemberFixture.VALID_NICKNAME;
import static com.wooteco.sokdak.util.fixture.MemberFixture.VALID_PASSWORD;
import static com.wooteco.sokdak.util.fixture.MemberFixture.VALID_USERNAME;
import static com.wooteco.sokdak.util.fixture.MemberFixture.getMembersForReport;
import static com.wooteco.sokdak.util.fixture.PostFixture.VALID_POST_CONTENT;
import static com.wooteco.sokdak.util.fixture.PostFixture.VALID_POST_TITLE;
import static com.wooteco.sokdak.util.fixture.PostFixture.VALID_POST_WRITER_NICKNAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.wooteco.sokdak.auth.exception.AuthorizationException;
import com.wooteco.sokdak.member.domain.Member;
import com.wooteco.sokdak.report.domain.PostReport;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

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
                .title(VALID_POST_TITLE)
                .content(VALID_POST_CONTENT)
                .writerNickname(VALID_POST_WRITER_NICKNAME)
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
                .isInstanceOf(AuthorizationException.class);
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
                .isInstanceOf(AuthorizationException.class);
    }

    @DisplayName("신고가 5개 이상이면 isBlocked()가 true를 반환하고, 게시글의 정보는 반환되지 않는다.")
    @Test
    void isBlocked_true() {
        List<Member> members = getMembersForReport();
        int blockCondition = 5;
        for (int i = 0; i < blockCondition; ++i) {
            PostReport.builder()
                    .post(post)
                    .reporter(members.get(i))
                    .reportMessage("신고")
                    .build();
        }

        assertAll(
                () -> assertThat(post.isBlocked()).isTrue(),
                () -> assertThat(post.getNickname()).isEqualTo("블라인드 처리된 게시글입니다."),
                () -> assertThat(post.getTitle()).isEqualTo("블라인드 처리된 게시글입니다."),
                () -> assertThat(post.getContent()).isEqualTo("블라인드 처리된 게시글입니다.")
        );
    }

    @DisplayName("신고가 5개 미만이면 isBlocked()가 false를 반환하고 게시글 정보들이 반환된다.")
    @Test
    void isBlocked_false() {
        List<Member> members = getMembersForReport();
        int unblockCondition = 4;
        for (int i = 0; i < unblockCondition; ++i) {
            PostReport.builder()
                    .post(post)
                    .reporter(members.get(i))
                    .reportMessage("신고")
                    .build();
        }

        assertAll(
                () -> assertThat(post.isBlocked()).isFalse(),
                () -> assertThat(post.getNickname()).isEqualTo(VALID_POST_WRITER_NICKNAME),
                () -> assertThat(post.getTitle()).isEqualTo(VALID_POST_TITLE),
                () -> assertThat(post.getContent()).isEqualTo(VALID_POST_CONTENT)
        );
    }

    @DisplayName("게시글의 회원 정보가 일치하는지 반환")
    @ParameterizedTest
    @CsvSource({"1, true", "2, false"})
    void isAuthorized(Long accessMemberId, boolean expected) {
        boolean actual = post.isAuthorized(accessMemberId);

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("특정 멤버가 신고를 이미 했으면 true를, 안했으면 false를 반환한다.")
    @ParameterizedTest
    @MethodSource("hasReportByMemberArguments")
    void hasReportByMember(Member reporter, Member member, boolean expected) {
        PostReport postReport = PostReport.builder()
                .post(post)
                .reporter(reporter)
                .reportMessage("report")
                .build();
        post.addReport(postReport);

        assertThat(post.hasReportByMember(member)).isEqualTo(expected);
    }

    static Stream<Arguments> hasReportByMemberArguments() {
        Member reporter = Member.builder()
                .username("reporter")
                .nickname("reporterNickname")
                .password("Abcd123!@")
                .build();
        Member member = Member.builder()
                .username("member")
                .nickname("memberNickname")
                .password("Abcd123!@")
                .build();
        return Stream.of(
                Arguments.of(reporter, reporter, true),
                Arguments.of(reporter, member, false)
        );
    }
}
