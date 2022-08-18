package com.wooteco.sokdak.admin.controller;

import com.wooteco.sokdak.admin.dto.EmailsAddRequest;
import com.wooteco.sokdak.admin.service.AdminService;
import com.wooteco.sokdak.auth.dto.AuthInfo;
import com.wooteco.sokdak.support.token.Login;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping
    public ResponseEntity<Void> success() {
        return ResponseEntity.ok().build();
    }


    @PostMapping("/email")
    public ResponseEntity<Void> registerEmail(@Login AuthInfo authInfo, @RequestBody EmailsAddRequest emailsAddRequest) {
        adminService.registerEmail(authInfo, emailsAddRequest);

        return ResponseEntity.noContent().build();
    }
}
