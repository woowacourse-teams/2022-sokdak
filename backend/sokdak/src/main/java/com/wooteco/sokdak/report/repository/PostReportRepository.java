package com.wooteco.sokdak.report.repository;

import com.wooteco.sokdak.member.domain.Member;
import com.wooteco.sokdak.post.domain.Post;
import com.wooteco.sokdak.report.domain.PostReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostReportRepository extends JpaRepository<PostReport, Long> {

    void deleteAllPostReportByPost(Post post);

    boolean existsPostReportByPostAndReporter(Post post, Member member);
}
