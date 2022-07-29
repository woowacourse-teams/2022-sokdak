package com.wooteco.sokdak.board.dto;

import lombok.Getter;

@Getter
public class BoardContentPostElement {

    private int likeCount;
    private String title;
    private int commentCount;

    protected BoardContentPostElement() {
    }

    public BoardContentPostElement(int likeCount, String title, int commentCount) {
        this.likeCount = likeCount;
        this.title = title;
        this.commentCount = commentCount;
    }
}
