package com.wooteco.sokdak.board.dto;

import com.wooteco.sokdak.post.dto.PostsElementResponse;
import lombok.Getter;

@Getter
public class BoardContentPostElement {

    private Long id;
    private int likeCount;
    private String title;
    private int commentCount;

    protected BoardContentPostElement() {
    }

    private BoardContentPostElement(Long id, int likeCount, String title, int commentCount) {
        this.id = id;
        this.likeCount = likeCount;
        this.title = title;
        this.commentCount = commentCount;
    }

    public static BoardContentPostElement of(PostsElementResponse postsElementResponse) {
        return new BoardContentPostElement(postsElementResponse.getId(), postsElementResponse.getLikeCount(),
                postsElementResponse.getTitle(), postsElementResponse.getCommentCount());
    }
}
