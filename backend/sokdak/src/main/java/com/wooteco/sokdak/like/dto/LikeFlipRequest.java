package com.wooteco.sokdak.like.dto;

import lombok.Getter;

@Getter
public class LikeFlipRequest {

    private Long boardId;

    public LikeFlipRequest() {
    }

    public LikeFlipRequest(Long boardId) {
        this.boardId = boardId;
    }
}
