package com.wooteco.sokdak.notification.domain;

import com.wooteco.sokdak.auth.exception.AuthorizationException;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    private NotificationType notificationType;

    private Long memberId;

    private Long postId;

    private Long commentId;

    private boolean inquired;

    @CreatedDate
    private LocalDateTime createdAt;

    protected Notification() {
    }

    public Notification(NotificationType notificationType, Long memberId, Long postId, Long commentId) {
        this.notificationType = notificationType;
        this.memberId = memberId;
        this.postId = postId;
        this.commentId = commentId;
        this.inquired = false;
    }

    public void validateOwner(Long accessMemberId) {
        if (!memberId.equals(accessMemberId)) {
            throw new AuthorizationException();
        }
    }

    public boolean isCommentNotification() {
        return Objects.nonNull(commentId);
    }
}
