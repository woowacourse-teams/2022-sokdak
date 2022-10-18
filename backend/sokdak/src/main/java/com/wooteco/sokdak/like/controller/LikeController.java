package com.wooteco.sokdak.like.controller;

import com.wooteco.sokdak.auth.dto.AuthInfo;
import com.wooteco.sokdak.like.dto.LikeFlipResponse;
import com.wooteco.sokdak.like.service.LikeService;
import com.wooteco.sokdak.support.token.Login;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LikeController {

    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @PutMapping("/posts/{id}/like")
    public ResponseEntity<LikeFlipResponse> flipPostLike(@PathVariable("id") Long postId,
                                                         @Login AuthInfo authInfo) {
        LikeFlipResponse likeFlipResponse = likeService.flipPostLike(postId, authInfo);
        return ResponseEntity.ok(likeFlipResponse);
    }

    @PutMapping("/comments/{id}/like")
    public ResponseEntity<LikeFlipResponse> flipCommentLike(@PathVariable("id") Long commentId,
                                                            @Login AuthInfo authInfo) {
        LikeFlipResponse likeFlipResponse = likeService.flipCommentLike(commentId, authInfo);
        return ResponseEntity.ok(likeFlipResponse);
    }
}
