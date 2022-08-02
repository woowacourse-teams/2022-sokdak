package com.wooteco.sokdak.board.acceptance;

import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.getToken;
import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.httpGet;
import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.httpPostWithAuthorization;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.tuple;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.wooteco.sokdak.board.dto.BoardContentElement;
import com.wooteco.sokdak.board.dto.BoardContentPostElement;
import com.wooteco.sokdak.board.dto.BoardResponse;
import com.wooteco.sokdak.board.dto.NewBoardRequest;
import com.wooteco.sokdak.post.dto.NewPostRequest;
import com.wooteco.sokdak.util.AcceptanceTest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("게시글 관련 인수테스트")
public class BoardAcceptanceTest extends AcceptanceTest {

    public static final int WRITABLE_BOARD_FIRST_INDEX = 1;
    public static final int WTIABLE_BOARD_SECOND_INDEX = 2;

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
                .contains("포수타", "자유게시판");
    }

    @DisplayName("게시판 목록을 최신순의 게시글들과 함께 조회할 수 있다.")
    @Test
    void findBoardsContent() {
        // given
        String token = getToken();
        NewPostRequest postRequest1 = new NewPostRequest("제목1", "본문1", Collections.emptyList());
        NewPostRequest postRequest2 = new NewPostRequest("제목2", "본문2", Collections.emptyList());
        NewPostRequest postRequest3 = new NewPostRequest("제목3", "본문3", Collections.emptyList());
        NewPostRequest postRequest4 = new NewPostRequest("제목4", "본문4", Collections.emptyList());

        httpPostWithAuthorization(postRequest1, "/boards/" + 2 + "/posts", token);
        httpPostWithAuthorization(postRequest2, "/boards/" + 2 + "/posts", token);
        httpPostWithAuthorization(postRequest3, "/boards/" + 2 + "/posts", token);
        httpPostWithAuthorization(postRequest4, "/boards/" + 3 + "/posts", token);

        // when
        ExtractableResponse<Response> response = httpGet("/boards/contents");

        // then
        List<BoardContentElement> boardsAll = getBoardContentElements(response);
        BoardContentElement board1 = getBoardContentElements(response).get(WRITABLE_BOARD_FIRST_INDEX);
        BoardContentElement board2 = getBoardContentElements(response).get(WTIABLE_BOARD_SECOND_INDEX);
        List<BoardContentPostElement> posts1 = board1.getPosts();
        List<BoardContentPostElement> posts2 = board2.getPosts();

        assertAll(
                () -> assertThat(boardsAll)
                        .extracting("id", "title")
                        .containsExactly(
                                tuple(1L, "Hot 게시판"),
                                tuple(2L, "자유게시판"),
                                tuple(3L, "포수타"),
                                tuple(4L, "감동크루")
                        ),
                () -> assertThat(posts1)
                        .hasSize(3)
                        .extracting("id", "title")
                        .containsExactly(
                                tuple(3L, "제목3"),
                                tuple(2L, "제목2"),
                                tuple(1L, "제목1")
                        ),
                () -> assertThat(posts2)
                        .hasSize(1)
                        .extracting("id", "title")
                        .containsExactly(
                                tuple(4L, "제목4")
                        )
        );
    }

    private List<BoardContentElement> getBoardContentElements(ExtractableResponse<Response> response) {
        return response.body().jsonPath().getList("boards", BoardContentElement.class);
    }
}
