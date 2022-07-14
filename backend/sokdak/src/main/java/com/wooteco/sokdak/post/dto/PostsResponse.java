package com.wooteco.sokdak.post.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class PostsResponse {

    private final List<PostResponse> posts;
    private final boolean lastPage;

    public PostsResponse(List<PostResponse> posts, boolean lastPage) {
        this.posts = posts;
        this.lastPage = lastPage;
    }
}
