package com.wooteco.sokdak.post.service;

import static com.wooteco.sokdak.util.fixture.MemberFixture.AUTH_INFO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.data.domain.Sort.Direction.DESC;

import com.wooteco.sokdak.auth.dto.AuthInfo;
import com.wooteco.sokdak.comment.dto.NewCommentRequest;
import com.wooteco.sokdak.comment.service.CommentService;
import com.wooteco.sokdak.member.domain.Member;
import com.wooteco.sokdak.member.exception.MemberNotFoundException;
import com.wooteco.sokdak.member.repository.MemberRepository;
import com.wooteco.sokdak.post.domain.Post;
import com.wooteco.sokdak.post.dto.NewPostRequest;
import com.wooteco.sokdak.post.dto.PostDetailResponse;
import com.wooteco.sokdak.post.dto.PostUpdateRequest;
import com.wooteco.sokdak.post.dto.PostsElementResponse;
import com.wooteco.sokdak.post.dto.PostsResponse;
import com.wooteco.sokdak.post.exception.PostNotFoundException;
import com.wooteco.sokdak.post.repository.PostRepository;
import com.wooteco.sokdak.util.fixture.BoardFixture;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
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
    private MemberRepository memberRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentService commentService;

    private Post post;

    @BeforeEach
    public void setUp() {
        Member member = memberRepository.findById(1L)
                .orElseThrow(MemberNotFoundException::new);
        post = Post.builder()
                .title("제목")
                .content("본문")
                .member(member)
                .likes(new ArrayList<>())
                .build();
    }

    @DisplayName("특정 게시판에 글 작성 기능")
    @Test
    void addPost() {
        NewPostRequest newPostRequest = new NewPostRequest("제목", "본문", Collections.emptyList());

        Long postId = postService.addPost(1L, newPostRequest, AUTH_INFO);
        Post actual = postRepository.findById(postId).orElseThrow();

        assertAll(
                () -> assertThat(actual.getTitle()).isEqualTo(newPostRequest.getTitle()),
                () -> assertThat(actual.getContent()).isEqualTo(newPostRequest.getContent()),
                () -> assertThat(actual.getMember().getId()).isEqualTo(1L),
                () -> assertThat(actual.getCreatedAt()).isNotNull(),
                () -> assertThat(actual.getPostBoards().get(0).getBoard().getTitle()).isNotNull()
        );
    }

    @DisplayName("본인이 작성한 게시글 조회 기능")
    @Test
    void findPost_Session_MyPost() {
        Long savedPostId = postRepository.save(post).getId();

        PostDetailResponse response = postService.findPost(savedPostId, AUTH_INFO);

        assertAll(
                () -> assertThat(response.getTitle()).isEqualTo(post.getTitle()),
                () -> assertThat(response.getContent()).isEqualTo(post.getContent()),
                () -> assertThat(response.isAuthorized()).isTrue(),
                () -> assertThat(response.isModified()).isFalse(),
                () -> assertThat(response.getCreatedAt()).isNotNull()
        );
    }


    @DisplayName("로그인을 하고, 다른 회원이 작성한 게시글 조회 기능")
    @Test
    void findPost_Session_OtherPost() {
        Long savedPostId = postRepository.save(post).getId();

        PostDetailResponse response = postService.findPost(savedPostId, new AuthInfo(2L));

        assertAll(
                () -> assertThat(response.getTitle()).isEqualTo(post.getTitle()),
                () -> assertThat(response.getContent()).isEqualTo(post.getContent()),
                () -> assertThat(response.isAuthorized()).isFalse(),
                () -> assertThat(response.isModified()).isFalse(),
                () -> assertThat(response.getCreatedAt()).isNotNull()
        );
    }

    @DisplayName("로그인 없이,게시글 조회 기능")
    @Test
    void findPost_NoSession() {
        Long savedPostId = postRepository.save(post).getId();

        PostDetailResponse response = postService.findPost(savedPostId, new AuthInfo(null));

        assertAll(
                () -> assertThat(response.getTitle()).isEqualTo(post.getTitle()),
                () -> assertThat(response.getContent()).isEqualTo(post.getContent()),
                () -> assertThat(response.isAuthorized()).isFalse(),
                () -> assertThat(response.isModified()).isFalse(),
                () -> assertThat(response.getCreatedAt()).isNotNull()
        );
    }

    @DisplayName("존재하지 않는 id로 글 조회 시 예외 발생")
    @Test
    void findPost_Exception() {
        Long invalidPostId = 9999L;

        assertThatThrownBy(() -> postService.findPost(invalidPostId, AUTH_INFO))
                .isInstanceOf(PostNotFoundException.class);
    }

    @DisplayName("특정 페이지 글 목록 조회 기능")
    @Test
    void findPosts() {
        Post post2 = Post.builder()
                .title("제목2")
                .content("본문2")
                .likes(new ArrayList<>())
                .comments(new ArrayList<>())
                .build();
        Post post3 = Post.builder()
                .title("제목3")
                .content("본문3")
                .likes(new ArrayList<>())
                .comments(new ArrayList<>())
                .build();
        postRepository.save(post);
        postRepository.save(post2);
        postRepository.save(post3);
        Pageable pageable = PageRequest.of(0, 2, DESC, "createdAt");

        PostsResponse postsResponse = postService.findPosts(pageable);

        assertAll(
                () -> assertThat(postsResponse.getPosts()).hasSize(2),
                () -> assertThat(postsResponse.getPosts()).usingRecursiveComparison()
                        .ignoringFields("id", "createdAt")
                        .isEqualTo(List.of(PostsElementResponse.from(post3), PostsElementResponse.from(post2)))
        );
    }

    @DisplayName("게시글 수정 기능")
    @Test
    void updatePost() {
        Long postId = postRepository.save(post).getId();
        PostUpdateRequest postUpdateRequest = new PostUpdateRequest("변경된 제목", "변경된 본문", Collections.emptyList());

        postService.updatePost(postId, postUpdateRequest, AUTH_INFO);

        Post foundPost = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);
        postRepository.flush();
        assertAll(
                () -> assertThat(foundPost.getTitle()).isEqualTo("변경된 제목"),
                () -> assertThat(foundPost.getContent()).isEqualTo("변경된 본문"),
                () -> assertThat(foundPost.isModified()).isTrue()
        );
    }

    @DisplayName("게시글 삭제 기능")
    @Test
    void deletePost() {
        Long postId = postRepository.save(post).getId();

        postService.deletePost(postId, AUTH_INFO);

        Optional<Post> foundPost = postRepository.findById(postId);
        assertThat(foundPost).isEmpty();
    }

    @DisplayName("댓글이 있는 게시글 삭제")
    @Test
    void deletePostWithComment() {
        postRepository.save(post).getId();
        NewCommentRequest newCommentRequest = new NewCommentRequest("댓글", true);
        commentService.addComment(post.getId(), newCommentRequest, AUTH_INFO);

        postService.deletePost(post.getId(), AUTH_INFO);

        Optional<Post> deletePost = postRepository.findById(post.getId());
        assertThat(deletePost).isEmpty();
    }
}
