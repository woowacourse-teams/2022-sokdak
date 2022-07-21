package com.wooteco.sokdak.comment.controller;

import com.wooteco.sokdak.auth.dto.AuthInfo;
import com.wooteco.sokdak.comment.dto.CommentsResponse;
import com.wooteco.sokdak.comment.dto.NewCommentRequest;
import com.wooteco.sokdak.comment.service.CommentService;
import com.wooteco.sokdak.support.token.Login;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/posts")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<Void> addComment(@PathVariable(name = "id") Long postId,
                                           @Valid @RequestBody NewCommentRequest newCommentRequest,
                                           @Login AuthInfo authInfo) {
        commentService.addComment(postId, newCommentRequest, authInfo);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<CommentsResponse> findComments(@PathVariable(name = "id") Long postId) {
        CommentsResponse commentsResponse = commentService.findComments(postId);
        return ResponseEntity.ok(commentsResponse);
    }
}
