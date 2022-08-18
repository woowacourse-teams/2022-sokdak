package com.wooteco.sokdak.util.fixture;

import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.httpPostWithAuthorization;
import static com.wooteco.sokdak.util.fixture.MemberFixture.getChrisToken;

import com.wooteco.sokdak.comment.dto.NewCommentRequest;

public class CommentFixture {

    public static final String VALID_COMMENT_MESSAGE = "댓글";

    public static final NewCommentRequest NEW_COMMENT_REQUEST
            = new NewCommentRequest(VALID_COMMENT_MESSAGE, false);
    public static final NewCommentRequest NEW_REPLY_REQUEST =
            new NewCommentRequest(VALID_COMMENT_MESSAGE, false);

    public static Long addNewCommentInPost(Long postId) {
        return Long.parseLong(
                httpPostWithAuthorization(NEW_COMMENT_REQUEST, "/posts/" + postId + "/comments", getChrisToken())
                        .header("Location").split("/comments/")[1]);
    }
}
