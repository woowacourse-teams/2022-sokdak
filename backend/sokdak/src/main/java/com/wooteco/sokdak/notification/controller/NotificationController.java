package com.wooteco.sokdak.notification.controller;

import static org.springframework.data.domain.Sort.Direction.DESC;

import com.wooteco.sokdak.auth.dto.AuthInfo;
import com.wooteco.sokdak.notification.dto.NewNotificationCheckResponse;
import com.wooteco.sokdak.notification.dto.NotificationsResponse;
import com.wooteco.sokdak.notification.service.NotificationService;
import com.wooteco.sokdak.support.token.Login;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/check")
    public ResponseEntity<NewNotificationCheckResponse> checkNewNotification(@Login AuthInfo authInfo) {
        return ResponseEntity.ok(notificationService.checkNewNotification(authInfo));
    }

    @GetMapping
    public ResponseEntity<NotificationsResponse> findNotifications(
            @Login AuthInfo authInfo, @PageableDefault(sort = "createdAt", direction = DESC) Pageable pageable) {
        return ResponseEntity.ok(notificationService.findNotifications(authInfo, pageable));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(@Login AuthInfo authInfo, @PathVariable Long id) {
        notificationService.deleteNotification(authInfo, id);
        return ResponseEntity.noContent().build();
    }
}
