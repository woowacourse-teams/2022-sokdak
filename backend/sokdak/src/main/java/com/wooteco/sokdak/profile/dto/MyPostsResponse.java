package com.wooteco.sokdak.profile.dto;

import com.wooteco.sokdak.post.domain.Post;
import com.wooteco.sokdak.post.dto.PostsElementResponse;
import java.util.List;
import java.util.stream.Collectors;
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

    public static MyPostsResponse ofPosts(List<Post> posts, int totalPageCount) {
        List<PostsElementResponse> postsElementResponses = posts.stream()
                .map(PostsElementResponse::from)
                .collect(Collectors.toList());
        return new MyPostsResponse(postsElementResponses, totalPageCount);
    }
}
