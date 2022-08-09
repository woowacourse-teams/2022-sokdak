package com.wooteco.sokdak.profile.controller;

import com.wooteco.sokdak.auth.dto.AuthInfo;
import com.wooteco.sokdak.profile.dto.MyPostsResponse;
import com.wooteco.sokdak.profile.dto.NicknameResponse;
import com.wooteco.sokdak.profile.dto.NicknameUpdateRequest;
import com.wooteco.sokdak.profile.service.ProfileService;
import com.wooteco.sokdak.support.token.Login;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

    @PatchMapping ("/members/nickname")
    public ResponseEntity<Void> editNickname(@RequestBody NicknameUpdateRequest nicknameUpdateRequest,
                                             @Login AuthInfo authInfo) {
        profileService.editNickname(nicknameUpdateRequest, authInfo);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(path = "/posts/me", params = {"size", "page"})
    public ResponseEntity<MyPostsResponse> searchMyPost(@PageableDefault Pageable pageable,
                                                        @Login AuthInfo authInfo) {
        MyPostsResponse myPosts = profileService.findMyPosts(pageable, authInfo);
        return ResponseEntity.ok(myPosts);
    }
}
