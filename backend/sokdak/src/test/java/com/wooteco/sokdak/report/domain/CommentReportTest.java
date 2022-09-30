package com.wooteco.sokdak.report.domain;

import static com.wooteco.sokdak.util.fixture.CommentFixture.VALID_COMMENT_MESSAGE;
import static com.wooteco.sokdak.util.fixture.MemberFixture.VALID_NICKNAME;
import static com.wooteco.sokdak.util.fixture.MemberFixture.VALID_PASSWORD;
import static com.wooteco.sokdak.util.fixture.MemberFixture.VALID_USERNAME;
import static com.wooteco.sokdak.util.fixture.PostFixture.VALID_POST_CONTENT;
import static com.wooteco.sokdak.util.fixture.PostFixture.VALID_POST_TITLE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.wooteco.sokdak.comment.domain.Comment;
import com.wooteco.sokdak.member.domain.Member;
import com.wooteco.sokdak.post.domain.Post;
import com.wooteco.sokdak.report.exception.AlreadyReportCommentException;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CommentReportTest {

    private Member member;
    private Post post;
    private Comment comment;

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
}
