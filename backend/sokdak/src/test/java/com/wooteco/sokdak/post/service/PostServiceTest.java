package com.wooteco.sokdak.post.service;

import static com.wooteco.sokdak.member.domain.RoleType.USER;
import static com.wooteco.sokdak.util.fixture.BoardFixture.FREE_BOARD_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.data.domain.Sort.Direction.DESC;

import com.wooteco.sokdak.auth.dto.AuthInfo;
import com.wooteco.sokdak.board.domain.Board;
import com.wooteco.sokdak.board.domain.PostBoard;
import com.wooteco.sokdak.board.exception.BoardNotFoundException;
import com.wooteco.sokdak.board.repository.BoardRepository;
import com.wooteco.sokdak.board.repository.PostBoardRepository;
import com.wooteco.sokdak.comment.domain.Comment;
import com.wooteco.sokdak.comment.dto.NewCommentRequest;
import com.wooteco.sokdak.comment.repository.CommentRepository;
import com.wooteco.sokdak.comment.service.CommentService;
import com.wooteco.sokdak.hashtag.domain.Hashtag;
import com.wooteco.sokdak.hashtag.domain.PostHashtag;
import com.wooteco.sokdak.hashtag.repository.HashtagRepository;
import com.wooteco.sokdak.hashtag.repository.PostHashtagRepository;
import com.wooteco.sokdak.like.domain.Like;
import com.wooteco.sokdak.like.repository.LikeRepository;
import com.wooteco.sokdak.notification.domain.Notification;
import com.wooteco.sokdak.notification.domain.NotificationType;
import com.wooteco.sokdak.notification.repository.NotificationRepository;
import com.wooteco.sokdak.post.domain.Post;
import com.wooteco.sokdak.post.dto.MyPostsResponse;
import com.wooteco.sokdak.post.dto.NewPostRequest;
import com.wooteco.sokdak.post.dto.PostDetailResponse;
import com.wooteco.sokdak.post.dto.PostUpdateRequest;
import com.wooteco.sokdak.post.dto.PostsElementResponse;
import com.wooteco.sokdak.post.dto.PostsResponse;
import com.wooteco.sokdak.post.exception.PostNotFoundException;
import com.wooteco.sokdak.post.repository.PostRepository;
import com.wooteco.sokdak.report.domain.PostReport;
import com.wooteco.sokdak.report.repository.PostReportRepository;
import com.wooteco.sokdak.util.ServiceTest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

class PostServiceTest extends ServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostBoardRepository postBoardRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private CommentService commentService;

    @Autowired
    private HashtagRepository hashtagRepository;

    @Autowired
    private PostHashtagRepository postHashtagRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostReportRepository postReportRepository;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    private Post post;
    private Post fullSettingPost;
    private Board board;
    private Board hotBoard;
    private PostBoard postBoard;

    @BeforeEach
    public void setUp() {
        post = Post.builder()
                .title("제목")
                .content("본문")
                .writerNickname(member.getNickname())
                .member(member)
                .postLikes(new ArrayList<>())
                .comments(new ArrayList<>())
                .build();
        board = boardRepository.findById(2L)
                .orElseThrow(BoardNotFoundException::new);
        hotBoard = boardRepository.findById(1L)
                .orElseThrow(BoardNotFoundException::new);
    }

    @DisplayName("특정 게시판에 익명으로 글 작성 기능")
    @Test
    void addPost_Anonymous() {
        String tagName = "tag";
        Hashtag tag = Hashtag.builder()
                .name(tagName)
                .build();
        NewPostRequest newPostRequest = new NewPostRequest("제목", "본문", true, List.of(tagName));

        Long postId = postService.addPost(FREE_BOARD_ID, newPostRequest, AUTH_INFO);
        Post actual = postRepository.findById(postId).orElseThrow();
        List<PostHashtag> postHashtags = postHashtagRepository.findAllByPost(actual);
        List<Hashtag> hashtags = hashtagRepository.findAllByNameContains(tagName);

        assertAll(
                () -> assertThat(actual.getTitle()).isEqualTo(newPostRequest.getTitle()),
                () -> assertThat(actual.getContent()).isEqualTo(newPostRequest.getContent()),
                () -> assertThat(actual.getMember().getId()).isEqualTo(member.getId()),
                () -> assertThat(actual.getNickname()).isNotEqualTo(actual.getMember().getNickname()),
                () -> assertThat(actual.getCreatedAt()).isNotNull(),
                () -> assertThat(actual.getWritableBoard().getId()).isEqualTo(FREE_BOARD_ID),
                () -> assertThat(postHashtags).hasSize(1),
                () -> assertThat(hashtags).contains(tag)
        );
    }

    @DisplayName("특정 게시판에 기명으로 글 작성 기능")
    @Test
    void addPost_Identified() {
        NewPostRequest newPostRequest = new NewPostRequest("제목", "본문", false, Collections.emptyList());

        Long postId = postService.addPost(FREE_BOARD_ID, newPostRequest, AUTH_INFO);
        Post actual = postRepository.findById(postId).orElseThrow();

        assertAll(
                () -> assertThat(actual.getTitle()).isEqualTo(newPostRequest.getTitle()),
                () -> assertThat(actual.getContent()).isEqualTo(newPostRequest.getContent()),
                () -> assertThat(actual.getMember().getId()).isEqualTo(member.getId()),
                () -> assertThat(actual.getNickname()).isEqualTo(actual.getMember().getNickname()),
                () -> assertThat(actual.getCreatedAt()).isNotNull(),
                () -> assertThat(actual.getPostBoards().get(0).getBoard().getTitle()).isNotNull()
        );
    }

    @DisplayName("본인이 작성한 게시글 조회 기능")
    @Test
    void findPost_Session_MyPost() {
        Long savedPostId = postRepository.save(post).getId();
        postBoard = PostBoard.builder()
                .build();
        postBoard.addPost(post);
        postBoard.addBoard(board);
        postBoardRepository.save(postBoard);

        PostDetailResponse response = postService.findPost(savedPostId, AUTH_INFO);

        assertAll(
                () -> assertThat(response.getTitle()).isEqualTo(post.getTitle()),
                () -> assertThat(response.getContent()).isEqualTo(post.getContent()),
                () -> assertThat(response.isAuthorized()).isTrue(),
                () -> assertThat(response.isModified()).isFalse(),
                () -> assertThat(response.getCreatedAt()).isNotNull(),
                () -> assertThat(response.getBoardId()).isEqualTo(postBoard.getBoard().getId())
        );
    }

    @DisplayName("로그인을 하고, 다른 회원이 작성한 게시글 조회 기능")
    @Test
    void findPost_Session_OtherPost() {
        Long savedPostId = postRepository.save(post).getId();
        postBoard = PostBoard.builder().build();
        postBoard.addPost(post);
        postBoard.addBoard(board);
        postBoardRepository.save(postBoard);

        PostDetailResponse response = postService.findPost(savedPostId, new AuthInfo(2L, USER.getName(), "nickname"));

        assertAll(
                () -> assertThat(response.getTitle()).isEqualTo(post.getTitle()),
                () -> assertThat(response.getContent()).isEqualTo(post.getContent()),
                () -> assertThat(response.isAuthorized()).isFalse(),
                () -> assertThat(response.isModified()).isFalse(),
                () -> assertThat(response.getCreatedAt()).isNotNull(),
                () -> assertThat(response.getBoardId()).isEqualTo(postBoard.getBoard().getId())
        );
    }

    @DisplayName("로그인 없이,게시글 조회 기능")
    @Test
    void findPost_NoSession() {
        Long savedPostId = postRepository.save(post).getId();
        postBoard = PostBoard.builder()
                .build();
        postBoard.addPost(post);
        postBoard.addBoard(board);
        postBoardRepository.save(postBoard);

        PostDetailResponse response = postService.findPost(savedPostId, new AuthInfo(null, USER.getName(), "nickname"));

        assertAll(
                () -> assertThat(response.getTitle()).isEqualTo(post.getTitle()),
                () -> assertThat(response.getContent()).isEqualTo(post.getContent()),
                () -> assertThat(response.isAuthorized()).isFalse(),
                () -> assertThat(response.isModified()).isFalse(),
                () -> assertThat(response.getCreatedAt()).isNotNull(),
                () -> assertThat(response.getBoardId()).isEqualTo(postBoard.getBoard().getId())
        );
    }

    @DisplayName("로그인 없이,게시글 조회 기능")
    @Test
    void findPost_No_Hot_Board() {
        Long savedPostId = postRepository.save(post).getId();
        postBoard = PostBoard.builder()
                .build();
        postBoard.addPost(post);
        postBoard.addBoard(board);
        PostBoard postHotBoard = PostBoard.builder()
                .build();
        postHotBoard.addBoard(hotBoard);
        postHotBoard.addPost(post);
        postBoardRepository.save(postBoard);
        postBoardRepository.save(postHotBoard);

        PostDetailResponse response = postService.findPost(savedPostId, new AuthInfo(null, USER.getName(), "nickname"));

        assertAll(
                () -> assertThat(response.getTitle()).isEqualTo(post.getTitle()),
                () -> assertThat(response.getContent()).isEqualTo(post.getContent()),
                () -> assertThat(response.isAuthorized()).isFalse(),
                () -> assertThat(response.isModified()).isFalse(),
                () -> assertThat(response.getCreatedAt()).isNotNull(),
                () -> assertThat(response.getBoardId()).isEqualTo(postBoard.getBoard().getId())
        );
    }

    @DisplayName("존재하지 않는 id로 글 조회 시 예외 발생")
    @Test
    void findPost_Exception() {
        Long invalidPostId = 9999L;

        assertThatThrownBy(() -> postService.findPost(invalidPostId, AUTH_INFO))
                .isInstanceOf(PostNotFoundException.class);
    }

    @DisplayName("특정 게시판 게시글 목록 조회 기능")
    @Test
    void findPostsByBoard() {
        postService.addPost(board.getId(), new NewPostRequest("제목1", "본문1", false, new ArrayList<>()),
                new AuthInfo(1L, USER.getName(), "nickname"));
        postService.addPost(board.getId(), new NewPostRequest("제목2", "본문2", false, new ArrayList<>()),
                new AuthInfo(1L, USER.getName(), "nickname"));
        postService.addPost(board.getId(), new NewPostRequest("제목3", "본문3", false, new ArrayList<>()),
                new AuthInfo(1L, USER.getName(), "nickname"));

        Pageable pageable = PageRequest.of(0, 2, DESC, "createdAt");
        PostsResponse postsResponse = postService.findPostsByBoard(board.getId(), pageable);

        assertAll(
                () -> assertThat(postsResponse.getPosts()).hasSize(2),
                () -> assertThat(postsResponse.getPosts())
                        .extracting("title")
                        .containsExactly("제목3", "제목2")
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
        postRepository.save(post);
        NewCommentRequest newCommentRequest = new NewCommentRequest("댓글", true);
        commentService.addComment(post.getId(), newCommentRequest, AUTH_INFO);

        postService.deletePost(post.getId(), AUTH_INFO);

        Optional<Post> deletePost = postRepository.findById(post.getId());
        assertThat(deletePost).isEmpty();
    }

    @DisplayName("내가 쓴 글 조회 기능")
    @Test
    void findMyPosts() {
        Post post1 = Post.builder()
                .title("제목")
                .content("본문")
                .member(member)
                .build();
        postRepository.save(post1);
        Post post2 = Post.builder()
                .title("제목2")
                .content("본문2")
                .member(member)
                .build();
        postRepository.save(post2);

        MyPostsResponse myPosts = postService.findMyPosts(PageRequest.of(0, 1), AUTH_INFO);

        assertAll(
                () -> assertThat(myPosts.getPosts()).usingRecursiveComparison()
                        .comparingOnlyFields("title", "content")
                        .isEqualTo(List.of(PostsElementResponse.from(post2))),
                () -> assertThat(myPosts.getTotalPageCount()).isEqualTo(2)
        );
    }

    @DisplayName("없는 페이지로 요청이 올 시 빈 배열 반환")
    @Test
    void findMyPosts_Exception_WrongPage() {
        Post post = Post.builder()
                .title("제목")
                .content("본문")
                .writerNickname(member.getNickname())
                .member(member)
                .postLikes(new ArrayList<>())
                .comments(new ArrayList<>())
                .build();
        postRepository.save(post);
        int wrongPage = 99;

        MyPostsResponse myPosts = postService.findMyPosts(PageRequest.of(wrongPage, 3), AUTH_INFO);

        assertAll(
                () -> assertThat(myPosts.getPosts()).isEmpty(),
                () -> assertThat(myPosts.getTotalPageCount()).isEqualTo(1)
        );
    }
}
