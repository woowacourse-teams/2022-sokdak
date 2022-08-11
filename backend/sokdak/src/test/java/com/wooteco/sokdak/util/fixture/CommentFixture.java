package com.wooteco.sokdak.util.fixture;

import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.getToken;
import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.httpPostWithAuthorization;

import com.wooteco.sokdak.comment.dto.NewCommentRequest;

public class CommentFixture {

    public static final String VALID_COMMENT_MESSAGE = "본문";

    public static final NewCommentRequest NEW_ANONYMOUS_COMMENT_REQUEST
            = new NewCommentRequest(VALID_COMMENT_MESSAGE, true);
    public static final NewCommentRequest NEW_COMMENT_REQUEST
            = new NewCommentRequest(VALID_COMMENT_MESSAGE, false);

    public static Long addCommentAndGetCommentId(Long postId) {
        return Long.parseLong(httpPostWithAuthorization(NEW_COMMENT_REQUEST,
                "/posts/" + postId + "/comments", getToken())
                .header("Location").split("/comments/")[1]);
    }
}
