package com.wooteco.sokdak.profile.controller;

import com.wooteco.sokdak.auth.dto.AuthInfo;
import com.wooteco.sokdak.profile.dto.MyPostsResponse;
import com.wooteco.sokdak.profile.service.ProfileService;
import com.wooteco.sokdak.support.token.Login;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping(path = "/posts/me", params = {"size", "page"})
    public ResponseEntity<MyPostsResponse> searchMyPost(@PageableDefault Pageable pageable,
                                                        @Login AuthInfo authInfo) {
        MyPostsResponse myPosts = profileService.findMyPosts(pageable, authInfo);
        return ResponseEntity.ok(myPosts);
    }
}
