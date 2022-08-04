package com.wooteco.sokdak.board.dto;

import javax.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class NewBoardRequest {

    @NotBlank
    private String name;

    public NewBoardRequest() {
    }

    public NewBoardRequest(String name) {
        this.name = name;
    }
}
