package com.wooteco.sokdak.report.service;

import static com.wooteco.sokdak.member.domain.RoleType.USER;
import static com.wooteco.sokdak.util.fixture.BoardFixture.APPLICANT_BOARD_ID;
import static com.wooteco.sokdak.util.fixture.BoardFixture.FREE_BOARD_ID;
import static com.wooteco.sokdak.util.fixture.MemberFixture.VALID_NICKNAME_TEXT;
import static com.wooteco.sokdak.util.fixture.PostFixture.VALID_POST_CONTENT;
import static com.wooteco.sokdak.util.fixture.PostFixture.VALID_POST_TITLE;
import static com.wooteco.sokdak.util.fixture.PostFixture.VALID_POST_WRITER_NICKNAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.wooteco.sokdak.auth.dto.AuthInfo;
import com.wooteco.sokdak.auth.exception.AuthorizationException;
import com.wooteco.sokdak.board.domain.Board;
import com.wooteco.sokdak.board.domain.PostBoard;
import com.wooteco.sokdak.board.repository.BoardRepository;
import com.wooteco.sokdak.board.repository.PostBoardRepository;
import com.wooteco.sokdak.post.domain.Post;
import com.wooteco.sokdak.post.exception.PostNotFoundException;
import com.wooteco.sokdak.post.repository.PostRepository;
import com.wooteco.sokdak.report.dto.ReportRequest;
import com.wooteco.sokdak.report.event.PostReportEvent;
import com.wooteco.sokdak.report.exception.AlreadyReportPostException;
import com.wooteco.sokdak.report.exception.InvalidReportMessageException;
import com.wooteco.sokdak.util.ServiceTest;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

class PostReportServiceTest extends ServiceTest {

    private static final ReportRequest REPORT_REQUEST = new ReportRequest("나쁜글");

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostReportService postReportService;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private PostBoardRepository postBoardRepository;

    private Post post;
    private Post applicantPost;

    @BeforeEach
    void setUp() {
        Board freeBoard = boardRepository.findById(FREE_BOARD_ID).get();
        Board applicantBoard = boardRepository.findById(APPLICANT_BOARD_ID).get();

        post = Post.builder()
                .member(member)
                .title(VALID_POST_TITLE)
                .content(VALID_POST_CONTENT)
                .writerNickname(VALID_POST_WRITER_NICKNAME)
                .postHashtags(Collections.emptyList())
                .comments(Collections.emptyList())
                .postLikes(Collections.emptyList())
                .build();
        postRepository.save(post);
        PostBoard postBoard = PostBoard.builder().build();
        postBoard.addPost(post);
        postBoard.addBoard(freeBoard);
        postBoardRepository.save(postBoard);

        applicantPost = Post.builder()
                .member(member)
                .title(VALID_POST_TITLE)
                .content(VALID_POST_CONTENT)
                .writerNickname(VALID_POST_WRITER_NICKNAME)
                .postHashtags(Collections.emptyList())
                .comments(Collections.emptyList())
                .postLikes(Collections.emptyList())
                .build();
        postRepository.save(applicantPost);
        PostBoard applicantPostBoard = PostBoard.builder().build();
        applicantPostBoard.addBoard(applicantBoard);
        applicantPostBoard.addPost(applicantPost);
        postBoardRepository.save(applicantPostBoard);
    }

    @DisplayName("글 신고 기능")
    @Test
    void reportPost() {
        int reportCountBeforeReport = post.getPostReports().size();

        postReportService
                .reportPost(post.getId(), REPORT_REQUEST, new AuthInfo(member.getId(), USER.getName(), "nickname"));
        int reportCountAfterReport = post.getPostReports().size();

        assertThat(reportCountBeforeReport + 1).isEqualTo(reportCountAfterReport);
    }

    @DisplayName("이미 신고한 게시글을 신고 시 예외발생")
    @Test
    void reportPost_Exception_Already_Report() {
        postReportService
                .reportPost(post.getId(), REPORT_REQUEST, new AuthInfo(member.getId(), USER.getName(), "nickname"));

        assertThatThrownBy(
                () -> postReportService.reportPost(post.getId(), REPORT_REQUEST,
                        new AuthInfo(member.getId(), USER.getName(), "nickname")))
                .isInstanceOf(AlreadyReportPostException.class);
    }

    @DisplayName("신고내용 없이 신고하면 예외발생")
    @Test
    void reportPost_Exception_No_Content() {
        ReportRequest invalidReportRequest = new ReportRequest("  ");

        assertThatThrownBy(
                () -> postReportService.reportPost(post.getId(), invalidReportRequest,
                        new AuthInfo(member.getId(), USER.getName(), "nickname")))
                .isInstanceOf(InvalidReportMessageException.class);
    }

    @DisplayName("게시글 5회 신고시 알림 등록")
    @ParameterizedTest
    @CsvSource({"4, 0", "5, 1"})
    void reportPost_blockNotification(int reportCount, long expected) {
        AuthInfo authInfo;
        for (long i = 1; i <= reportCount; i++) {
            authInfo = new AuthInfo(i, "USER", VALID_NICKNAME_TEXT);
            postReportService.reportPost(post.getId(), REPORT_REQUEST, authInfo);
        }

        long postReportEventCount = applicationEvents.stream(PostReportEvent.class).count();

        assertThat(postReportEventCount).isEqualTo(expected);
    }

    @DisplayName("게시글 5회 신고시 게시글의 내용이 반환되지 않는다.")
    @Test
    void reportPost_block() {
        int blockCondition = 5;
        AuthInfo authInfo;
        for (long i = 1; i <= blockCondition; i++) {
            authInfo = new AuthInfo(i, "USER", VALID_NICKNAME_TEXT);
            postReportService.reportPost(post.getId(), REPORT_REQUEST, authInfo);
        }
        String blindPostMessage = "블라인드 처리된 게시글입니다.";

        Post post = postRepository.findById(this.post.getId())
                .orElseThrow(PostNotFoundException::new);

        assertAll(
                () -> assertThat(post.isBlocked()).isTrue(),
                () -> assertThat(post.getNickname()).isEqualTo(blindPostMessage),
                () -> assertThat(post.getTitle()).isEqualTo(blindPostMessage),
                () -> assertThat(post.getContent()).isEqualTo(blindPostMessage)
        );
    }

    @DisplayName("지원자는 권한이 없는 게시판 게시글을 신고할 수 없다.")
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

        ReportRequest reportRequest = new ReportRequest("message");

        assertThatThrownBy(() -> postReportService.reportPost(post.getId(), reportRequest, APPLICANT_AUTH_INFO))
                .isInstanceOf(AuthorizationException.class);
    }

    @DisplayName("지원자는 권한이 있는 게시판 게시글을 신고할 수 있다.")
    @Test
    void flipPostLike_Applicant() {
        ReportRequest reportRequest = new ReportRequest("message");
        int reportCountBeforeReport = applicantPost.getPostReports().size();

        postReportService.reportPost(applicantPost.getId(), reportRequest, APPLICANT_AUTH_INFO);

        int reportCountAfterReport = applicantPost.getPostReports().size();

        assertThat(reportCountBeforeReport + 1).isEqualTo(reportCountAfterReport);
    }
}
