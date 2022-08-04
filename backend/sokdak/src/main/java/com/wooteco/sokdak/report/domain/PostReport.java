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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

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
    private PostReport(Post post, Member reporter, String reportMessage) {
        this.post = post;
        this.reporter = reporter;
        this.reportMessage = new ReportMessage(reportMessage);
        addPost();
    }

    public boolean isSameReporter(PostReport other) {
        return this.reporter.equals(other.reporter);
    }

    private void addPost() {
        this.post.addReport(this);
    }

    public Long getId() {
        return id;
    }

    public Post getPost() {
        return post;
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
}
