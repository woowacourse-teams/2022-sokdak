package com.wooteco.sokdak.notification.controller;

import com.wooteco.sokdak.auth.dto.AuthInfo;
import com.wooteco.sokdak.notification.dto.NewNotificationCheckResponse;
import com.wooteco.sokdak.notification.service.NotificationService;
import com.wooteco.sokdak.support.token.Login;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
}
