package com.wooteco.sokdak.post.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.wooteco.sokdak.post.domain.Post;
import com.wooteco.sokdak.post.dto.NewPostRequest;
import com.wooteco.sokdak.post.dto.PostResponse;
import com.wooteco.sokdak.post.dto.PostsResponse;
import com.wooteco.sokdak.post.exception.PostNotFoundException;
import com.wooteco.sokdak.post.repository.PostRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @DisplayName("글 작성 기능")
    @Test
    void addPost() {
        NewPostRequest newPostRequest = new NewPostRequest("제목", "본문");

        Long postId = postService.addPost(newPostRequest);
        Post actual = postRepository.findById(postId).orElseThrow();

        assertAll(
                () -> assertThat(actual.getTitle()).isEqualTo(newPostRequest.getTitle()),
                () -> assertThat(actual.getContent()).isEqualTo(newPostRequest.getContent()),
                () -> assertThat(actual.getCreatedAt()).isNotNull()
        );
    }

    @DisplayName("글 조회 기능")
    @Test
    void findPost() {
        Post post = Post.builder()
                .title("제목")
                .content("본문")
                .build();
        Long savedPostId = postRepository.save(post).getId();

        PostResponse response = postService.findPost(savedPostId);

        assertAll(
                () -> assertThat(response.getTitle()).isEqualTo(post.getTitle()),
                () -> assertThat(response.getContent()).isEqualTo(post.getContent()),
                () -> assertThat(response.getLocalDate()).isNotNull()
        );
    }

    @DisplayName("존재하지 않는 id로 글 조회 시 예외 발생")
    @Test
    void findPost_Exception() {
        Long invalidPostId = 9999L;

        assertThatThrownBy(() -> postService.findPost(invalidPostId))
                .isInstanceOf(PostNotFoundException.class);
    }

    @DisplayName("특정 페이지 글 목록 조회 기능")
    @Test
    void findPosts() {
        Post post = Post.builder()
                .title("제목")
                .content("본문")
                .build();
        postRepository.save(post);
        Pageable pageable = PageRequest.of(0, 3);

        PostsResponse postsResponse = postService.findPosts(pageable);

        assertAll(
                () -> assertThat(postsResponse.getPosts()).hasSize(1),
                () -> assertThat(postsResponse.getPosts()).usingRecursiveComparison()
                        .ignoringFields("id", "localDate")
                        .isEqualTo(List.of(PostResponse.from(post)))
        );
    }
}
