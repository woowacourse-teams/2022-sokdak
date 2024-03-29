package com.wooteco.sokdak.report.domain;

import com.wooteco.sokdak.member.domain.Member;
import com.wooteco.sokdak.post.domain.Post;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Builder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class PostReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_report_id")
    private Long id;

    private Long postId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member reporter;

    @Embedded
    private ReportMessage reportMessage;

    @CreatedDate
    private LocalDateTime createdAt;

    protected PostReport() {
    }

    @Builder
    private PostReport(Long postId, Member reporter, String reportMessage) {
        this.postId = postId;
        this.reporter = reporter;
        this.reportMessage = new ReportMessage(reportMessage);
    }

    public Long getId() {
        return id;
    }

    public Long getPostId() {
        return postId;
    }

    public Member getReporter() {
        return reporter;
    }

    public String getReportMessage() {
        return reportMessage.getValue();
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public boolean isOwner(Member other) {
        // TODO: Member의 equals, hashCode에서 id가 아닌 username을 이용하도록 수정해야 됨.
        return this.reporter.getUsername().equals(other.getUsername());
    }
}
