package com.wooteco.sokdak.like.dto;

import lombok.Getter;

@Getter
public class LikeFlipResponse {

    private int likeCount;
    private boolean like;

    public LikeFlipResponse() {
    }

    public LikeFlipResponse(int likeCount, boolean like) {
        this.likeCount = likeCount;
        this.like = like;
    }
}
