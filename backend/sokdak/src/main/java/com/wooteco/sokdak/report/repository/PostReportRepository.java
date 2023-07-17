package com.wooteco.sokdak.report.repository;

import com.wooteco.sokdak.member.domain.Member;
import com.wooteco.sokdak.post.domain.Post;
import com.wooteco.sokdak.report.domain.PostReport;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface PostReportRepository extends JpaRepository<PostReport, Long> {

    void deleteAllPostReportByPostId(Long postId);

    @Query(value = "DELETE FROM PostReport pr WHERE pr.postId = :postId")
    @Modifying
    void deleteAllByPostId(Long postId);

    boolean existsPostReportByPostIdAndReporter(Long postId, Member member);

    List<PostReport> findByPostId(Long postId);
}
