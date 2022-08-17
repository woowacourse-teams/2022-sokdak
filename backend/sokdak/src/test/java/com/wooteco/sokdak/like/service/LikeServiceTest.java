package com.wooteco.sokdak.like.service;

import static com.wooteco.sokdak.util.fixture.PostFixture.VALID_POST_CONTENT;
import static com.wooteco.sokdak.util.fixture.PostFixture.VALID_POST_TITLE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.wooteco.sokdak.like.dto.LikeFlipResponse;
import com.wooteco.sokdak.post.domain.Post;
import com.wooteco.sokdak.post.repository.PostRepository;
import com.wooteco.sokdak.util.ServiceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class LikeServiceTest extends ServiceTest {

    @Autowired
    private LikeService likeService;

    @Autowired
    private PostRepository postRepository;

    private Post post;

    @BeforeEach
    void setUp() {
        post = Post.builder()
                .title(VALID_POST_TITLE)
                .content(VALID_POST_CONTENT)
                .member(member)
                .build();

        postRepository.save(post);
    }

    @DisplayName("좋아요 등록")
    @Test
    void flipLike_create() {
        LikeFlipResponse putLikeResponse = likeService.flipLike(post.getId(), AUTH_INFO);

        assertAll(
                () -> assertThat(putLikeResponse.isLike()).isTrue(),
                () -> assertThat(putLikeResponse.getLikeCount()).isEqualTo(1)
        );
    }

    @DisplayName("처음 좋아요를 누른 상태")
    @Test
    void flipLike_delete() {
        likeService.flipLike(post.getId(), AUTH_INFO);

        LikeFlipResponse putLikeResponse2 = likeService.flipLike(post.getId(), AUTH_INFO);

        assertAll(
                () -> assertThat(putLikeResponse2.isLike()).isFalse(),
                () -> assertThat(putLikeResponse2.getLikeCount()).isZero()
        );
    }
}
