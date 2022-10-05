package com.wooteco.sokdak.report.service;

import com.wooteco.sokdak.auth.dto.AuthInfo;
import com.wooteco.sokdak.comment.domain.Comment;
import com.wooteco.sokdak.comment.exception.CommentNotFoundException;
import com.wooteco.sokdak.comment.repository.CommentRepository;
import com.wooteco.sokdak.member.domain.Member;
import com.wooteco.sokdak.member.exception.MemberNotFoundException;
import com.wooteco.sokdak.member.repository.MemberRepository;
import com.wooteco.sokdak.notification.service.NotificationService;
import com.wooteco.sokdak.post.domain.Post;
import com.wooteco.sokdak.report.domain.CommentReport;
import com.wooteco.sokdak.report.dto.ReportRequest;
import com.wooteco.sokdak.report.exception.AlreadyReportCommentException;
import com.wooteco.sokdak.report.repository.CommentReportRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CommentReportService {

    private static final int BLOCKED_CONDITION = 5;

    private final CommentReportRepository commentReportRepository;
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final NotificationService notificationService;

    public CommentReportService(CommentReportRepository commentReportRepository, CommentRepository commentRepository,
                                MemberRepository memberRepository, NotificationService notificationService) {
        this.commentReportRepository = commentReportRepository;
        this.commentRepository = commentRepository;
        this.memberRepository = memberRepository;
        this.notificationService = notificationService;
    }

    @Transactional
    public void reportComment(Long commentId, ReportRequest reportRequest, AuthInfo authInfo) {
        Comment comment = commentRepository.findByCommentId(commentId)
                .orElseThrow(CommentNotFoundException::new);
        Member reporter = memberRepository.findById(authInfo.getId())
                .orElseThrow(MemberNotFoundException::new);

        if (comment.hasReportByMember(reporter)) {
            throw new AlreadyReportCommentException();
        }

        CommentReport commentReport = CommentReport.builder()
                .comment(comment)
                .reporter(reporter)
                .reportMessage(reportRequest.getMessage())
                .build();
        commentReportRepository.save(commentReport);
        notifyReportIfOverBlockCondition(comment);
    }

    private void notifyReportIfOverBlockCondition(Comment comment) {
        if (comment.isBlocked()) {
            Post post = comment.getPost();
            notificationService.notifyCommentReport(post, comment);
        }
    }
}
