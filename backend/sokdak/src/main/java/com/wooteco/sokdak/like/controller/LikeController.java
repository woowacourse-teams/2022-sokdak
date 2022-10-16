package com.wooteco.sokdak.like.controller;

import com.wooteco.sokdak.auth.dto.AuthInfo;
import com.wooteco.sokdak.like.dto.LikeFlipRequest;
import com.wooteco.sokdak.like.dto.LikeFlipResponse;
import com.wooteco.sokdak.like.service.LikeService;
import com.wooteco.sokdak.support.token.Login;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LikeController {

    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @PutMapping("/posts/{id}/like")
    public ResponseEntity<LikeFlipResponse> flipPostLike(@PathVariable("id") Long postId,
                                                         @RequestBody LikeFlipRequest likeFlipRequest,
                                                         @Login AuthInfo authInfo) {
        LikeFlipResponse likeFlipResponse = likeService.flipPostLike(postId, authInfo, likeFlipRequest);
        return ResponseEntity.ok(likeFlipResponse);
    }

    @PutMapping("/comments/{id}/like")
    public ResponseEntity<LikeFlipResponse> flipCommentLike(@PathVariable("id") Long commentId,
                                                            @RequestBody LikeFlipRequest likeFlipRequest,
                                                            @Login AuthInfo authInfo) {
        LikeFlipResponse likeFlipResponse = likeService.flipCommentLike(commentId, authInfo, likeFlipRequest);
        return ResponseEntity.ok(likeFlipResponse);
    }
}
