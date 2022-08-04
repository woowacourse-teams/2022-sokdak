package com.wooteco.sokdak.board.dto;

import com.wooteco.sokdak.board.domain.Board;

public class NewBoardResponse {
    private Long id;

    public NewBoardResponse(Long id) {
        this.id = id;
    }

    public NewBoardResponse(Board board) {
        this.id = board.getId();
    }

    public Long getId() {
        return id;
    }
}
