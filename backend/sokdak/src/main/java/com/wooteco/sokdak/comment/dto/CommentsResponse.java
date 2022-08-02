package com.wooteco.sokdak.comment.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CommentsResponse {

    private List<CommentResponse> comments;

    public CommentsResponse() {
    }

    public CommentsResponse(List<CommentResponse> comments) {
        this.comments = comments;
    }
}
