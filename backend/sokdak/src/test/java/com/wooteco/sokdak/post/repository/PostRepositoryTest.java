package com.wooteco.sokdak.post.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.wooteco.sokdak.post.domain.Post;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

@DataJpaTest
class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @DisplayName("특정 페이지의 게시글들을 가져오는지 확인한다.")
    @Test
    void findWithPagination() {
        Post post1 = Post.builder()
                .title("제목1")
                .content("본문1")
                .build();
        Post post2 = Post.builder()
                .title("제목2")
                .content("본문2")
                .build();
        postRepository.save(post1);
        postRepository.save(post2);

        Slice<Post> result = postRepository.findSliceBy(PageRequest.of(0, 3));

        assertAll(
                () -> assertThat(result).containsExactly(post1, post2),
                () -> assertThat(result.isLast()).isTrue()
        );
    }

    @DisplayName("특정 페이지의 게시글들을 가져오는지 확인한다.")
    @Test
    void findWithPagination2() {
        Post post1 = Post.builder()
                .title("제목1")
                .content("본문1")
                .build();
        Post post2 = Post.builder()
                .title("제목2")
                .content("본문2")
                .build();
        Post post3 = Post.builder()
                .title("제목3")
                .content("본문3")
                .build();
        Post post4 = Post.builder()
                .title("제목4")
                .content("본문4")
                .build();
        Post post5 = Post.builder()
                .title("제목5")
                .content("본문5")
                .build();
        postRepository.save(post1);
        postRepository.save(post2);
        postRepository.save(post3);
        postRepository.save(post4);
        postRepository.save(post5);

        Slice<Post> result = postRepository.findSliceBy(PageRequest.of(1, 2));

        assertAll(
                () -> assertThat(result).containsExactly(post3, post4),
                () -> assertThat(result.isLast()).isFalse()
        );
    }
}
