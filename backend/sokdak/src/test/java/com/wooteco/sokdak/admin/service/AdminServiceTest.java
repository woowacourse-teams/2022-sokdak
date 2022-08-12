package com.wooteco.sokdak.admin.service;

import static com.wooteco.sokdak.util.fixture.MemberFixture.AUTH_INFO;
import static com.wooteco.sokdak.util.fixture.MemberFixture.AUTH_INFO_ADMIN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.wooteco.sokdak.admin.dto.PostReportsResponse;
import com.wooteco.sokdak.admin.dto.TicketRequest;
import com.wooteco.sokdak.admin.dto.TicketsResponse;
import com.wooteco.sokdak.admin.exception.NoAdminException;
import com.wooteco.sokdak.advice.UnauthorizedException;
import com.wooteco.sokdak.auth.domain.Ticket;
import com.wooteco.sokdak.board.domain.Board;
import com.wooteco.sokdak.board.domain.PostBoard;
import com.wooteco.sokdak.board.exception.BoardNotFoundException;
import com.wooteco.sokdak.board.repository.BoardRepository;
import com.wooteco.sokdak.board.repository.PostBoardRepository;
import com.wooteco.sokdak.member.domain.Member;
import com.wooteco.sokdak.member.exception.MemberNotFoundException;
import com.wooteco.sokdak.member.exception.SerialNumberNotFoundException;
import com.wooteco.sokdak.member.repository.MemberRepository;
import com.wooteco.sokdak.member.repository.TicketRepository;
import com.wooteco.sokdak.post.domain.Post;
import com.wooteco.sokdak.post.repository.PostRepository;
import com.wooteco.sokdak.post.service.PostService;
import com.wooteco.sokdak.report.repository.PostReportRepository;
import com.wooteco.sokdak.util.IntegrationTest;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Transactional(propagation = Propagation.NEVER)
class AdminServiceTest extends IntegrationTest {

    private static final Long BLOCKED_COUNT = 5L;
    private static final long WRITABLE_BOARD_ID = 2L;
    private static final String SERIAL_NUMBER = "test1@gmail.com";
    @Autowired
    private AdminService adminService;
    @Autowired
    private PostService postService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PostReportRepository postReportRepository;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private PostBoardRepository postBoardRepository;
    @Autowired
    private TicketRepository ticketRepository;

    private Post post;
    private Board board;
    private Ticket ticket;

    @BeforeEach
    public void setUp() {
        Member member = memberRepository.findById(AUTH_INFO_ADMIN.getId())
                .orElseThrow(MemberNotFoundException::new);
        post = Post.builder()
                .title("제목")
                .content("본문")
                .writerNickname(member.getNickname())
                .member(member)
                .likes(new ArrayList<>())
                .comments(new ArrayList<>())
                .build();
        board = boardRepository.findById(WRITABLE_BOARD_ID)
                .orElseThrow(BoardNotFoundException::new);
        ticket = Ticket.builder()
                .serialNumber(SERIAL_NUMBER)
                .used(false)
                .build();
    }

    @DisplayName("관리자 권한으로 게시글 삭제 기능")
    @Test
    void deletePost() {
        Long postId = postRepository.save(post).getId();

        adminService.deletePost(postId, AUTH_INFO_ADMIN);

        assertThat(postRepository.findById(postId)).isEmpty();
    }

    @DisplayName("관리자 권한이 아닐 시 게시글 삭제 예외 발생")
    @Test
    void deletePost_Exception_NoAdmin() {
        Long postId = postRepository.save(post).getId();

        assertThatThrownBy(() -> adminService.deletePost(postId, AUTH_INFO))
                .isInstanceOf(UnauthorizedException.class);

        assertThat(postRepository.findById(postId)).isNotEmpty();
    }

    @DisplayName("관리자 권한으로 블라인드")
    @Test
    void blockPost() {
        Post post = savePost();

        adminService.blockPost(post.getId(), BLOCKED_COUNT, AUTH_INFO_ADMIN);

        assertThat(postReportRepository.findAllByPostId(post.getId())).hasSize(Math.toIntExact(BLOCKED_COUNT));
        assertThat(postService.findPost(post.getId(), AUTH_INFO_ADMIN).isBlocked()).isTrue();
    }

    @DisplayName("관리자 권한이 아닐 시 블라인드 불가")
    @Test
    void blockPost_Exception_NoAdmin() {
        Post post = savePost();

        assertThatThrownBy(() -> adminService.blockPost(post.getId(), BLOCKED_COUNT, AUTH_INFO))
                .isInstanceOf(UnauthorizedException.class);

        assertThat(postReportRepository.findAllByPostId(post.getId())).isEmpty();
        assertThat(postService.findPost(post.getId(), AUTH_INFO_ADMIN).isBlocked()).isFalse();
    }

    @DisplayName("관리자 권한으로 블라인드 해제")
    @Test
    void unBlockPost() {
        Post post = savePost();
        adminService.blockPost(post.getId(), BLOCKED_COUNT, AUTH_INFO_ADMIN);
        boolean blocked = postService.findPost(post.getId(), AUTH_INFO_ADMIN).isBlocked();
        assertThat(blocked).isTrue();

        adminService.unBlockPost(post.getId(), AUTH_INFO_ADMIN);

        assertThat(postReportRepository.findAllByPostId(post.getId())).isEmpty();
        assertThat(postService.findPost(post.getId(), AUTH_INFO_ADMIN).isBlocked()).isFalse();
    }

    @DisplayName("관리자 권한이 아닐 시 블라인드 해제되지 않는다.")
    @Test
    void unBlockPost_Exception_NoAdmin() {
        Post post = savePost();
        adminService.blockPost(post.getId(), BLOCKED_COUNT, AUTH_INFO_ADMIN);

        assertThatThrownBy(() -> adminService.unBlockPost(post.getId(), AUTH_INFO))
                .isInstanceOf(UnauthorizedException.class);

        assertThat(postReportRepository.findAllByPostId(post.getId())).hasSize(Math.toIntExact(BLOCKED_COUNT));
        assertThat(postService.findPost(post.getId(), AUTH_INFO_ADMIN).isBlocked()).isTrue();
    }

    @DisplayName("관리자 권한으로 모든 게시글 신고 조회")
    @Test
    void findAllPostReport() {
        Post post = savePost();
        adminService.blockPost(post.getId(), BLOCKED_COUNT, AUTH_INFO_ADMIN);

        PostReportsResponse allPostReport = adminService.findAllPostReport(AUTH_INFO_ADMIN);
        assertThat(allPostReport.getPostReports()).hasSize(Math.toIntExact(BLOCKED_COUNT));
    }

    @DisplayName("관리자 권한이 아닐 시 모든 게시글 신고를 조회할 수 없다.")
    @Test
    void findAllPostReport_Exception_NoAdmin() {
        assertThatThrownBy(() -> adminService.findAllPostReport(AUTH_INFO))
                .isInstanceOf(NoAdminException.class);
    }

    @DisplayName("관리자 권한으로 모든 티켓 조회")
    @Test
    void findAllTickets() {
        ticketRepository.deleteAll();
        Ticket ticket1 = Ticket.builder()
                .serialNumber(SERIAL_NUMBER)
                .used(false)
                .build();
        Ticket ticket2 = Ticket.builder()
                .serialNumber("test2@gmail.com")
                .used(false)
                .build();
        ticketRepository.save(ticket1);
        ticketRepository.save(ticket2);

        TicketsResponse tickets = adminService.findAllTickets(AUTH_INFO_ADMIN);
        assertThat(tickets.getTickets()).usingRecursiveComparison()
                .comparingOnlyFields("serialNumber", "used")
                .isEqualTo(List.of(ticket1, ticket2));
    }

    @DisplayName("관리자 권한이 아닐 시 티켓을 조회할 수 없다.")
    @Test
    void findAllTickets_Exception_NoAdmin() {
        assertThatThrownBy(() -> adminService.findAllTickets(AUTH_INFO))
                .isInstanceOf(NoAdminException.class);
    }

    @DisplayName("관리자 권한으로 티켓 추가")
    @Test
    void saveTicket() {
        adminService.saveTicket(AUTH_INFO_ADMIN, TicketRequest.of(ticket));

        Ticket actual = ticketRepository.findBySerialNumber(SERIAL_NUMBER).orElseThrow();
        assertThat(actual).usingRecursiveComparison()
                .comparingOnlyFields("serialNumber", "used")
                .isEqualTo(ticket);
    }

    @DisplayName("관리자 권한이 아닐 시 티켓을 추가할 수 없다.")
    @Test
    void saveTicket_Exception_NoAdmin() {
        assertThatThrownBy(() -> adminService.saveTicket(AUTH_INFO, TicketRequest.of(ticket)))
                .isInstanceOf(NoAdminException.class);
        assertThat(ticketRepository.findBySerialNumber(SERIAL_NUMBER)).isEmpty();
    }

    @DisplayName("관리자 권한으로 티켓 상태 변경")
    @Test
    void changeTicketUsedState() {
        adminService.saveTicket(AUTH_INFO_ADMIN, TicketRequest.of(ticket));
        TicketRequest changedTicketRequest = TicketRequest.builder()
                .serialNumber(SERIAL_NUMBER)
                .used(true)
                .build();

        adminService.changeTicketUsedState(AUTH_INFO_ADMIN, changedTicketRequest);

        Ticket actual = ticketRepository.findBySerialNumber(SERIAL_NUMBER)
                .orElseThrow(SerialNumberNotFoundException::new);
        assertThat(actual.isUsed()).isTrue();
    }

    @DisplayName("관리자 권한이 아닐 시 티켓 상태를 변경할 수 없다.")
    @Test
    void changeTicketUsedState_Exception_NoAdmin() {
        adminService.saveTicket(AUTH_INFO_ADMIN, TicketRequest.of(ticket));
        TicketRequest changedTicketRequest = TicketRequest.builder()
                .serialNumber(SERIAL_NUMBER)
                .used(true)
                .build();

        assertThatThrownBy(() -> adminService.changeTicketUsedState(AUTH_INFO, changedTicketRequest))
                .isInstanceOf(NoAdminException.class);
        Ticket actual = ticketRepository.findBySerialNumber(SERIAL_NUMBER)
                .orElseThrow(SerialNumberNotFoundException::new);
        assertThat(actual.isUsed()).isFalse();
    }

    @DisplayName("없는 티켓 상태를 변경할 수 없다.")
    @Test
    void changeTicketUsedState_Exception_NoTicket() {
        TicketRequest changedTicketRequest = TicketRequest.builder()
                .serialNumber(SERIAL_NUMBER)
                .used(true)
                .build();

        assertThatThrownBy(() -> adminService.changeTicketUsedState(AUTH_INFO_ADMIN, changedTicketRequest))
                .isInstanceOf(SerialNumberNotFoundException.class);
    }

    private Post savePost() {
        Post post = postRepository.save(this.post);
        postBoardRepository.save(PostBoard.builder().post(post).board(board).build());
        return post;
    }
}
