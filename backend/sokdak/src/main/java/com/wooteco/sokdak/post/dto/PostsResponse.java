package com.wooteco.sokdak.post.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class PostsResponse {

    private final List<PostsElementResponse> posts;
    private final boolean lastPage;

    public PostsResponse(List<PostsElementResponse> posts, boolean lastPage) {
        this.posts = posts;
        this.lastPage = lastPage;
    }
}
