package com.wooteco.sokdak.post.controller;

import static org.springframework.data.domain.Sort.Direction.DESC;

import com.wooteco.sokdak.auth.dto.AuthInfo;
import com.wooteco.sokdak.post.dto.NewPostRequest;
import com.wooteco.sokdak.post.dto.PostResponse;
import com.wooteco.sokdak.post.dto.PostUpdateRequest;
import com.wooteco.sokdak.post.dto.PostsResponse;
import com.wooteco.sokdak.post.service.PostService;
import com.wooteco.sokdak.support.Login;
import java.net.URI;
import javax.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> findPost(@PathVariable Long id) {
        PostResponse postResponse = postService.findPost(id);
        return ResponseEntity.ok(postResponse);
    }

    @PostMapping
    public ResponseEntity<Void> addPost(@Valid @RequestBody NewPostRequest newPostRequest, @Login AuthInfo authInfo) {
        Long postId = postService.addPost(newPostRequest, authInfo);
        return ResponseEntity.created(URI.create("/posts/" + postId)).build();
    }

    @GetMapping
    public ResponseEntity<PostsResponse> findPosts(
            @PageableDefault(sort = "createdAt", direction = DESC) Pageable pageable) {
        PostsResponse postsResponse = postService.findPosts(pageable);
        return ResponseEntity.ok(postsResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updatePost(@PathVariable Long id,
                                           @Valid @RequestBody PostUpdateRequest postUpdateRequest,
                                           @Login AuthInfo authInfo) {
        postService.updatePost(id, postUpdateRequest, authInfo);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id, @Login AuthInfo authInfo) {
        postService.deletePost(id, authInfo);
        return ResponseEntity.noContent().build();
    }
}
