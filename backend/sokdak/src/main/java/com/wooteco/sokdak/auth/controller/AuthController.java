package com.wooteco.sokdak.auth.controller;

import com.wooteco.sokdak.auth.dto.AuthInfo;
import com.wooteco.sokdak.auth.dto.LoginRequest;
import com.wooteco.sokdak.auth.service.AuthService;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

@RestController
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(HttpSession httpSession, @Valid @RequestBody LoginRequest loginRequest) {
        AuthInfo authInfo = authService.login(loginRequest);
        httpSession.setAttribute("member", authInfo);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/test")
    public ResponseEntity<Void> testSession(@SessionAttribute(name = "member") AuthInfo authInfo) {
        System.err.println("성공 authInfo = " + authInfo);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/test2")
    public ResponseEntity<Void> testSession2(HttpSession httpSession) {
        AuthInfo authInfo = (AuthInfo) httpSession.getAttribute("member");
        System.out.println(authInfo.getId() + "!!!!!");
        return ResponseEntity.ok().build();
    }
}
