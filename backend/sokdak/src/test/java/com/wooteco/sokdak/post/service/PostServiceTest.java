package com.wooteco.sokdak.post.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.data.domain.Sort.Direction.DESC;

import com.wooteco.sokdak.auth.dto.AuthInfo;
import com.wooteco.sokdak.post.domain.Post;
import com.wooteco.sokdak.post.dto.NewPostRequest;
import com.wooteco.sokdak.post.dto.PostResponse;
import com.wooteco.sokdak.post.dto.PostUpdateRequest;
import com.wooteco.sokdak.post.dto.PostsResponse;
import com.wooteco.sokdak.post.exception.PostNotFoundException;
import com.wooteco.sokdak.post.repository.PostRepository;
import java.util.List;
import java.util.Optional;
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

    private static final Post POST = Post.builder()
            .title("제목")
            .content("본문")
            .build();

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @DisplayName("글 작성 기능")
    @Test
    void addPost() {
        NewPostRequest newPostRequest = new NewPostRequest("제목", "본문");
        AuthInfo authInfo = new AuthInfo(1L);

        Long postId = postService.addPost(newPostRequest, authInfo);
        Post actual = postRepository.findById(postId).orElseThrow();

        assertAll(
                () -> assertThat(actual.getTitle()).isEqualTo(newPostRequest.getTitle()),
                () -> assertThat(actual.getContent()).isEqualTo(newPostRequest.getContent()),
                () -> assertThat(actual.getMember().getId()).isEqualTo(1L),
                () -> assertThat(actual.getCreatedAt()).isNotNull()
        );
    }

    @DisplayName("글 조회 기능")
    @Test
    void findPost() {
        Long savedPostId = postRepository.save(POST).getId();

        PostResponse response = postService.findPost(savedPostId);

        assertAll(
                () -> assertThat(response.getTitle()).isEqualTo(POST.getTitle()),
                () -> assertThat(response.getContent()).isEqualTo(POST.getContent()),
                () -> assertThat(response.getCreatedAt()).isNotNull()
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
        Post post2 = Post.builder()
                .title("제목2")
                .content("본문2")
                .build();
        Post post3 = Post.builder()
                .title("제목3")
                .content("본문3")
                .build();
        postRepository.save(POST);
        postRepository.save(post2);
        postRepository.save(post3);
        Pageable pageable = PageRequest.of(0, 2, DESC, "createdAt");

        PostsResponse postsResponse = postService.findPosts(pageable);

        assertAll(
                () -> assertThat(postsResponse.getPosts()).hasSize(2),
                () -> assertThat(postsResponse.getPosts()).usingRecursiveComparison()
                        .ignoringFields("id", "createdAt")
                        .isEqualTo(List.of(PostResponse.from(post3), PostResponse.from(post2)))
        );
    }

    @DisplayName("게시글 수정 기능")
    @Test
    void updatePost() {
        Long postId = postRepository.save(POST).getId();
        PostUpdateRequest postUpdateRequest = new PostUpdateRequest("변경된 제목", "변경된 본문");

        postService.updatePost(postId, postUpdateRequest);

        Post foundPost = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);
        assertAll(
                () -> assertThat(foundPost.getTitle()).isEqualTo("변경된 제목"),
                () -> assertThat(foundPost.getContent()).isEqualTo("변경된 본문")
        );
    }

    @DisplayName("게시글 삭제 기능")
    @Test
    void deletePost() {
        Long postId = postRepository.save(POST).getId();

        postService.deletePost(postId);

        Optional<Post> foundPost = postRepository.findById(postId);
        assertThat(foundPost).isEmpty();
    }
}
