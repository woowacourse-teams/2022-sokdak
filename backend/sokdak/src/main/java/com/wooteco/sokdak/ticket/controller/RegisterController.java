package com.wooteco.sokdak.ticket.controller;

import com.wooteco.sokdak.member.dto.VerificationRequest;
import com.wooteco.sokdak.ticket.service.RegisterService;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegisterController {

    private final RegisterService registerService;

    public RegisterController(RegisterService registerService) {
        this.registerService = registerService;
    }

    @PostMapping("members/signup/email/verification")
    public ResponseEntity<Void> verifyAuthCode(@Valid @RequestBody VerificationRequest verificationRequest) {
        registerService.verifyAuthCode(verificationRequest);
        return ResponseEntity.noContent().build();
    }
}
