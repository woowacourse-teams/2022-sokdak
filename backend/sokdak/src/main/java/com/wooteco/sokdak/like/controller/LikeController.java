package com.wooteco.sokdak.like.controller;

import com.wooteco.sokdak.auth.dto.AuthInfo;
import com.wooteco.sokdak.like.dto.LikeFlipResponse;
import com.wooteco.sokdak.like.service.LikeService;
import com.wooteco.sokdak.support.token.AuthenticationPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/posts/")
public class LikeController {

    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @PutMapping("/{id}/like")
    public ResponseEntity<LikeFlipResponse> flipLike(@PathVariable("id") Long postId,
                                                     @AuthenticationPrincipal AuthInfo authInfo) {
        LikeFlipResponse likeFlipResponse = likeService.flipLike(postId, authInfo);
        return ResponseEntity.ok(likeFlipResponse);
    }
}
