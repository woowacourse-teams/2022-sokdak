package com.wooteco.sokdak.report.domain;

import com.wooteco.sokdak.comment.domain.Comment;
import com.wooteco.sokdak.member.domain.Member;
import java.time.LocalDate;
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
public class CommentReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_report_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member reporter;

    @Embedded
    private ReportMessage reportMessage;

    @CreatedDate
    private LocalDateTime createdAt;

    protected CommentReport() {}

    @Builder
    public CommentReport(Comment comment, Member reporter, String reportMessage) {
        this.comment = comment;
        this.reporter = reporter;
        this.reportMessage = new ReportMessage(reportMessage);
    }

    public Long getId() {
        return id;
    }

    public Comment getComment() {
        return comment;
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
