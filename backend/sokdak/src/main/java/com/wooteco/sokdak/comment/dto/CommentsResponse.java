package com.wooteco.sokdak.comment.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class CommentsResponse {

    private List<CommentResponse> comments;
    private int totalCount;

    public CommentsResponse() {
    }

    public CommentsResponse(List<CommentResponse> comments, int totalCount) {
        this.comments = comments;
        this.totalCount = totalCount;
    }
}
