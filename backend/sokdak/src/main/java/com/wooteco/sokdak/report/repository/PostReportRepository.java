package com.wooteco.sokdak.report.repository;

import com.wooteco.sokdak.report.domain.PostReport;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostReportRepository extends JpaRepository<PostReport, Long> {

    int countByPostId(Long postId);

    boolean existsByReporterIdAndPostId(Long id, Long postId);
}
