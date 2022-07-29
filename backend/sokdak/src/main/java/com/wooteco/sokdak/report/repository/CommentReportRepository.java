package com.wooteco.sokdak.report.repository;

import com.wooteco.sokdak.report.domain.CommentReport;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentReportRepository extends JpaRepository<CommentReport, Long> {

    Optional<CommentReport> findByReporterIdAndCommentId(Long memberId, Long commentId);

    int countByCommentId(Long commentId);
}
