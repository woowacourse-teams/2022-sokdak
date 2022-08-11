package com.wooteco.sokdak.notification.dto;

import com.wooteco.sokdak.notification.domain.NotificationType;
import java.time.LocalDateTime;
import lombok.Builder;
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

    @Builder
    private NotificationResponse(Long id, String content, LocalDateTime createdAt,
                                 NotificationType type, Long postId) {
        this.id = id;
        this.content = content;
        this.createdAt = createdAt;
        this.type = type.name();
        this.postId = postId;
    }
}
