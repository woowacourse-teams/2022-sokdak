package com.wooteco.sokdak.notification.dto;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class NotificationResponse {

    private Long id;
    private String type;
    private Long postId;
    private LocalDateTime createdAt;
    private String content;

    public NotificationResponse() {
    }

    public NotificationResponse(Long id, String type, Long postId, LocalDateTime createdAt, String content) {
        this.id = id;
        this.type = type;
        this.postId = postId;
        this.createdAt = createdAt;
        this.content = content;
    }
}
