package com.wooteco.sokdak.post.service;

import static com.wooteco.sokdak.member.domain.RoleType.USER;
import static com.wooteco.sokdak.util.fixture.BoardFixture.APPLICANT_BOARD_ID;
import static com.wooteco.sokdak.util.fixture.BoardFixture.FREE_BOARD_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.data.domain.Sort.Direction.DESC;

import com.wooteco.sokdak.auth.dto.AuthInfo;
import com.wooteco.sokdak.auth.exception.AuthorizationException;
import com.wooteco.sokdak.board.domain.Board;
import com.wooteco.sokdak.board.domain.PostBoard;
import com.wooteco.sokdak.board.exception.BoardNotFoundException;
import com.wooteco.sokdak.board.repository.BoardRepository;
import com.wooteco.sokdak.board.repository.PostBoardRepository;
import com.wooteco.sokdak.comment.dto.NewCommentRequest;
import com.wooteco.sokdak.comment.service.CommentService;
import com.wooteco.sokdak.hashtag.domain.Hashtag;
import com.wooteco.sokdak.hashtag.domain.PostHashtag;
import com.wooteco.sokdak.hashtag.repository.HashtagRepository;
import com.wooteco.sokdak.hashtag.repository.PostHashtagRepository;
import com.wooteco.sokdak.post.domain.Post;
import com.wooteco.sokdak.post.domain.PostDeletionEvent;
import com.wooteco.sokdak.post.dto.NewPostRequest;
import com.wooteco.sokdak.post.dto.PagePostsResponse;
import com.wooteco.sokdak.post.dto.PostDetailResponse;
import com.wooteco.sokdak.post.dto.PostUpdateRequest;
import com.wooteco.sokdak.post.dto.PostsCountResponse;
import com.wooteco.sokdak.post.dto.PostsElementResponse;
import com.wooteco.sokdak.post.dto.PostsResponse;
import com.wooteco.sokdak.post.exception.PostNotFoundException;
import com.wooteco.sokdak.post.repository.PostRepository;
import com.wooteco.sokdak.util.ServiceTest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

class PostServiceTest extends ServiceTest {

    private static final String EMPTY_COOKIE_VALUE = "";

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

    @PersistenceContext
    private EntityManager em;

    private Post post;
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

    @DisplayName("특정 게시물의 조회수를 1 증가시킴")
    @Test
    void findPost_ViewCount_Deprecated() {
        postRepository.save(post);
        PostBoard postBoard = PostBoard.builder().build();
        postBoard.addBoard(board);
        postBoard.addPost(post);
        postBoardRepository.save(postBoard);

        int viewCount = post.getViewCount();
        postService.findPost(post.getId(), AUTH_INFO, EMPTY_COOKIE_VALUE);
        em.clear();
        int updatedViewCount = postRepository.findById(post.getId()).get().getViewCount();

        assertThat(viewCount + 1).isEqualTo(updatedViewCount);
    }

    @DisplayName("오늘 처음 방문한 사이트를 조회하면 조회수가 1 증가되고, 이미 오늘 방문한 사이트를 조회하면 조회수가 올라가지 않는다.")
    @ParameterizedTest
    @MethodSource("argsOfFindPostViewCount")
    void findPost_ViewCount(String logs, int expectedIncreasementViewcount) {
        postRepository.save(post);
        PostBoard postBoard = PostBoard.builder().build();
        postBoard.addBoard(board);
        postBoard.addPost(post);
        postBoardRepository.save(postBoard);

        int viewCount = post.getViewCount();
        postService.findPost(post.getId(), AUTH_INFO, logs);
        em.clear();

        int updatedViewCount = postRepository.findById(post.getId())
                .get()
                .getViewCount();

        assertThat(viewCount + expectedIncreasementViewcount).isEqualTo(updatedViewCount);
    }

    // post.getId == 1
    private static Stream<Arguments> argsOfFindPostViewCount() {
        int today = LocalDateTime.now().getDayOfMonth();
        return Stream.of(
                Arguments.of(EMPTY_COOKIE_VALUE, 1),
                Arguments.of(today + ":2/3", 1),
                Arguments.of(today + ":1/2/3", 0)
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

    @DisplayName("지원자는 권한이 없는 게시판에 글을 작성할 수 없다.")
    @ParameterizedTest
    @CsvSource({"1", "2", "3", "4"})
    void addPost_Applicant_Exception(Long boardId) {
        NewPostRequest newPostRequest = new NewPostRequest("제목", "본문", false, Collections.emptyList());

        assertThatThrownBy(() -> postService.addPost(boardId, newPostRequest, APPLICANT_AUTH_INFO))
                .isInstanceOf(AuthorizationException.class);
    }

    @DisplayName("지원자는 권한이 있는 게시판에 글을 작성할 수 있다.")
    @Test
    void addPost_Applicant() {
        NewPostRequest newPostRequest = new NewPostRequest("제목", "본문", false, Collections.emptyList());

        Long postId = postService.addPost(APPLICANT_BOARD_ID, newPostRequest, APPLICANT_AUTH_INFO);
        Post actual = postRepository.findById(postId).orElseThrow();

        assertAll(
                () -> assertThat(actual.getTitle()).isEqualTo(newPostRequest.getTitle()),
                () -> assertThat(actual.getContent()).isEqualTo(newPostRequest.getContent()),
                () -> assertThat(actual.getMember().getId()).isEqualTo(APPLICANT_AUTH_INFO.getId()),
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

        PostDetailResponse response = postService.findPost(savedPostId, AUTH_INFO, EMPTY_COOKIE_VALUE);

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

        PostDetailResponse response = postService.findPost(savedPostId, new AuthInfo(2L, USER.getName(), "nickname"),
                EMPTY_COOKIE_VALUE);

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

        PostDetailResponse response = postService.findPost(savedPostId, new AuthInfo(null, USER.getName(), "nickname"),
                EMPTY_COOKIE_VALUE);

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

        PostDetailResponse response = postService.findPost(savedPostId, new AuthInfo(null, USER.getName(), "nickname"),
                EMPTY_COOKIE_VALUE);

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

        assertThatThrownBy(() -> postService.findPost(invalidPostId, AUTH_INFO, EMPTY_COOKIE_VALUE))
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
        long postDeletionEventCount = applicationEvents.stream(PostDeletionEvent.class).count();
        assertAll(
                () -> assertThat(foundPost).isEmpty(),
                () -> assertThat(postDeletionEventCount).isEqualTo(1L)
        );
    }

    @DisplayName("댓글이 있는 게시글 삭제")
    @Test
    void deletePostWithComment() {
        postRepository.save(post);
        PostBoard postBoard = PostBoard.builder().build();
        postBoard.addBoard(board);
        postBoard.addPost(post);
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
        Post post2 = Post.builder()
                .title("제목2")
                .content("본문2")
                .member(member)
                .build();
        postRepository.saveAll(List.of(post1, post2));

        PagePostsResponse myPosts = postService.findMyPosts(PageRequest.of(0, 1, DESC, "createdAt"), AUTH_INFO);

        assertAll(
                () -> assertThat(myPosts.getPosts()).usingRecursiveComparison()
                        .comparingOnlyFields("title", "content")
                        .isEqualTo(List.of(PostsElementResponse.from(post2))),
                () -> assertThat(myPosts.getTotalPageCount()).isEqualTo(2)
        );
    }

    @DisplayName("쿼리로 포스트 하나 검색 기능")
    @Test
    void searchSliceWithQueryAndHashtags_OnePost() {
        Post post1 = Post.builder()
                .title("제목")
                .content("본문")
                .member(member)
                .build();
        Post post2 = Post.builder()
                .title("제목2")
                .content("본문2")
                .member(member)
                .build();
        postRepository.saveAll(List.of(post1, post2));

        String query = "2";
        PostsResponse myPosts = postService.searchSliceWithQuery(query,
                PageRequest.of(0, 5, DESC, "createdAt"));
        PostsCountResponse postsCountResponse = postService.countPostWithQuery(query);

        assertAll(
                () -> assertThat(myPosts.getPosts()).usingRecursiveComparison()
                        .comparingOnlyFields("title", "content")
                        .isEqualTo(List.of(PostsElementResponse.from(post2))),
                () -> assertThat(postsCountResponse.getTotalPostCount()).isEqualTo(1)
        );
    }

    @DisplayName("쿼리로 여러 포스트 검색 기능")
    @Test
    void searchSliceWithQuery_MultiPost() {
        Post post1 = Post.builder()
                .title("제목")
                .content("본문")
                .member(member)
                .build();
        Post post2 = Post.builder()
                .title("제목2")
                .content("본문2")
                .member(member)
                .build();
        postRepository.saveAll(List.of(post1, post2));

        PostsResponse pagePostsResponse = postService.searchSliceWithQuery("제목",
                PageRequest.of(0, 5, DESC, "createdAt"));

        assertAll(
                () -> assertThat(pagePostsResponse.getPosts()).usingRecursiveComparison()
                        .comparingOnlyFields("title", "content")
                        .isEqualTo(List.of(PostsElementResponse.from(post2), PostsElementResponse.from(post1)))
        );
    }

    @DisplayName("쿼리로 제목과 본문 안에서 검색 기능")
    @Test
    void searchSliceWithQuery_TitleOrContent() {
        Post post1 = Post.builder()
                .title("제목")
                .content("본문")
                .member(member)
                .build();
        Post post2 = Post.builder()
                .title("제목2")
                .content("본문2검색")
                .member(member)
                .build();
        Post post3 = Post.builder()
                .title("제목2검색")
                .content("본문2")
                .member(member)
                .build();
        postRepository.saveAll(List.of(post1, post2, post3));

        String query = "검색";
        PostsResponse myPosts = postService.searchSliceWithQuery(query,
                PageRequest.of(0, 5, DESC, "createdAt"));
        PostsCountResponse postsCountResponse = postService.countPostWithQuery(query);

        assertAll(
                () -> assertThat(myPosts.getPosts()).usingRecursiveComparison()
                        .comparingOnlyFields("title", "content")
                        .isEqualTo(List.of(PostsElementResponse.from(post3), PostsElementResponse.from(post2))),
                () -> assertThat(postsCountResponse.getTotalPostCount()).isEqualTo(2)
        );
    }

    @DisplayName("띄어쓰기 쿼리로 제목과 본문 안에서 검색 기능")
    @Test
    void searchSliceWithQuery_Or() {
        Post post1 = Post.builder()
                .title("제목")
                .content("본문서치")
                .member(member)
                .build();
        Post post2 = Post.builder()
                .title("제목2")
                .content("본문2검색")
                .member(member)
                .build();
        Post post3 = Post.builder()
                .title("제목2검색")
                .content("본문2")
                .member(member)
                .build();
        postRepository.saveAll(List.of(post1, post2, post3));

        String query = "서치|검색";
        PostsResponse myPosts = postService.searchSliceWithQuery(query,
                PageRequest.of(0, 5, DESC, "createdAt"));
        PostsCountResponse postsCountResponse = postService.countPostWithQuery(query);

        assertAll(
                () -> assertThat(myPosts.getPosts()).usingRecursiveComparison()
                        .comparingOnlyFields("title", "content")
                        .isEqualTo(List.of(
                                PostsElementResponse.from(post3),
                                PostsElementResponse.from(post2),
                                PostsElementResponse.from(post1))),
                () -> assertThat(postsCountResponse.getTotalPostCount()).isEqualTo(3)
        );
    }

    @DisplayName("인젝션 위험 쿼리는 빈 쿼리로 대체")
    @ParameterizedTest
    @CsvSource(value = {"update", "delete", "insert", "select", "()"})
    void searchSliceWithQuery_noInjection(String query) {
        Post post1 = Post.builder()
                .title("제목")
                .content("본문서치")
                .member(member)
                .build();
        Post post2 = Post.builder()
                .title("제목2")
                .content("본문2검색")
                .member(member)
                .build();
        Post post3 = Post.builder()
                .title("제목2검색")
                .content("본문2")
                .member(member)
                .build();
        postRepository.saveAll(List.of(post1, post2, post3));

        PostsResponse myPosts = postService.searchSliceWithQuery(query,
                PageRequest.of(0, 5, DESC, "createdAt"));
        PostsCountResponse postsCountResponse = postService.countPostWithQuery(query);

        assertAll(
                () -> assertThat(myPosts.isLastPage()).isEqualTo(true),
                () -> assertThat(postsCountResponse.getTotalPostCount()).isEqualTo(3)
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

        PagePostsResponse myPosts = postService.findMyPosts(PageRequest.of(wrongPage, 3, DESC, "createdAt"), AUTH_INFO);

        assertAll(
                () -> assertThat(myPosts.getPosts()).isEmpty(),
                () -> assertThat(myPosts.getTotalPageCount()).isEqualTo(1)
        );
    }
}
