package com.wooteco.sokdak.board.acceptance;

import static com.wooteco.sokdak.util.fixture.BoardFixture.FREE_BOARD_ID;
import static com.wooteco.sokdak.util.fixture.BoardFixture.HOT_BOARD_ID;
import static com.wooteco.sokdak.util.fixture.BoardFixture.POSUTA_BOARD_ID;
import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.httpDeleteWithAuthorization;
import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.httpGet;
import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.httpPost;
import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.httpPostWithAuthorization;
import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.httpPutWithAuthorization;
import static com.wooteco.sokdak.util.fixture.MemberFixture.getChrisToken;
import static com.wooteco.sokdak.util.fixture.PostFixture.addNewPost;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.tuple;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import com.wooteco.sokdak.auth.dto.LoginRequest;
import com.wooteco.sokdak.board.dto.BoardContentElement;
import com.wooteco.sokdak.board.dto.BoardContentPostElement;
import com.wooteco.sokdak.board.dto.BoardResponse;
import com.wooteco.sokdak.board.dto.NewBoardRequest;
import com.wooteco.sokdak.post.dto.NewPostRequest;
import com.wooteco.sokdak.post.dto.PostsElementResponse;
import com.wooteco.sokdak.post.dto.PostsResponse;
import com.wooteco.sokdak.util.AcceptanceTest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("게시글 관련 인수테스트")
class BoardAcceptanceTest extends AcceptanceTest {

    @DisplayName("게시판을 생성할 수 있다.")
    @Test
    void createBoard() {
        NewBoardRequest newBoardRequest = new NewBoardRequest("포수타");
        ExtractableResponse<Response> response = httpPostWithAuthorization(newBoardRequest, "/boards", getChrisToken());

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("게시판 목록을 조회할 수 있다.")
    @Test
    void findBoards() {
        NewBoardRequest newBoardRequest1 = new NewBoardRequest("포수타");
        httpPostWithAuthorization(newBoardRequest1, "/boards", getChrisToken());
        NewBoardRequest newBoardRequest2 = new NewBoardRequest("자유게시판");
        httpPostWithAuthorization(newBoardRequest2, "/boards", getChrisToken());

        ExtractableResponse<Response> response = httpGet("/boards");

        assertThat(response.body().jsonPath().getList("boards", BoardResponse.class))
                .extracting("title")
                .contains("포수타", "자유게시판");
    }

    @DisplayName("게시판 목록을 최신순의 게시글들과 함께 조회할 수 있다.")
    @Test
    void findBoardsContent() {
        // given
        String token = getChrisToken();
        NewPostRequest postRequest1 = new NewPostRequest("제목1", "본문1", false, Collections.emptyList());
        NewPostRequest postRequest2 = new NewPostRequest("제목2", "본문2", false, Collections.emptyList());
        NewPostRequest postRequest3 = new NewPostRequest("제목3", "본문3", false, Collections.emptyList());
        NewPostRequest postRequest4 = new NewPostRequest("제목4", "본문4", false, Collections.emptyList());

        httpPostWithAuthorization(postRequest1, "/boards/" + FREE_BOARD_ID + "/posts", token);
        httpPostWithAuthorization(postRequest2, "/boards/" + FREE_BOARD_ID + "/posts", token);
        httpPostWithAuthorization(postRequest3, "/boards/" + FREE_BOARD_ID + "/posts", token);
        httpPostWithAuthorization(postRequest4, "/boards/" + POSUTA_BOARD_ID + "/posts", token);

        // when
        ExtractableResponse<Response> response = httpGet("/boards/contents");

        // then
        List<BoardContentElement> boardsAll = getBoardContentElements(response);
        BoardContentElement board1 = getBoardContentElements(response).get((int) FREE_BOARD_ID - 1);
        BoardContentElement board2 = getBoardContentElements(response).get((int) POSUTA_BOARD_ID - 1);
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

    @DisplayName("좋아요 개수가 5개 이상이면 Hot 게시판에도 글이 등록된다")
    @Test
    void saveInHotBoardWithMoreThan5Likes() {
        // given
        String token1 = getChrisToken();
        String token2 = httpPost(new LoginRequest("josh", "Abcd123!@"), "/login").header(AUTHORIZATION);
        String token3 = httpPost(new LoginRequest("thor", "Abcd123!@"), "/login").header(AUTHORIZATION);
        String token4 = httpPost(new LoginRequest("hunch", "Abcd123!@"), "/login").header(AUTHORIZATION);
        String token5 = httpPost(new LoginRequest("east", "Abcd123!@"), "/login").header(AUTHORIZATION);
        List<String> tokens = List.of(token1, token2, token3, token4, token5);

        addNewPost();

        // when
        for (String token : tokens) {
            httpPutWithAuthorization("/posts/1/like", token);
        }
        ExtractableResponse<Response> hotBoardResponse = httpGet("/boards/" + HOT_BOARD_ID + "/posts?size=2&page=0");
        List<String> hotBoardPostNames = parsePostTitles(hotBoardResponse);
        ExtractableResponse<Response> boardResponse = httpGet("/boards/" + FREE_BOARD_ID + "/posts?size=2&page=0");
        List<String> boardPostNames = parsePostTitles(boardResponse);

        // then
        assertAll(
                () -> assertThat(hotBoardResponse.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(hotBoardPostNames).isEqualTo(List.of("제목")),
                () -> assertThat(boardResponse.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(boardPostNames).isEqualTo(List.of("제목"))
        );
    }

    @DisplayName("좋아요 개수가 5개 이상이어도 Hot 게시판에는 하나만 등록된다")
    @Test
    void saveInHotBoardWith6Likes() {
        // given
        String token1 = getChrisToken();
        String token2 = httpPost(new LoginRequest("josh", "Abcd123!@"), "/login").header(AUTHORIZATION);
        String token3 = httpPost(new LoginRequest("thor", "Abcd123!@"), "/login").header(AUTHORIZATION);
        String token4 = httpPost(new LoginRequest("hunch", "Abcd123!@"), "/login").header(AUTHORIZATION);
        String token5 = httpPost(new LoginRequest("east", "Abcd123!@"), "/login").header(AUTHORIZATION);
        String token6 = httpPost(new LoginRequest("movie", "Abcd123!@"), "/login").header(AUTHORIZATION);
        List<String> tokens = List.of(token1, token2, token3, token4, token5, token6);

        addNewPost();

        // when
        for (String token : tokens) {
            httpPutWithAuthorization("/posts/1/like", token);
        }
        ExtractableResponse<Response> hotBoardResponse = httpGet("/boards/" + HOT_BOARD_ID + "/posts?size=2&page=0");
        List<String> hotBoardPostNames = parsePostTitles(hotBoardResponse);
        ExtractableResponse<Response> boardResponse = httpGet("/boards/" + FREE_BOARD_ID + "/posts?size=2&page=0");
        List<String> boardPostNames = parsePostTitles(boardResponse);

        // then
        assertAll(
                () -> assertThat(hotBoardResponse.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(hotBoardPostNames).isEqualTo(List.of("제목")),
                () -> assertThat(boardResponse.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(boardPostNames).isEqualTo(List.of("제목"))
        );
    }

    @DisplayName("Hot 게시판에도 글이 등록된 후 좋아요를 한 유저가 좋아요 취소를 해도 Hot 게시판에는 유지가 된다.")
    @Test
    void keepPostInHotBoardAfterCancelLIke() {
        // given
        String token1 = getChrisToken();
        String token2 = httpPost(new LoginRequest("josh", "Abcd123!@"), "/login").header(AUTHORIZATION);
        String token3 = httpPost(new LoginRequest("thor", "Abcd123!@"), "/login").header(AUTHORIZATION);
        String token4 = httpPost(new LoginRequest("hunch", "Abcd123!@"), "/login").header(AUTHORIZATION);
        String token5 = httpPost(new LoginRequest("east", "Abcd123!@"), "/login").header(AUTHORIZATION);
        List<String> tokens = List.of(token1, token2, token3, token4, token5);

        addNewPost();

        for (String token : tokens) {
            httpPutWithAuthorization("/posts/1/like", token);
        }

        // when
        httpPutWithAuthorization("/posts/1/like", token1);

        ExtractableResponse<Response> hotBoardResponse = httpGet("/boards/" + HOT_BOARD_ID + "/posts?size=2&page=0");
        List<String> hotBoardPostNames = parsePostTitles(hotBoardResponse);

        ExtractableResponse<Response> boardResponse = httpGet("/boards/" + FREE_BOARD_ID + "/posts?size=2&page=0");
        List<String> boardPostNames = parsePostTitles(boardResponse);

        // then
        assertAll(
                () -> assertThat(hotBoardResponse.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(hotBoardPostNames).isEqualTo(List.of("제목")),
                () -> assertThat(boardResponse.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(boardPostNames).isEqualTo(List.of("제목"))
        );
    }

    @DisplayName("Hot 게시판에도 글이 등록된 후 원본 게시글을 작성한 사용자가 글을 삭제하면 삭제된다.")
    @Test
    void deletePostInHotBoardAfterUserDeleteOriginalPost() {
        // given
        String token1 = getChrisToken();
        String token2 = httpPost(new LoginRequest("josh", "Abcd123!@"), "/login").header(AUTHORIZATION);
        String token3 = httpPost(new LoginRequest("thor", "Abcd123!@"), "/login").header(AUTHORIZATION);
        String token4 = httpPost(new LoginRequest("hunch", "Abcd123!@"), "/login").header(AUTHORIZATION);
        String token5 = httpPost(new LoginRequest("east", "Abcd123!@"), "/login").header(AUTHORIZATION);
        List<String> tokens = List.of(token1, token2, token3, token4, token5);

        addNewPost();

        for (String token : tokens) {
            httpPutWithAuthorization("/posts/1/like", token);
        }

        // when
        httpDeleteWithAuthorization("/posts/1", token1);

        ExtractableResponse<Response> hotBoardResponse = httpGet("/boards/" + HOT_BOARD_ID + "/posts?size=2&page=0");
        List<String> hotBoardPostNames = parsePostTitles(hotBoardResponse);

        ExtractableResponse<Response> boardResponse = httpGet("/boards/" + FREE_BOARD_ID + "/posts?size=2&page=0");
        List<String> boardPostNames = parsePostTitles(boardResponse);

        // then
        assertAll(
                () -> assertThat(hotBoardResponse.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(hotBoardPostNames).hasSize(0),
                () -> assertThat(boardResponse.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(boardPostNames).hasSize(0)
        );
    }

    private List<BoardContentElement> getBoardContentElements(ExtractableResponse<Response> response) {
        return response.body().jsonPath().getList("boards", BoardContentElement.class);
    }

    private List<String> parsePostTitles(ExtractableResponse<Response> response) {
        return response.jsonPath()
                .getObject(".", PostsResponse.class)
                .getPosts()
                .stream()
                .map(PostsElementResponse::getTitle)
                .collect(Collectors.toList());
    }
}
