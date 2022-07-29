package com.wooteco.sokdak.report.repository;

import com.wooteco.sokdak.report.domain.PostReport;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostReportRepository extends JpaRepository<PostReport, Long> {

    Optional<PostReport> findByReporterIdAndPostId(Long memberId, Long postId);

    int countByPostId(Long postId);
}
