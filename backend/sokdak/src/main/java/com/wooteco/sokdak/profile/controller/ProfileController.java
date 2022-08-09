package com.wooteco.sokdak.profile.controller;

import com.wooteco.sokdak.auth.dto.AuthInfo;
import com.wooteco.sokdak.profile.dto.EditedNicknameRequest;
import com.wooteco.sokdak.profile.dto.NicknameResponse;
import com.wooteco.sokdak.profile.service.ProfileService;
import com.wooteco.sokdak.support.token.Login;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/members/nickname")
    public ResponseEntity<NicknameResponse> findNickname(@Login AuthInfo authInfo) {
        NicknameResponse nicknameResponse = profileService.findNickname(authInfo);
        return ResponseEntity.ok(nicknameResponse);
    }

    @PatchMapping("/members/nickname")
    public ResponseEntity<Void> editNickname(@RequestBody EditedNicknameRequest editedNicknameRequest,
                                             @Login AuthInfo authInfo) {
        profileService.editNickname(editedNicknameRequest, authInfo);
        return ResponseEntity.noContent().build();
    }
}
