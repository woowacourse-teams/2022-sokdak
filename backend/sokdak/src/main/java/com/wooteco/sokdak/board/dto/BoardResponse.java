package com.wooteco.sokdak.board.dto;

import com.wooteco.sokdak.board.domain.Board;
import lombok.Getter;

@Getter
public class BoardResponse {

    private Long id;
    private String title;

    public BoardResponse(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    public BoardResponse(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
    }
}
