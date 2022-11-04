package com.wooteco.sokdak.util.fixture;

import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.httpPostWithAuthorization;
import static com.wooteco.sokdak.util.fixture.TokenFixture.getChrisToken;

import com.wooteco.sokdak.comment.dto.NewCommentRequest;
import com.wooteco.sokdak.comment.dto.NewReplyRequest;

public class CommentFixture {

    public static final String VALID_COMMENT_MESSAGE = "댓글";
    public static final String VALID_REPLY_COMMENT = "대댓글";

    public static final NewCommentRequest NON_ANONYMOUS_COMMENT_REQUEST
            = new NewCommentRequest(VALID_COMMENT_MESSAGE, false);
    public static final NewCommentRequest ANONYMOUS_COMMENT_REQUEST
            = new NewCommentRequest(VALID_COMMENT_MESSAGE, true);
    public static final NewCommentRequest APPLICANT_COMMENT_REQUEST
            = new NewCommentRequest(VALID_COMMENT_MESSAGE, false);

    public static final NewReplyRequest NON_ANONYMOUS_REPLY_REQUEST =
            new NewReplyRequest(VALID_REPLY_COMMENT, false);
    public static final NewReplyRequest ANONYMOUS_REPLY_REQUEST =
            new NewReplyRequest(VALID_REPLY_COMMENT, true);

    public static Long addNewCommentInPost(Long postId) {
        return Long.parseLong(
                httpPostWithAuthorization(NON_ANONYMOUS_COMMENT_REQUEST, "/posts/" + postId + "/comments",
                        getChrisToken())
                        .header("Location").split("/comments/")[1]);
    }
}
