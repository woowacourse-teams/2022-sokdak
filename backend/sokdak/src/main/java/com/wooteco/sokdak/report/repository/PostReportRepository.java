package com.wooteco.sokdak.report.repository;

import com.wooteco.sokdak.report.domain.PostReport;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostReportRepository extends JpaRepository<PostReport, Long> {

    int countByPostId(Long postId);

    void deleteAllPostReportByPostId(Long id);

    List<PostReport> findAllByPostId(Long id);
}
