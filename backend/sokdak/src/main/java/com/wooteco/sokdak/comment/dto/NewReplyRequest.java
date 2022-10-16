package com.wooteco.sokdak.comment.dto;

import javax.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class NewReplyRequest {

    private Long boardId;
    @NotBlank(message = "댓글은 1자 이상 50자 이하여야 합니다.")
    private String content;
    private boolean anonymous;

    public NewReplyRequest() {
    }

    public NewReplyRequest(Long boardId, String content, boolean anonymous) {
        this.boardId = boardId;
        this.content = content;
        this.anonymous = anonymous;
    }
}
