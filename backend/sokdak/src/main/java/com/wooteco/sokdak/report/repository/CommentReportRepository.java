package com.wooteco.sokdak.report.repository;

import com.wooteco.sokdak.comment.domain.Comment;
import com.wooteco.sokdak.member.domain.Member;
import com.wooteco.sokdak.report.domain.CommentReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentReportRepository extends JpaRepository<CommentReport, Long> {

    int countByCommentId(Long commentId);

    boolean existsCommentReportByCommentAndReporter(Comment comment, Member reporter);
}
