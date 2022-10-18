package com.wooteco.sokdak.report.service;

import com.wooteco.sokdak.auth.dto.AuthInfo;
import com.wooteco.sokdak.auth.service.AuthService;
import com.wooteco.sokdak.member.domain.Member;
import com.wooteco.sokdak.member.exception.MemberNotFoundException;
import com.wooteco.sokdak.member.repository.MemberRepository;
import com.wooteco.sokdak.notification.service.NotificationService;
import com.wooteco.sokdak.post.domain.Post;
import com.wooteco.sokdak.post.exception.PostNotFoundException;
import com.wooteco.sokdak.post.repository.PostRepository;
import com.wooteco.sokdak.report.domain.PostReport;
import com.wooteco.sokdak.report.dto.ReportRequest;
import com.wooteco.sokdak.report.exception.AlreadyReportPostException;
import com.wooteco.sokdak.report.repository.PostReportRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PostReportService {

    private final PostReportRepository postReportRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final NotificationService notificationService;
    private final AuthService authService;

    public PostReportService(PostReportRepository postReportRepository, PostRepository postRepository,
                             MemberRepository memberRepository, NotificationService notificationService,
                             AuthService authService) {
        this.postReportRepository = postReportRepository;
        this.postRepository = postRepository;
        this.memberRepository = memberRepository;
        this.notificationService = notificationService;
        this.authService = authService;
    }

    @Transactional
    public void reportPost(Long postId, ReportRequest reportRequest, AuthInfo authInfo) {
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);
        authService.checkAuthority(authInfo, post.getBoardId());
        Member member = memberRepository.findById(authInfo.getId())
                .orElseThrow(MemberNotFoundException::new);
        checkMemberAlreadyReport(post, member);
        PostReport postReport = createPostReport(post, member, reportRequest.getMessage());
        postReportRepository.save(postReport);
        notifyReportIfOverThanBlockCondition(post);
    }

    private PostReport createPostReport(Post post, Member member, String message) {
        return PostReport.builder()
                .post(post)
                .reporter(member)
                .reportMessage(message)
                .build();
    }

    private void checkMemberAlreadyReport(Post post, Member member) {
        if (post.hasReportByMember(member)) {
            throw new AlreadyReportPostException();
        }
    }

    private void notifyReportIfOverThanBlockCondition(Post post) {
        if (post.isBlocked()) {
            notificationService.notifyPostReport(post);
        }
    }
}
