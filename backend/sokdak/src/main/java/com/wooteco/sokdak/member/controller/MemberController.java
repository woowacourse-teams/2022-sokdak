package com.wooteco.sokdak.member.controller;

import com.wooteco.sokdak.member.dto.EmailRequest;
import com.wooteco.sokdak.member.dto.SignupRequest;
import com.wooteco.sokdak.member.dto.UniqueResponse;
import com.wooteco.sokdak.member.dto.VerificationRequest;
import com.wooteco.sokdak.member.service.EmailService;
import com.wooteco.sokdak.member.service.MemberService;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
public class MemberController {

    private final EmailService emailService;
    private final MemberService memberService;

    public MemberController(EmailService emailService, MemberService memberService) {
        this.emailService = emailService;
        this.memberService = memberService;
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

    @GetMapping(value = "/signup/exists", params = "username")
    public ResponseEntity<UniqueResponse> validateUniqueUsername(@RequestParam String username) {
        UniqueResponse uniqueResponse = memberService.checkUniqueUsername(username);
        return ResponseEntity.ok(uniqueResponse);
    }

    @GetMapping(value = "/signup/exists", params = "nickname")
    public ResponseEntity<UniqueResponse> validateUniqueNickname(@RequestParam String nickname) {
        UniqueResponse uniqueResponse = memberService.checkUniqueNickname(nickname);
        return ResponseEntity.ok(uniqueResponse);
    }

    @PostMapping("/signup")
    public ResponseEntity<Void> signUp(@Valid @RequestBody SignupRequest signupRequest) {
        memberService.signUp(signupRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
