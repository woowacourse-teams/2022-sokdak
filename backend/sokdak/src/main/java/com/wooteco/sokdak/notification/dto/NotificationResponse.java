package com.wooteco.sokdak.notification.dto;

import com.wooteco.sokdak.notification.domain.Notification;
import com.wooteco.sokdak.notification.domain.NotificationType;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class NotificationResponse {

    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private String type;
    private Long postId;

    public NotificationResponse() {
    }

    public NotificationResponse(Long id, String content, LocalDateTime createdAt,
                                NotificationType type, Long postId) {
        this.id = id;
        this.content = content;
        this.createdAt = createdAt;
        this.type = type.name();
        this.postId = postId;
    }

    public static NotificationResponse of(Notification notification) {
        return new NotificationResponse(notification.getId(), notification.getContent(),
                notification.getCreatedAt(), notification.getNotificationType(), notification.getPost().getId());
    }
}
