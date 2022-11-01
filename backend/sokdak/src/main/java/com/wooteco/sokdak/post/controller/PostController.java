package com.wooteco.sokdak.post.controller;

import static org.springframework.data.domain.Sort.Direction.DESC;

import com.wooteco.sokdak.auth.dto.AuthInfo;
import com.wooteco.sokdak.post.dto.NewPostRequest;
import com.wooteco.sokdak.post.dto.PagePostsResponse;
import com.wooteco.sokdak.post.dto.PostDetailResponse;
import com.wooteco.sokdak.post.dto.PostUpdateRequest;
import com.wooteco.sokdak.post.dto.PostsCountResponse;
import com.wooteco.sokdak.post.dto.PostsResponse;
import com.wooteco.sokdak.post.service.PostService;
import com.wooteco.sokdak.support.token.Login;
import java.net.URI;
import javax.annotation.Nullable;
import javax.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<PostDetailResponse> findPost(@PathVariable Long id,
                                                       @Login AuthInfo authInfo,
                                                       @CookieValue(value = "viewedPost", required = false, defaultValue = "") String postLog) {
        PostDetailResponse postResponse = postService.findPost(id, authInfo, postLog);
        String updatedLog = postService.updatePostLog(id, postLog);
        ResponseCookie responseCookie = ResponseCookie.from("viewedPost", updatedLog).build();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, responseCookie.toString()).body(postResponse);
    }

    @PostMapping("/boards/{boardId}/posts")
    public ResponseEntity<Void> addPost(@PathVariable Long boardId,
                                        @Valid @RequestBody NewPostRequest newPostRequest,
                                        @Login AuthInfo authInfo) {
        Long postId = postService.addPost(boardId, newPostRequest, authInfo);
        return ResponseEntity.created(URI.create("/posts/" + postId)).build();
    }

    @GetMapping("/boards/{boardId}/posts")
    public ResponseEntity<PostsResponse> findPosts(
            @PathVariable Long boardId,
            @PageableDefault(sort = "createdAt", direction = DESC) Pageable pageable) {
        PostsResponse postsResponse = postService.findPostsByBoard(boardId, pageable);
        return ResponseEntity.ok(postsResponse);
    }

    @GetMapping(path = "/posts/count")
    public ResponseEntity<PostsCountResponse> searchPostCount(@RequestParam @Nullable String query) {
        PostsCountResponse postsResponse = postService.countPostWithQuery(query);
        return ResponseEntity.ok(postsResponse);
    }


    @GetMapping(path = "/posts")
    public ResponseEntity<PostsResponse> searchSlicePosts(@RequestParam @Nullable String query,
                                                          @PageableDefault(sort = "createdAt", direction = DESC) Pageable pageable) {
        PostsResponse postsResponse = postService.searchSliceWithQuery(query, pageable);
        return ResponseEntity.ok(postsResponse);
    }

    @GetMapping(path = "/posts/me")
    public ResponseEntity<PagePostsResponse> findMyPosts(
            @PageableDefault(sort = "createdAt", direction = DESC) Pageable pageable, @Login AuthInfo authInfo) {
        PagePostsResponse myPosts = postService.findMyPosts(pageable, authInfo);
        return ResponseEntity.ok(myPosts);
    }

    @PutMapping("/posts/{id}")
    public ResponseEntity<Void> updatePost(@PathVariable Long id,
                                           @Valid @RequestBody PostUpdateRequest postUpdateRequest,
                                           @Login AuthInfo authInfo) {
        postService.updatePost(id, postUpdateRequest, authInfo);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id, @Login AuthInfo authInfo) {
        postService.deletePost(id, authInfo);
        return ResponseEntity.noContent().build();
    }
}
