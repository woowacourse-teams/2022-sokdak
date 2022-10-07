package com.wooteco.sokdak.report.domain;

import static com.wooteco.sokdak.util.fixture.MemberFixture.VALID_NICKNAME;
import static com.wooteco.sokdak.util.fixture.MemberFixture.VALID_PASSWORD;
import static com.wooteco.sokdak.util.fixture.MemberFixture.VALID_USERNAME;
import static com.wooteco.sokdak.util.fixture.PostFixture.VALID_POST_CONTENT;
import static com.wooteco.sokdak.util.fixture.PostFixture.VALID_POST_TITLE;
import static org.assertj.core.api.Assertions.assertThat;

import com.wooteco.sokdak.member.domain.Member;
import com.wooteco.sokdak.post.domain.Post;
import java.util.Collections;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;


class PostReportTest {

    private Member member;
    private Post post;

    @BeforeEach
    void setUp() {
        member = Member.builder()
                .username(VALID_USERNAME)
                .password(VALID_PASSWORD)
                .nickname(VALID_NICKNAME)
                .build();
        post = Post.builder()
                .member(member)
                .title(VALID_POST_TITLE)
                .content(VALID_POST_CONTENT)
                .postHashtags(Collections.emptyList())
                .build();
    }

    @DisplayName("연관관계 편의 메서드")
    @Test
    void constructor() {
        PostReport postReport = PostReport.builder()
                .post(post)
                .reporter(member)
                .reportMessage("신고")
                .build();

        assertThat(post.getPostReports()).contains(postReport);
    }

    @DisplayName("신고자가 맞으면 true, 아니면 false를 반환한다.")
    @ParameterizedTest
    @MethodSource("isOwnerArguments")
    void isOwner(Member reporter, Member member, boolean expected) {
        PostReport postReport = PostReport.builder()
                .post(post)
                .reporter(reporter)
                .reportMessage("message")
                .build();

        assertThat(postReport.isOwner(member)).isEqualTo(expected);
    }

    static Stream<Arguments> isOwnerArguments() {
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
