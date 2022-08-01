package com.wooteco.sokdak.board.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class BoardContentResponse {

    private List<BoardContentElement> boards;

    protected BoardContentResponse() {
    }

    public BoardContentResponse(List<BoardContentElement> boards) {
        this.boards = boards;
    }

    public static BoardContentResponse of(List<BoardContentElement> boardContentElements) {
        return new BoardContentResponse(boardContentElements);
    }
}
