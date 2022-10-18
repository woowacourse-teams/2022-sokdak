package com.wooteco.sokdak.report.service;

import static com.wooteco.sokdak.member.domain.RoleType.USER;
import static com.wooteco.sokdak.util.fixture.BoardFixture.APPLICANT_BOARD_ID;
import static com.wooteco.sokdak.util.fixture.BoardFixture.FREE_BOARD_ID;
import static com.wooteco.sokdak.util.fixture.BoardFixture.GOOD_CREW_BOARD_ID;
import static com.wooteco.sokdak.util.fixture.BoardFixture.HOT_BOARD_ID;
import static com.wooteco.sokdak.util.fixture.BoardFixture.POSUTA_BOARD_ID;
import static com.wooteco.sokdak.util.fixture.MemberFixture.VALID_NICKNAME;
import static com.wooteco.sokdak.util.fixture.PostFixture.VALID_POST_CONTENT;
import static com.wooteco.sokdak.util.fixture.PostFixture.VALID_POST_TITLE;
import static com.wooteco.sokdak.util.fixture.PostFixture.VALID_POST_WRITER_NICKNAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.wooteco.sokdak.auth.dto.AuthInfo;
import com.wooteco.sokdak.auth.exception.AuthorizationException;
import com.wooteco.sokdak.board.domain.Board;
import com.wooteco.sokdak.board.domain.PostBoard;
import com.wooteco.sokdak.board.repository.BoardRepository;
import com.wooteco.sokdak.board.repository.PostBoardRepository;
import com.wooteco.sokdak.notification.repository.NotificationRepository;
import com.wooteco.sokdak.post.domain.Post;
import com.wooteco.sokdak.post.exception.PostNotFoundException;
import com.wooteco.sokdak.post.repository.PostRepository;
import com.wooteco.sokdak.report.dto.ReportRequest;
import com.wooteco.sokdak.report.exception.AlreadyReportPostException;
import com.wooteco.sokdak.report.exception.InvalidReportMessageException;
import com.wooteco.sokdak.report.repository.PostReportRepository;
import com.wooteco.sokdak.util.ServiceTest;
import java.util.Collections;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

class PostReportServiceTest extends ServiceTest {

    private static final ReportRequest REPORT_REQUEST = new ReportRequest("나쁜글");

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostReportRepository postReportRepository;

    @Autowired
    private PostReportService postReportService;

    @Autowired
    private NotificationRepository notificationRepository;

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
    @CsvSource({"4, false", "5, true"})
    void reportPost_blockNotification(int reportCount, boolean expected) {
        AuthInfo authInfo;
        for (long i = 1; i <= reportCount; i++) {
            authInfo = new AuthInfo(i, "USER", VALID_NICKNAME);
            postReportService.reportPost(post.getId(), REPORT_REQUEST, authInfo);
        }

        boolean actual = notificationRepository.existsByMemberIdAndInquiredIsFalse(member.getId());

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("게시글 5회 신고시 게시글의 내용이 반환되지 않는다.")
    @Test
    void reportPost_block() {
        int blockCondition = 5;
        AuthInfo authInfo;
        for (long i = 1; i <= blockCondition; i++) {
            authInfo = new AuthInfo(i, "USER", VALID_NICKNAME);
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
    @MethodSource("mockPosts")
    void flipPostLike_Applicant_Exception(Post mockPost) {
        ReportRequest reportRequest = new ReportRequest("message");

        assertThatThrownBy(() -> postReportService.reportPost(mockPost.getId(), reportRequest, APPLICANT_AUTH_INFO))
                .isInstanceOf(AuthorizationException.class);
    }

    static Stream<Arguments> mockPosts() {
        Post hotBoardPost = mock(Post.class);
        Post freeBoardPost = mock(Post.class);
        Post posutaBoardPost = mock(Post.class);
        Post goodCrewBoardPost = mock(Post.class);

        when(hotBoardPost.getBoardId()).thenReturn(HOT_BOARD_ID);
        when(freeBoardPost.getBoardId()).thenReturn(FREE_BOARD_ID);
        when(posutaBoardPost.getBoardId()).thenReturn(POSUTA_BOARD_ID);
        when(goodCrewBoardPost.getBoardId()).thenReturn(GOOD_CREW_BOARD_ID);

        when(hotBoardPost.getId()).thenReturn(1L);
        when(freeBoardPost.getId()).thenReturn(1L);
        when(posutaBoardPost.getId()).thenReturn(1L);
        when(goodCrewBoardPost.getId()).thenReturn(1L);

        return Stream.of(
                Arguments.of(hotBoardPost),
                Arguments.of(freeBoardPost),
                Arguments.of(posutaBoardPost),
                Arguments.of(goodCrewBoardPost)
        );
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
