package com.wooteco.sokdak.post.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class PostsResponse {

    private List<PostResponse> posts;
    private boolean isLastPage;

    public PostsResponse(List<PostResponse> posts, boolean isLastPage) {
        this.posts = posts;
        this.isLastPage = isLastPage;
    }
}
