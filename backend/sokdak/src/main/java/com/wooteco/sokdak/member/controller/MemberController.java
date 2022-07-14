package com.wooteco.sokdak.member.controller;

import com.wooteco.sokdak.member.dto.EmailRequest;
import com.wooteco.sokdak.member.dto.VerificationRequest;
import com.wooteco.sokdak.member.service.EmailService;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
public class MemberController {

    private final EmailService emailService;

    public MemberController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/signup/email")
    public ResponseEntity<Void> sendEmail(@RequestBody EmailRequest emailRequest) {
        emailService.sendEmail(emailRequest);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/signup/email/verification")
    public ResponseEntity<Void> verifyAuthCode(@Valid @RequestBody VerificationRequest verificationRequest) {
        emailService.verifyAuthCode(verificationRequest);
        return ResponseEntity.noContent().build();
    }


}
