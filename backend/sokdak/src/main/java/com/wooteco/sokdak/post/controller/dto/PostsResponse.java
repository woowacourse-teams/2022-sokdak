package com.wooteco.sokdak.post.controller.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class PostsResponse {

    private List<PostResponse> posts;

    public PostsResponse(List<PostResponse> posts) {
        this.posts = posts;
    }
}
