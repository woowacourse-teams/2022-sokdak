package com.wooteco.sokdak.notification.domain;

import com.wooteco.sokdak.comment.domain.Comment;
import com.wooteco.sokdak.member.domain.Member;
import com.wooteco.sokdak.post.domain.Post;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    private NotificationType notificationType;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;

    private boolean inquired;

    @CreatedDate
    private LocalDateTime createdAt;

    protected Notification() {
    }

    @Builder
    private Notification(NotificationType notificationType, Member member, Post post, Comment comment) {
        this.notificationType = notificationType;
        this.member = member;
        this.post = post;
        this.comment = comment;
        this.inquired = false;
    }

    public String getContent() {
        if (getComment() == null) {
            return post.getTitle();
        }
        return getComment().getMessage();
    }
}
