package com.wooteco.sokdak.hashtag.controller;

import static com.wooteco.sokdak.util.fixture.MemberFixture.AUTH_INFO;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.matches;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;

import com.wooteco.sokdak.hashtag.exception.HashtagNotFoundException;
import com.wooteco.sokdak.post.dto.PostsElementResponse;
import com.wooteco.sokdak.post.dto.PostsResponse;
import com.wooteco.sokdak.util.ControllerTest;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

class HashtagControllerTest extends ControllerTest {

    @BeforeEach
    void setUpArgumentResolver() {
        doReturn(true)
                .when(authInterceptor)
                .preHandle(any(), any(), any());
        doReturn(AUTH_INFO)
                .when(authenticationPrincipalArgumentResolver)
                .resolveArgument(any(), any(), any(), any());
    }

    @DisplayName("해시태그로 검색 시 200 반환")
    @Test
    void findPostsWithHashtag() {
        PostsElementResponse postsElementResponse1 = PostsElementResponse.builder()
                .id(3L)
                .title("제목")
                .content("내용")
                .createdAt(LocalDateTime.MIN)
                .modified(false)
                .commentCount(2)
                .likeCount(5)
                .build();
        PostsElementResponse postsElementResponse2 = PostsElementResponse.builder()
                .id(2L)
                .title("제목2")
                .content("내용2")
                .createdAt(LocalDateTime.MAX)
                .modified(false)
                .commentCount(4)
                .likeCount(10)
                .build();

        doReturn(new PostsResponse(List.of(postsElementResponse1, postsElementResponse2), true))
                .when(hashtagService)
                .findPostsWithHashtag(matches("속닥"), any());

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/posts?hashtag=속닥&size=5&page=0")
                .then().log().all()
                .apply(document("hashtag/search/success"))
                .statusCode(HttpStatus.OK.value());
    }

    @DisplayName("해시태그로 검색 시 없는 해시태그이면 404 반환")
    @Test
    void findPostsWithHashtags_Exception_NoHashtag() {
        doThrow(new HashtagNotFoundException())
                .when(hashtagService)
                .findPostsWithHashtag(matches("없는태그"), any());

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/posts?hashtag=없는태그&size=5&page=0")
                .then().log().all()
                .apply(document("hashtag/search/success"))
                .statusCode(HttpStatus.NOT_FOUND.value());
    }
}
