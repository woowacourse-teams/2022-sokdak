package com.wooteco.sokdak.util.fixture;

import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.httpPostWithAuthorization;
import static com.wooteco.sokdak.util.fixture.MemberFixture.CHRIS;
import static com.wooteco.sokdak.util.fixture.MemberFixture.JOSH;
import static com.wooteco.sokdak.util.fixture.PostFixture.CHRIS_POST;
import static com.wooteco.sokdak.util.fixture.TokenFixture.getChrisToken;

import com.wooteco.sokdak.comment.domain.Comment;
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

    public static final Comment JOSH_COMMENT = Comment.builder()
            .member(JOSH)
            .post(CHRIS_POST)
            .message(VALID_COMMENT_MESSAGE)
            .nickname(MemberFixture.VALID_NICKNAME_TEXT)
            .build();

    public static final Comment CHRIS_COMMENT = Comment.builder()
            .member(CHRIS)
            .post(CHRIS_POST)
            .message(VALID_COMMENT_MESSAGE)
            .nickname(MemberFixture.VALID_NICKNAME_TEXT)
            .build();

    public static final Comment CHRIS_REPLY = Comment.builder()
            .member(CHRIS)
            .post(CHRIS_POST)
            .message(VALID_COMMENT_MESSAGE)
            .parent(JOSH_COMMENT)
            .nickname(MemberFixture.VALID_NICKNAME_TEXT)
            .build();

    public static final Comment JOSH_REPLY = Comment.builder()
            .member(JOSH)
            .post(CHRIS_POST)
            .message(VALID_COMMENT_MESSAGE)
            .parent(JOSH_COMMENT)
            .nickname(MemberFixture.VALID_NICKNAME_TEXT)
            .build();

    public static Long addNewCommentInPost(Long postId) {
        return Long.parseLong(
                httpPostWithAuthorization(NON_ANONYMOUS_COMMENT_REQUEST, "/posts/" + postId + "/comments",
                        getChrisToken())
                        .header("Location").split("/comments/")[1]);
    }
}
