package com.wooteco.sokdak.comment.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class CommentsResponse {

    private final List<CommentResponse> comments;

    public CommentsResponse(List<CommentResponse> comments) {
        this.comments = comments;
    }
}
