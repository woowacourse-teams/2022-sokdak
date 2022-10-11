package com.wooteco.sokdak.util.fixture;

import static com.wooteco.sokdak.util.fixture.BoardFixture.*;
import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.httpPostWithAuthorization;
import static com.wooteco.sokdak.util.fixture.MemberFixture.getChrisToken;

import com.wooteco.sokdak.post.dto.NewPostRequest;
import java.util.List;

public class PostFixture {

    public static final NewPostRequest NEW_POST_REQUEST =
            new NewPostRequest(FREE_BOARD_ID, "제목", "본문", false, List.of("태그1", "태그2"));
    public static final NewPostRequest NEW_POST_REQUEST2 =
            new NewPostRequest(FREE_BOARD_ID, "제목2", "본문2", false, List.of("태그1", "태그2"));

    public static final String FREE_BOARD_POST_URI = "/boards/2/posts";

    public static final String VALID_POST_TITLE = "제목";
    public static final String VALID_POST_CONTENT = "본문";
    public static final String VALID_POST_WRITER_NICKNAME = "작성자 닉네임";

    public static final String UPDATED_POST_TITLE = "변경된 제목";
    public static final String UPDATED_POST_CONTENT = "변경된 본문";

    public static final Long BLOCKED_COUNT = 5L;
    public static final String SERIAL_NUMBER = "asd23456";

    public static Long addNewPost() {
        NewPostRequest newPostRequest = new NewPostRequest(FREE_BOARD_ID, VALID_POST_TITLE, VALID_POST_CONTENT, false,
                List.of("태그1", "태그2"));
        return Long.parseLong(
                httpPostWithAuthorization(newPostRequest, FREE_BOARD_POST_URI, getChrisToken())
                        .header("Location").split("/posts/")[1]);
    }
}
