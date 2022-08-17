package com.wooteco.sokdak.util.fixture;

import static com.wooteco.sokdak.util.fixture.HttpMethodFixture.httpPostWithAuthorization;
import static com.wooteco.sokdak.util.fixture.MemberFixture.getChrisToken;

import com.wooteco.sokdak.post.dto.NewPostRequest;
import com.wooteco.sokdak.post.dto.PostsElementResponse;
import java.time.LocalDateTime;
import java.util.List;

public class PostFixture {

    public static final String CANNOT_CREATE_POST_URI = "/boards/1/posts";
    public static final String CREATE_POST_URI = "/boards/2/posts";
    public static final String BOARD_ID_POST_CREATED = "2";

    public static final String VALID_POST_TITLE = "제목";
    public static final String VALID_POST_CONTENT = "본문";

    public static final String UPDATED_POST_TITLE = "변경된 제목";
    public static final String UPDATED_POST_CONTENT = "변경된 본문";

    public static final Long BLOCKED_COUNT = 5L;
    public static final String SERIAL_NUMBER = "asd23456";

    public static final PostsElementResponse POSTS_ELEMENT_RESPONSE_1 = PostsElementResponse.builder()
            .id(1L)
            .title("제목")
            .content("본문")
            .createdAt(LocalDateTime.now())
            .likeCount(0)
            .commentCount(0)
            .modified(false)
            .build();
    public static final PostsElementResponse POSTS_ELEMENT_RESPONSE_2 = PostsElementResponse.builder()
            .id(2L)
            .title("제목2")
            .content("본문2")
            .createdAt(LocalDateTime.now())
            .likeCount(0)
            .commentCount(0)
            .modified(false)
            .build();

    public static Long addNewPost() {
        NewPostRequest newPostRequest = new NewPostRequest(VALID_POST_TITLE, VALID_POST_CONTENT, false,
                List.of("태그1", "태그2"));
        return Long.parseLong(
                httpPostWithAuthorization(newPostRequest, CREATE_POST_URI, getChrisToken())
                        .header("Location").split("/posts/")[1]);
    }
}
