package com.wooteco.sokdak.board.acceptance;

import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.getToken;
import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.httpGet;
import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.httpPostWithAuthorization;
import static org.assertj.core.api.Assertions.assertThat;

import com.wooteco.sokdak.board.dto.BoardResponse;
import com.wooteco.sokdak.board.dto.NewBoardRequest;
import com.wooteco.sokdak.util.AcceptanceTest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("게시글 관련 인수테스트")
public class BoardAcceptanceTest extends AcceptanceTest {

    @DisplayName("게시판을 생성할 수 있다.")
    @Test
    void createBoard() {
        NewBoardRequest newBoardRequest = new NewBoardRequest("포수타");
        ExtractableResponse<Response> response = httpPostWithAuthorization(newBoardRequest, "/boards", getToken());

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("게시판 목록을 조회할 수 있다.")
    @Test
    void findBoards() {
        NewBoardRequest newBoardRequest1 = new NewBoardRequest("포수타");
        httpPostWithAuthorization(newBoardRequest1, "/boards", getToken());
        NewBoardRequest newBoardRequest2 = new NewBoardRequest("자유게시판");
        httpPostWithAuthorization(newBoardRequest2, "/boards", getToken());

        ExtractableResponse<Response> response = httpGet("/boards");

        assertThat(response.body().jsonPath().getList("boards", BoardResponse.class))
                .extracting("title")
                .containsExactly("포수타", "자유게시판");
    }
}
