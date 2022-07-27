package com.wooteco.sokdak.board.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class BoardsResponse {

    private List<BoardResponse> boards;

    public BoardsResponse(List<BoardResponse> boards) {
        this.boards = boards;
    }
}
