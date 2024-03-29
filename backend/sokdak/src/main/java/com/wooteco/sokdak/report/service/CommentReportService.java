package com.wooteco.sokdak.report.service;

import com.wooteco.sokdak.auth.dto.AuthInfo;
import com.wooteco.sokdak.auth.service.AuthService;
import com.wooteco.sokdak.comment.domain.Comment;
import com.wooteco.sokdak.comment.exception.CommentNotFoundException;
import com.wooteco.sokdak.comment.repository.CommentRepository;
import com.wooteco.sokdak.member.domain.Member;
import com.wooteco.sokdak.member.exception.MemberNotFoundException;
import com.wooteco.sokdak.member.repository.MemberRepository;
import com.wooteco.sokdak.post.domain.Post;
import com.wooteco.sokdak.report.domain.CommentReport;
import com.wooteco.sokdak.report.domain.CommentReportEvent;
import com.wooteco.sokdak.report.dto.ReportRequest;
import com.wooteco.sokdak.report.exception.AlreadyReportCommentException;
import com.wooteco.sokdak.report.repository.CommentReportRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CommentReportService {

    private final CommentReportRepository commentReportRepository;
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final AuthService authService;
    private final ApplicationEventPublisher applicationEventPublisher;

    public CommentReportService(CommentReportRepository commentReportRepository, CommentRepository commentRepository,
                                MemberRepository memberRepository, AuthService authService,
                                ApplicationEventPublisher applicationEventPublisher) {
        this.commentReportRepository = commentReportRepository;
        this.commentRepository = commentRepository;
        this.memberRepository = memberRepository;
        this.authService = authService;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Transactional
    public void reportComment(Long commentId, ReportRequest reportRequest, AuthInfo authInfo) {
        Comment comment = commentRepository.findByCommentId(commentId)
                .orElseThrow(CommentNotFoundException::new);
        authService.checkAuthority(authInfo, comment.getBoardId());
        Member reporter = memberRepository.findById(authInfo.getId())
                .orElseThrow(MemberNotFoundException::new);
        checkMemberAlreadyReport(comment, reporter);
        CommentReport commentReport = createCommentReport(comment, reporter, reportRequest.getMessage());
        commentReportRepository.save(commentReport);
        notifyReportIfOverBlockCondition(comment);
    }

    private CommentReport createCommentReport(Comment comment, Member reporter, String message) {
        return CommentReport.builder()
                .comment(comment)
                .reporter(reporter)
                .reportMessage(message)
                .build();
    }

    private void checkMemberAlreadyReport(Comment comment, Member member) {
        if (comment.hasReportByMember(member)) {
            throw new AlreadyReportCommentException();
        }
    }

    private void notifyReportIfOverBlockCondition(Comment comment) {
        if (comment.isBlocked()) {
            Post post = comment.getPost();
            applicationEventPublisher.publishEvent(
                    new CommentReportEvent(comment.getMember().getId(), post.getId(), comment.getId()));
        }
    }
}
