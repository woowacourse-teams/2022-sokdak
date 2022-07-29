package com.wooteco.sokdak.post.dto;

import com.wooteco.sokdak.board.domain.PostBoard;
import com.wooteco.sokdak.post.domain.Post;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import org.springframework.data.domain.Slice;

@Getter
public class PostsResponse {

    private final List<PostsElementResponse> posts;
    private final boolean lastPage;

    public PostsResponse(List<PostsElementResponse> posts, boolean lastPage) {
        this.posts = posts;
        this.lastPage = lastPage;
    }

    public static PostsResponse ofPostSlice(Slice<Post> postSlice) {
        List<PostsElementResponse> postsElementResponses = postSlice.getContent()
                .stream()
                .map(PostsElementResponse::from)
                .collect(Collectors.toList());
        return new PostsResponse(postsElementResponses, postSlice.isLast());
    }

    public static PostsResponse ofPostBoardSlice(Slice<PostBoard> postBoards) {
        List<PostsElementResponse> postsElementResponses = postBoards.getContent()
                .stream()
                .map(PostBoard::getPost)
                .map(PostsElementResponse::from)
                .collect(Collectors.toList());
        return new PostsResponse(postsElementResponses, postBoards.isLast());
    }
}
