package com.wooteco.sokdak.util.fixture;

import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.getChrisToken;
import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.httpPostWithAuthorization;

import com.wooteco.sokdak.comment.dto.NewCommentRequest;

public class CommentFixture {

    public static final String VALID_COMMENT_MESSAGE = "댓글";
    public static final String VALID_REPLY_MESSAGE = "대댓글";

    public static final NewCommentRequest NEW_ANONYMOUS_COMMENT_REQUEST
            = new NewCommentRequest(VALID_COMMENT_MESSAGE, true);
    public static final NewCommentRequest NEW_COMMENT_REQUEST
            = new NewCommentRequest(VALID_COMMENT_MESSAGE, false);
    public static final NewCommentRequest NEW_REPLY_REQUEST =
            new NewCommentRequest(VALID_COMMENT_MESSAGE, false);

    public static Long addCommentAndGetCommentId(Long postId) {
        return Long.parseLong(httpPostWithAuthorization(NEW_COMMENT_REQUEST,
                "/posts/" + postId + "/comments", getChrisToken())
                .header("Location").split("/comments/")[1]);
    }
}
