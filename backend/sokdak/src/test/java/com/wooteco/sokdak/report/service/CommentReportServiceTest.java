package com.wooteco.sokdak.report.service;

import static com.wooteco.sokdak.member.domain.RoleType.USER;
import static com.wooteco.sokdak.util.fixture.BoardFixture.APPLICANT_BOARD_ID;
import static com.wooteco.sokdak.util.fixture.BoardFixture.FREE_BOARD_ID;
import static com.wooteco.sokdak.util.fixture.CommentFixture.VALID_COMMENT_MESSAGE;
import static com.wooteco.sokdak.util.fixture.MemberFixture.VALID_NICKNAME_TEXT;
import static com.wooteco.sokdak.util.fixture.PostFixture.VALID_POST_CONTENT;
import static com.wooteco.sokdak.util.fixture.PostFixture.VALID_POST_TITLE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.wooteco.sokdak.auth.dto.AuthInfo;
import com.wooteco.sokdak.auth.exception.AuthorizationException;
import com.wooteco.sokdak.board.domain.Board;
import com.wooteco.sokdak.board.domain.PostBoard;
import com.wooteco.sokdak.board.repository.BoardRepository;
import com.wooteco.sokdak.board.repository.PostBoardRepository;
import com.wooteco.sokdak.comment.domain.Comment;
import com.wooteco.sokdak.comment.exception.CommentNotFoundException;
import com.wooteco.sokdak.comment.repository.CommentRepository;
import com.wooteco.sokdak.post.domain.Post;
import com.wooteco.sokdak.post.exception.PostNotFoundException;
import com.wooteco.sokdak.post.repository.PostRepository;
import com.wooteco.sokdak.report.domain.CommentReportEvent;
import com.wooteco.sokdak.report.dto.ReportRequest;
import com.wooteco.sokdak.report.exception.AlreadyReportCommentException;
import com.wooteco.sokdak.report.exception.InvalidReportMessageException;
import com.wooteco.sokdak.util.ServiceTest;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

class CommentReportServiceTest extends ServiceTest {

    private static final ReportRequest REPORT_REQUEST = new ReportRequest("나쁜댓글");

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentReportService commentReportService;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private PostBoardRepository postBoardRepository;

    private Comment comment;
    private Comment applicantComment;

    @BeforeEach
    void setUp() {
        Board freeBoard = boardRepository.findById(FREE_BOARD_ID).get();
        Board applicantBoard = boardRepository.findById(APPLICANT_BOARD_ID).get();

        Post post = Post.builder()
                .member(member)
                .title(VALID_POST_TITLE)
                .content(VALID_POST_CONTENT)
                .postHashtags(Collections.emptyList())
                .comments(Collections.emptyList())
                .postLikes(Collections.emptyList())
                .build();
        postRepository.save(post);
        PostBoard postBoard = PostBoard.builder().build();
        postBoard.addPost(post);
        postBoard.addBoard(freeBoard);
        postBoardRepository.save(postBoard);

        Post applicantPost = Post.builder()
                .member(member)
                .title(VALID_POST_TITLE)
                .content(VALID_POST_CONTENT)
                .postHashtags(Collections.emptyList())
                .comments(Collections.emptyList())
                .postLikes(Collections.emptyList())
                .build();
        postRepository.save(applicantPost);
        PostBoard applicantPostBoard = PostBoard.builder().build();
        applicantPostBoard.addBoard(applicantBoard);
        applicantPostBoard.addPost(applicantPost);
        postBoardRepository.save(applicantPostBoard);

        comment = Comment.builder()
                .member(member)
                .post(post)
                .nickname(VALID_NICKNAME_TEXT)
                .message(VALID_COMMENT_MESSAGE)
                .build();
        commentRepository.save(comment);

        applicantComment = Comment.builder()
                .member(member)
                .post(applicantPost)
                .nickname(VALID_NICKNAME_TEXT)
                .message(VALID_COMMENT_MESSAGE)
                .build();
        commentRepository.save(applicantComment);
    }

    @DisplayName("댓글 신고 기능")
    @Test
    void reportComment() {
        int commentCountBeforeReport = comment.getCommentReports().size();

        commentReportService.reportComment(comment.getId(), REPORT_REQUEST, AUTH_INFO);
        int commentCountAfterReport = comment.getCommentReports().size();

        assertThat(commentCountBeforeReport + 1).isEqualTo(commentCountAfterReport);
    }

    @DisplayName("이미 신고한 댓글을 신고 시 예외 발생")
    @Test
    void reportComment_Exception_Already_Report() {
        commentReportService.reportComment(comment.getId(), REPORT_REQUEST, AUTH_INFO);

        assertThatThrownBy(
                () -> commentReportService.reportComment(comment.getId(), REPORT_REQUEST, AUTH_INFO))
                .isInstanceOf(AlreadyReportCommentException.class);
    }

    @DisplayName("신고내용 없이 신고하면 예외발생")
    @Test
    void reportComment_Exception_No_Content() {
        ReportRequest reportRequest = new ReportRequest("  ");

        assertThatThrownBy(
                () -> commentReportService.reportComment(comment.getId(), reportRequest,
                        new AuthInfo(member.getId(), USER.getName(), "nickname")))
                .isInstanceOf(InvalidReportMessageException.class);
    }

    @DisplayName("댓글 신고가 5회 신고시 댓글의 내용이 반환되지 않는다.")
    @Test
    void reportComment_Block() {
        AuthInfo authInfo;
        for (long i = 1; i <= 5; i++) {
            authInfo = new AuthInfo(i, "USER", VALID_NICKNAME_TEXT);
            commentReportService.reportComment(comment.getId(), REPORT_REQUEST, authInfo);
        }
        String blindCommentMessage = "블라인드 처리된 댓글입니다.";

        Comment found = commentRepository.findById(this.comment.getId())
                .orElseThrow(CommentNotFoundException::new);

        assertAll(
                () -> assertThat(found.isBlocked()).isTrue(),
                () -> assertThat(found.getNickname()).isEqualTo(blindCommentMessage),
                () -> assertThat(found.getMessage()).isEqualTo(blindCommentMessage)
        );
    }

    @DisplayName("댓글 5회 신고시 알림 등록")
    @ParameterizedTest
    @CsvSource({"4, 0", "5, 1"})
    void reportComment_BlockNotification(int reportCount, long expected) {
        AuthInfo authInfo;
        for (long i = 1; i <= reportCount; i++) {
            authInfo = new AuthInfo(i, "USER", VALID_NICKNAME_TEXT);
            commentReportService.reportComment(comment.getId(), REPORT_REQUEST, authInfo);
        }

        long commentReportEventCount = applicationEvents.stream(CommentReportEvent.class).count();

        assertThat(commentReportEventCount).isEqualTo(expected);
    }

    @DisplayName("지원자는 권한이 없는 게시판 게시글의 댓글, 대댓글을 신고할 수 없다.")
    @ParameterizedTest
    @CsvSource({"2", "3", "4"})
    void flipPostLike_Applicant_Exception(Long boardId) {
        Post post = Post.builder()
                .member(member)
                .title(VALID_POST_TITLE)
                .content(VALID_POST_CONTENT)
                .writerNickname("randomNickname")
                .build();
        postRepository.save(post);
        PostBoard postBoard = PostBoard.builder().build();
        postBoard.addBoard(boardRepository.findById(boardId).get());
        postBoard.addPost(post);
        postBoardRepository.save(postBoard);
        comment = Comment.parent(member, post, "nickname", "댓글내용");
        commentRepository.save(comment);

        ReportRequest reportRequest = new ReportRequest("message");

        assertThatThrownBy(() -> commentReportService.reportComment(comment.getId(), reportRequest, APPLICANT_AUTH_INFO))
                .isInstanceOf(AuthorizationException.class);
    }

    @DisplayName("지원자는 권한이 있는 게시판 게시글의 댓글 대댓글을 신고할 수 있다.")
    @Test
    void flipPostLike_Applicant() {
        ReportRequest reportRequest = new ReportRequest("message");
        int reportCountBeforeReport = applicantComment.getCommentReports().size();

        commentReportService.reportComment(applicantComment.getId(), reportRequest, APPLICANT_AUTH_INFO);

        Post post = postRepository.findById(this.applicantComment.getId())
                .orElseThrow(PostNotFoundException::new);

        int reportCountAfterReport = applicantComment.getCommentReports().size();

        assertThat(reportCountBeforeReport + 1).isEqualTo(reportCountAfterReport);
    }
}
