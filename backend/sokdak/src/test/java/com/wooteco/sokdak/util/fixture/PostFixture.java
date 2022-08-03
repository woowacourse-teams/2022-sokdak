package com.wooteco.sokdak.util.fixture;

import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.httpPostWithAuthorization;
import static com.wooteco.sokdak.util.fixture.ReportFixture.getToken;

import com.wooteco.sokdak.post.dto.NewPostRequest;
import java.util.Collections;

public class PostFixture {

    public static final String CANNOT_CREATE_POST_URI = "/boards/1/posts";
    public static final String CREATE_POST_URI = "/boards/2/posts";
    public static final String BOARD_ID_POST_CREATED = "2";

    public static final String VALID_POST_TITLE = "제목";
    public static final String VALID_POST_CONTENT = "본문";

    public static final String UPDATED_POST_TITLE = "변경된 제목";
    public static final String UPDATED_POST_CONTENT = "변경된 본문";

    public static final String INVALID_POST_TITLE = "A".repeat(51);
    public static final String INVALID_POST_CONTENT = "A".repeat(5001);

    public static Long addPostAndGetPostId() {
        NewPostRequest newPostRequest = new NewPostRequest(VALID_POST_TITLE, VALID_POST_CONTENT,
                Collections.emptyList());
        return Long.parseLong(httpPostWithAuthorization(newPostRequest, CREATE_POST_URI, getToken("chris", "Abcd123!@"))
                .header("Location").split("/posts/")[1]);
    }
}
