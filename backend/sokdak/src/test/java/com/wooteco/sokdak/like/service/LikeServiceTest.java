package com.wooteco.sokdak.like.service;

import static com.wooteco.sokdak.util.fixture.MemberFixture.*;
import static com.wooteco.sokdak.util.fixture.PostFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.wooteco.sokdak.like.dto.LikeFlipResponse;
import com.wooteco.sokdak.member.domain.Member;
import com.wooteco.sokdak.member.repository.MemberRepository;
import com.wooteco.sokdak.post.domain.Post;
import com.wooteco.sokdak.post.repository.PostRepository;
import com.wooteco.sokdak.util.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class LikeServiceTest extends IntegrationTest {

    @Autowired
    private LikeService likeService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Member member;
    private Post post;

    @BeforeEach
    void setUp() {
        member = Member.builder()
                .username(VALID_USERNAME)
                .password(VALID_PASSWORD)
                .nickname(VALID_NICKNAME)
                .build();
        memberRepository.save(member);

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
        LikeFlipResponse putLikeResponse = likeService.flipLikePost(post.getId(), AUTH_INFO);

        assertAll(
                () -> assertThat(putLikeResponse.isLike()).isTrue(),
                () -> assertThat(putLikeResponse.getLikeCount()).isEqualTo(1)
        );
    }

    @DisplayName("처음 좋아요를 누른 상태")
    @Test
    void flipLike_delete() {
        likeService.flipLikePost(post.getId(), AUTH_INFO);

        LikeFlipResponse putLikeResponse2 = likeService.flipLikePost(post.getId(), AUTH_INFO);

        assertAll(
                () -> assertThat(putLikeResponse2.isLike()).isFalse(),
                () -> assertThat(putLikeResponse2.getLikeCount()).isZero()
        );
    }
}
