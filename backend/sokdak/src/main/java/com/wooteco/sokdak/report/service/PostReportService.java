package com.wooteco.sokdak.report.service;

import com.wooteco.sokdak.auth.dto.AuthInfo;
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
import java.rmi.AlreadyBoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PostReportService {

    private static final int BLOCKED_CONDITION = 5;

    private final PostReportRepository postReportRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final NotificationService notificationService;

    public PostReportService(PostReportRepository postReportRepository, PostRepository postRepository,
                             MemberRepository memberRepository, NotificationService notificationService) {
        this.postReportRepository = postReportRepository;
        this.postRepository = postRepository;
        this.memberRepository = memberRepository;
        this.notificationService = notificationService;
    }

    @Transactional
    public void reportPost(Long postId, ReportRequest reportRequest, AuthInfo authInfo) {
        if (postReportRepository.existsPostReportByPostIdAndMemberId(postId, authInfo.getId())) {
            throw new AlreadyReportPostException();
        }
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);
        Member member = memberRepository.findById(authInfo.getId())
                .orElseThrow(MemberNotFoundException::new);

        PostReport postReport = PostReport.builder()
                .post(post)
                .reporter(member)
                .reportMessage(reportRequest.getMessage())
                .build();
        postReportRepository.save(postReport);
        notifyReportIfOverThanBlockCondition(postId, post);
    }

    private void notifyReportIfOverThanBlockCondition(Long postId, Post post) {
        int reportCount = postReportRepository.countByPostId(postId);
        if (reportCount == BLOCKED_CONDITION) {
            notificationService.notifyPostReport(post);
        }
    }
}
