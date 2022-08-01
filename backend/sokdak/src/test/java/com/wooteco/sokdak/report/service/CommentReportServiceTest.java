package com.wooteco.sokdak.report.service;

import static com.wooteco.sokdak.member.domain.RoleType.USER;
import static com.wooteco.sokdak.post.util.CommentFixture.VALID_COMMENT_MESSAGE;
import static com.wooteco.sokdak.post.util.PostFixture.VALID_POST_CONTENT;
import static com.wooteco.sokdak.post.util.PostFixture.VALID_POST_TITLE;
import static com.wooteco.sokdak.util.fixture.MemberFixture.VALID_NICKNAME;
import static com.wooteco.sokdak.util.fixture.MemberFixture.VALID_PASSWORD;
import static com.wooteco.sokdak.util.fixture.MemberFixture.VALID_USERNAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

import com.wooteco.sokdak.auth.dto.AuthInfo;
import com.wooteco.sokdak.comment.domain.Comment;
import com.wooteco.sokdak.comment.repository.CommentRepository;
import com.wooteco.sokdak.member.domain.Member;
import com.wooteco.sokdak.member.domain.RoleType;
import com.wooteco.sokdak.member.repository.MemberRepository;
import com.wooteco.sokdak.post.domain.Post;
import com.wooteco.sokdak.post.repository.PostRepository;
import com.wooteco.sokdak.report.dto.ReportRequest;
import com.wooteco.sokdak.report.exception.AlreadyReportCommentException;
import com.wooteco.sokdak.report.exception.InvalidReportMessageException;
import com.wooteco.sokdak.report.repository.CommentReportRepository;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Sql(
        scripts = {"classpath:truncate.sql"},
        executionPhase = BEFORE_TEST_METHOD)
@Transactional
class CommentReportServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentReportRepository commentReportRepository;

    @Autowired
    private CommentReportService commentReportService;

    private Member member;
    private Post post;
    private Comment comment;

    @BeforeEach
    void setUp() {
        member = Member.builder()
                .username(VALID_USERNAME)
                .nickname(VALID_NICKNAME)
                .password(VALID_PASSWORD)
                .build();
        memberRepository.save(member);

        post = Post.builder()
                .member(member)
                .title(VALID_POST_TITLE)
                .content(VALID_POST_CONTENT)
                .postHashtags(Collections.emptyList())
                .comments(Collections.emptyList())
                .likes(Collections.emptyList())
                .build();
        postRepository.save(post);

        comment = Comment.builder()
                .member(member)
                .post(post)
                .nickname(VALID_NICKNAME)
                .message(VALID_COMMENT_MESSAGE)
                .build();
        commentRepository.save(comment);
    }

    @DisplayName("댓글 신고 기능")
    @Test
    void reportComment() {
        ReportRequest reportRequest = new ReportRequest("나쁜댓글");
        int commentCountBeforeReport = commentReportRepository.countByCommentId(comment.getId());

        commentReportService.reportComment(comment.getId(), reportRequest, new AuthInfo(member.getId(), USER.getName(), "nickname"));
        int commentCountAfterReport = commentReportRepository.countByCommentId(comment.getId());

        assertThat(commentCountBeforeReport + 1).isEqualTo(commentCountAfterReport);
    }

    @DisplayName("이미 신고한 댓글을 신고 시 예외 발생")
    @Test
    void reportComment_Exception_Already_Report() {
        ReportRequest reportRequest = new ReportRequest("나쁜댓글");
        commentReportService.reportComment(comment.getId(), reportRequest, new AuthInfo(member.getId(), USER.getName(), "nickname"));

        assertThatThrownBy(
                () -> commentReportService.reportComment(comment.getId(), reportRequest, new AuthInfo(member.getId(), USER.getName(), "nickname")))
                .isInstanceOf(AlreadyReportCommentException.class);
    }

    @DisplayName("신고내용 없이 신고하면 예외발생")
    @Test
    void reportComment_Exception_No_Content() {
        ReportRequest reportRequest = new ReportRequest("  ");

        assertThatThrownBy(
                () -> commentReportService.reportComment(comment.getId(), reportRequest, new AuthInfo(member.getId(), USER.getName(), "nickname")))
                .isInstanceOf(InvalidReportMessageException.class);
    }
}
