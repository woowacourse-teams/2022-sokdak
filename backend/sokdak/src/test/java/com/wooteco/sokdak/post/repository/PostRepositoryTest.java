package com.wooteco.sokdak.post.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.wooteco.sokdak.config.JPAConfig;
import com.wooteco.sokdak.post.domain.Post;
import com.wooteco.sokdak.util.RepositoryTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

@Import(JPAConfig.class)
class PostRepositoryTest extends RepositoryTest {

    @Autowired
    private PostRepository postRepository;

    private Post post1;
    private Post post2;
    private Post post3;
    private Post post4;
    private Post post5;

    @BeforeEach
    void setUp() {
        post1 = Post.builder()
                .title("제목1")
                .content("본문1")
                .member(member1)
                .build();
        post2 = Post.builder()
                .title("제목2")
                .content("본문2")
                .member(member1)
                .build();
        post3 = Post.builder()
                .title("제목3")
                .content("본문3")
                .member(member1)
                .build();
        post4 = Post.builder()
                .title("제목4")
                .content("본문4")
                .member(member1)
                .build();
        post5 = Post.builder()
                .title("제목5")
                .content("본문5")
                .member(member1)
                .build();
        postRepository.save(post1);
        postRepository.save(post2);
        postRepository.save(post3);
        postRepository.save(post4);
        postRepository.save(post5);
    }

    @DisplayName("특정 페이지의 게시글들을 가져오는지 확인한다.")
    @Test
    void findWithPagination() {
        Slice<Post> result = postRepository.findSliceBy(PageRequest.of(0, 6));

        assertAll(
                () -> assertThat(result).containsExactly(post1, post2, post3, post4, post5),
                () -> assertThat(result.isLast()).isTrue()
        );
    }

    @DisplayName("특정 페이지의 게시글들을 가져오는지 확인한다.")
    @Test
    void findWithPagination2() {
        Slice<Post> result = postRepository.findSliceBy(PageRequest.of(1, 2));

        assertAll(
                () -> assertThat(result).containsExactly(post3, post4),
                () -> assertThat(result.isLast()).isFalse()
        );
    }

    @DisplayName("게시글, 회원 매핑 확인")
    @Test
    void findPostWithMember() {
        Post foundPost = postRepository.findById(post1.getId())
                .orElseThrow();

        assertThat(foundPost.getMember()).isNotNull();
    }

    @DisplayName("특점 멤버의 글을 시간순으로 가져오는지 확인")
    @Test
    void findPostsByMember() {
        Page<Post> result = postRepository.findPostsByMemberOrderByCreatedAtDesc(PageRequest.of(0, 2), member1);

        assertAll(
                () -> assertThat(result.getContent()).containsExactly(post5, post4),
                () -> assertThat(result.getTotalPages()).isEqualTo(3)
        );
    }
}
