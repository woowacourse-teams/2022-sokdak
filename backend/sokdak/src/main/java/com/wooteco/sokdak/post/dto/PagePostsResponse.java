package com.wooteco.sokdak.post.dto;

import com.wooteco.sokdak.post.domain.Post;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public class PagePostsResponse {

    private List<PostsElementResponse> posts;
    private int totalPageCount;
    private int totalPostCount;

    public PagePostsResponse() {
    }

    public PagePostsResponse(List<PostsElementResponse> posts, int totalPageCount, int totalPostCount) {
        this.posts = posts;
        this.totalPageCount = totalPageCount;
        this.totalPostCount = totalPostCount;
    }

    public static PagePostsResponse of(List<Post> posts, int totalPageCount, int totalPostCount) {
        List<PostsElementResponse> postsElementResponses = posts.stream()
                .map(PostsElementResponse::from)
                .collect(Collectors.toList());
        return new PagePostsResponse(postsElementResponses, totalPageCount, totalPostCount);
    }
}
