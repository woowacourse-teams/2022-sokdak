package com.wooteco.sokdak.admin.service;

import com.wooteco.sokdak.admin.dto.EmailsAddRequest;
import com.wooteco.sokdak.admin.dto.PostReportElement;
import com.wooteco.sokdak.admin.dto.PostReportsResponse;
import com.wooteco.sokdak.admin.dto.TicketElement;
import com.wooteco.sokdak.admin.dto.TicketRequest;
import com.wooteco.sokdak.admin.dto.TicketsResponse;
import com.wooteco.sokdak.admin.exception.NoAdminException;
import com.wooteco.sokdak.ticket.domain.Ticket;
import com.wooteco.sokdak.auth.dto.AuthInfo;
import com.wooteco.sokdak.auth.domain.encryptor.Encryptor;
import com.wooteco.sokdak.member.domain.Member;
import com.wooteco.sokdak.member.domain.RoleType;
import com.wooteco.sokdak.member.exception.SerialNumberNotFoundException;
import com.wooteco.sokdak.member.repository.TicketRepository;
import com.wooteco.sokdak.post.domain.Post;
import com.wooteco.sokdak.post.exception.PostNotFoundException;
import com.wooteco.sokdak.post.repository.PostRepository;
import com.wooteco.sokdak.post.service.PostService;
import com.wooteco.sokdak.report.domain.PostReport;
import com.wooteco.sokdak.report.dto.ReportRequest;
import com.wooteco.sokdak.report.repository.PostReportRepository;
import com.wooteco.sokdak.report.service.PostReportService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AdminService {

    private final TicketRepository ticketRepository;
    private final PostService postService;
    private final PostReportService postReportService;
    private final PostRepository postRepository;
    private final PostReportRepository postReportRepository;
    private final Encryptor encryptor;

    public AdminService(TicketRepository ticketRepository, PostService postService,
                        PostReportService postReportService,
                        PostRepository postRepository,
                        PostReportRepository postReportRepository, Encryptor encryptor) {
        this.ticketRepository = ticketRepository;
        this.postService = postService;
        this.postReportService = postReportService;
        this.postRepository = postRepository;
        this.postReportRepository = postReportRepository;
        this.encryptor = encryptor;
    }

    @Transactional
    public void registerEmail(AuthInfo authInfo, EmailsAddRequest emailsAddRequest) {
        validateRole(authInfo);

        List<String> emails = emailsAddRequest.getEmails();

        for (String email : emails) {
            String serialNumber = encryptor.encode(email);
            Optional<Ticket> foundSerialNumber = ticketRepository.findBySerialNumber(serialNumber);
            if (foundSerialNumber.isEmpty()) {
                Ticket ticket = Ticket.builder()
                        .serialNumber(serialNumber)
                        .used(false)
                        .build();
                ticketRepository.save(ticket);
            }
        }
    }

    @Transactional
    public void deletePost(Long id, AuthInfo authInfo) {
        validateRole(authInfo);

        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFoundException::new);
        Member member = post.getMember();
        postService.deletePost(id, getAuthInfo(member));
    }

    @Transactional
    public void blockPost(Long id, Long postReportCount, AuthInfo authInfo) {
        validateRole(authInfo);

        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFoundException::new);
        ReportRequest reportRequest = new ReportRequest("adminBlocked");
        for (long i = 1; i <= postReportCount; i++) {
            AuthInfo dummyAuthInfo = new AuthInfo(i, RoleType.ADMIN.getName(), "관리자");
            postReportService.reportPost(post.getId(), reportRequest, dummyAuthInfo);
        }
    }

    @Transactional
    public void unblockPost(Long id, AuthInfo authInfo) {
        validateRole(authInfo);

        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFoundException::new);
        post.deleteAllReports();
        postReportRepository.deleteAllPostReportByPost(post);
    }

    public PostReportsResponse findAllPostReports(AuthInfo authInfo) {
        validateRole(authInfo);

        List<PostReport> postReports = postReportRepository.findAll();
        return PostReportsResponse.of(postReports.stream()
                .map(PostReportElement::of)
                .collect(Collectors.toList()));
    }

    public TicketsResponse findAllTickets(AuthInfo authInfo) {
        validateRole(authInfo);
        List<Ticket> tickets = ticketRepository.findAll();
        return TicketsResponse.of(tickets.stream()
                .map(TicketElement::of)
                .collect(Collectors.toList()));
    }

    @Transactional
    public void saveTicket(AuthInfo authInfo, TicketRequest ticketRequest) {
        validateRole(authInfo);
        ticketRepository.save(ticketRequest.toTicket());
    }

    @Transactional
    public void changeTicketUsedState(AuthInfo authInfo, TicketRequest ticketRequest) {
        validateRole(authInfo);
        Ticket ticket = ticketRepository.findBySerialNumber(ticketRequest.getSerialNumber())
                .orElseThrow(SerialNumberNotFoundException::new);
        ticket.updateUsed(ticketRequest.isUsed());
    }

    private AuthInfo getAuthInfo(Member member) {
        return new AuthInfo(member.getId(), RoleType.USER.getName(), member.getNickname());
    }

    private void validateRole(AuthInfo authInfo) {
        if (!authInfo.getRole().equals(RoleType.ADMIN.getName())) {
            throw new NoAdminException();
        }
    }
}
