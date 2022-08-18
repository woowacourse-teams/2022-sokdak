package com.wooteco.sokdak.board.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;

import com.wooteco.sokdak.board.dto.NewBoardRequest;
import com.wooteco.sokdak.board.dto.NewBoardResponse;
import com.wooteco.sokdak.util.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

class BoardControllerTest extends ControllerTest {

    @DisplayName("게시판을 생성하면 201 반환")
    @Test
    void createBoard() {
        NewBoardRequest newBoardRequest = new NewBoardRequest("포수타");

        doReturn(new NewBoardResponse(1L))
                .when(boardService)
                .createBoard(any());

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "any")
                .body(newBoardRequest)
                .when().post("/boards")
                .then().log().all()
                .apply(document("board/create/success"))
                .statusCode(HttpStatus.CREATED.value());
    }

    @DisplayName("게시판을 생성 요청에 이름이 빈 값이면 400 반환")
    @Test
    void createBoard_Exception_NoName() {
        NewBoardRequest newBoardRequest = new NewBoardRequest(null);

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "any")
                .body(newBoardRequest)
                .when().post("/boards")
                .then().log().all()
                .apply(document("board/create/fail"))
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("게시판 목록 요청 반환")
    @Test
    void findBoards() {
        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/boards")
                .then().log().all()
                .apply(document("board/find/board"))
                .statusCode(HttpStatus.OK.value());
    }


    @DisplayName("메인페이지 게시판 목록 반환")
    @Test
    void findBoardsContent() {
        restDocs
                .when().get("/boards/contents")
                .then().log().all()
                .apply(document("board/find/content"))
                .statusCode(HttpStatus.OK.value());
    }
}
