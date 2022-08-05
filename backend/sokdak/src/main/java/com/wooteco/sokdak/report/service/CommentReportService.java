package com.wooteco.sokdak.report.service;

import com.wooteco.sokdak.auth.dto.AuthInfo;
import com.wooteco.sokdak.comment.domain.Comment;
import com.wooteco.sokdak.comment.exception.CommentNotFoundException;
import com.wooteco.sokdak.comment.repository.CommentRepository;
import com.wooteco.sokdak.member.domain.Member;
import com.wooteco.sokdak.member.exception.MemberNotFoundException;
import com.wooteco.sokdak.member.repository.MemberRepository;
import com.wooteco.sokdak.report.domain.CommentReport;
import com.wooteco.sokdak.report.dto.ReportRequest;
import com.wooteco.sokdak.report.repository.CommentReportRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CommentReportService {

    private CommentReportRepository commentReportRepository;
    private CommentRepository commentRepository;
    private MemberRepository memberRepository;

    public CommentReportService(CommentReportRepository commentReportRepository,
                                CommentRepository commentRepository,
                                MemberRepository memberRepository) {
        this.commentReportRepository = commentReportRepository;
        this.commentRepository = commentRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public void reportComment(Long commentId, ReportRequest reportRequest, AuthInfo authInfo) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);
        Member reporter = memberRepository.findById(authInfo.getId())
                .orElseThrow(MemberNotFoundException::new);

        CommentReport commentReport = CommentReport.builder()
                .comment(comment)
                .reporter(reporter)
                .reportMessage(reportRequest.getMessage())
                .build();
        commentReportRepository.save(commentReport);
    }
}
