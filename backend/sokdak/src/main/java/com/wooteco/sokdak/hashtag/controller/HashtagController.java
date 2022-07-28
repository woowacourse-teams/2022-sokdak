package com.wooteco.sokdak.hashtag.controller;

import static org.springframework.data.domain.Sort.Direction.DESC;

import com.wooteco.sokdak.hashtag.service.HashtagService;
import com.wooteco.sokdak.post.dto.PostsResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class HashtagController {

    private final HashtagService hashtagService;

    public HashtagController(HashtagService hashtagService) {
        this.hashtagService = hashtagService;
    }

    @GetMapping(path = "/posts", params = {"hashtag", "size", "page"})
    public ResponseEntity<PostsResponse> findPostsWithHashtag(@RequestParam String hashtag,
                                                              @PageableDefault(sort = "createdAt", direction = DESC) Pageable pageable) {
        PostsResponse postsResponse = hashtagService.findPostsWithHashtag(hashtag, pageable);
        return ResponseEntity.ok(postsResponse);
    }
}
