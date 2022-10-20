package com.wooteco.sokdak.post.dto;

import lombok.Getter;

@Getter
public class PostsCountResponse {
    private int totalPostCount;

    public PostsCountResponse() {
    }

    public PostsCountResponse(int totalPostCount) {
        this.totalPostCount = totalPostCount;
    }

    public static PostsCountResponse of(int totalPostCount) {
        return new PostsCountResponse(totalPostCount);
    }
}
