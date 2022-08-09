package com.wooteco.sokdak.profile.dto;

import com.wooteco.sokdak.post.dto.PostsElementResponse;
import java.util.List;
import lombok.Getter;

@Getter
public class MyPostsResponse {

    private List<PostsElementResponse> posts;
    private int totalPageCount;

    public MyPostsResponse() {
    }

    public MyPostsResponse(List<PostsElementResponse> posts, int totalPageCount) {
        this.posts = posts;
        this.totalPageCount = totalPageCount;
    }
}
