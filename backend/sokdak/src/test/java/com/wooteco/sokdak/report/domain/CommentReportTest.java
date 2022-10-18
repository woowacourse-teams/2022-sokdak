package com.wooteco.sokdak.report.domain;

import static com.wooteco.sokdak.util.fixture.CommentFixture.VALID_COMMENT_MESSAGE;
import static com.wooteco.sokdak.util.fixture.MemberFixture.ENCRYPTOR;
import static com.wooteco.sokdak.util.fixture.MemberFixture.VALID_NICKNAME;
import static com.wooteco.sokdak.util.fixture.MemberFixture.VALID_PASSWORD;
import static com.wooteco.sokdak.util.fixture.MemberFixture.VALID_USERNAME;
import static com.wooteco.sokdak.util.fixture.PostFixture.VALID_POST_CONTENT;
import static com.wooteco.sokdak.util.fixture.PostFixture.VALID_POST_TITLE;
import static org.assertj.core.api.Assertions.assertThat;

import com.wooteco.sokdak.comment.domain.Comment;
import com.wooteco.sokdak.member.domain.Member;
import com.wooteco.sokdak.member.domain.Nickname;
import com.wooteco.sokdak.member.domain.Password;
import com.wooteco.sokdak.member.domain.Username;
import com.wooteco.sokdak.post.domain.Post;
import java.util.Collections;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class CommentReportTest {

    private Member member;
    private Post post;
    private Comment comment;

    @BeforeEach
    void setUp() {
        member = Member.builder()
                .username(Username.of(ENCRYPTOR,VALID_USERNAME))
                .password(Password.of(ENCRYPTOR, VALID_PASSWORD))
                .nickname(new Nickname(VALID_NICKNAME))
                .build();
        post = Post.builder()
                .member(member)
                .title(VALID_POST_TITLE)
                .content(VALID_POST_CONTENT)
                .postHashtags(Collections.emptyList())
                .build();
        comment = Comment.builder()
                .member(member)
                .post(post)
                .nickname(VALID_NICKNAME)
                .message(VALID_COMMENT_MESSAGE)
                .build();
    }

    @DisplayName("연관관계 편의 메서드")
    @Test
    void constructor() {
        CommentReport commentReport = CommentReport.builder()
                .comment(comment)
                .reporter(member)
                .reportMessage("report")
                .build();

        assertThat(comment.getCommentReports()).contains(commentReport);
    }

    @DisplayName("신고자가 맞으면 true, 아니면 false를 반환한다.")
    @ParameterizedTest
    @MethodSource("isOwnerArguments")
    void isOwner(Member reporter, Member member, boolean expected) {
        CommentReport commentReport = CommentReport.builder()
                .comment(comment)
                .reporter(reporter)
                .reportMessage("message")
                .build();

        assertThat(commentReport.isOwner(member)).isEqualTo(expected);
    }

    static Stream<Arguments> isOwnerArguments() {
        Member reporter = Member.builder()
                .username(Username.of(ENCRYPTOR,"reporter"))
                .nickname(new Nickname("reporterNickname"))
                .password(Password.of(ENCRYPTOR, "Abcd123!@"))
                .build();
        Member member = Member.builder()
                .username(Username.of(ENCRYPTOR,"member"))
                .nickname(new Nickname("memberNickname"))
                .password(Password.of(ENCRYPTOR, "Abcd123!@"))
                .build();

        return Stream.of(
                Arguments.of(reporter, reporter, true),
                Arguments.of(reporter, member, false)
        );
    }
}
