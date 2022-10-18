package com.wooteco.sokdak.like.service;

import static com.wooteco.sokdak.util.fixture.BoardFixture.APPLICANT_BOARD_ID;
import static com.wooteco.sokdak.util.fixture.BoardFixture.FREE_BOARD_ID;
import static com.wooteco.sokdak.util.fixture.BoardFixture.GOOD_CREW_BOARD_ID;
import static com.wooteco.sokdak.util.fixture.BoardFixture.HOT_BOARD_ID;
import static com.wooteco.sokdak.util.fixture.BoardFixture.POSUTA_BOARD_ID;
import static com.wooteco.sokdak.util.fixture.PostFixture.VALID_POST_CONTENT;
import static com.wooteco.sokdak.util.fixture.PostFixture.VALID_POST_TITLE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.wooteco.sokdak.auth.exception.AuthorizationException;
import com.wooteco.sokdak.board.domain.Board;
import com.wooteco.sokdak.board.domain.PostBoard;
import com.wooteco.sokdak.board.repository.BoardRepository;
import com.wooteco.sokdak.board.repository.PostBoardRepository;
import com.wooteco.sokdak.comment.domain.Comment;
import com.wooteco.sokdak.comment.dto.CommentResponse;
import com.wooteco.sokdak.comment.dto.ReplyResponse;
import com.wooteco.sokdak.comment.exception.CommentNotFoundException;
import com.wooteco.sokdak.comment.repository.CommentRepository;
import com.wooteco.sokdak.comment.service.CommentService;
import com.wooteco.sokdak.like.dto.LikeFlipResponse;
import com.wooteco.sokdak.post.domain.Post;
import com.wooteco.sokdak.post.exception.PostNotFoundException;
import com.wooteco.sokdak.post.repository.PostRepository;
import com.wooteco.sokdak.util.ServiceTest;
import java.util.List;
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

class LikeServiceTest extends ServiceTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private LikeService likeService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private PostBoardRepository postBoardRepository;

    private Post post;
    private Post applicantPost;
    private Comment comment;
    private Comment reply;
    private Comment applicantComment;

    @BeforeEach
    void setUp() {
        Board freeBoard = boardRepository.findById(FREE_BOARD_ID).get();

        post = Post.builder()
                .title(VALID_POST_TITLE)
                .content(VALID_POST_CONTENT)
                .member(member)
                .writerNickname(member.getNickname())
                .build();
        applicantPost = Post.builder()
                .title(VALID_POST_TITLE)
                .content(VALID_POST_CONTENT)
                .member(member)
                .writerNickname(member.getNickname())
                .build();
        postRepository.save(post);
        postRepository.save(applicantPost);

        PostBoard postBoard = PostBoard.builder().build();
        postBoard.addPost(post);
        postBoard.addBoard(freeBoard);
        PostBoard applicantPostBoard = PostBoard.builder().build();
        applicantPostBoard.addPost(applicantPost);
        applicantPostBoard.addBoard(boardRepository.findById(APPLICANT_BOARD_ID).get());
        postBoardRepository.save(postBoard);
        postBoardRepository.save(applicantPostBoard);

        comment = Comment.parent(member, post, "nickname", "댓글내용");
        commentRepository.save(comment);
        reply = Comment.child(member, post, "닉네임2", "대댓글", comment);
        commentRepository.save(reply);
        entityManager.clear();

        applicantComment = Comment.parent(member, applicantPost, "nickname", "message");
        commentRepository.save(applicantComment);
    }

    @DisplayName("게시글 좋아요 등록")
    @Test
    void flipPostLike_create() {
        LikeFlipResponse putLikeResponse = likeService.flipPostLike(post.getId(), AUTH_INFO);

        entityManager.flush();
        entityManager.clear();
        Post foundPost = postRepository.findById(post.getId())
                .orElseThrow(PostNotFoundException::new);
        assertAll(
                () -> assertThat(putLikeResponse.isLike()).isTrue(),
                () -> assertThat(putLikeResponse.getLikeCount()).isEqualTo(1),
                () -> assertThat(foundPost.hasLikeOfMember(member.getId())).isTrue()
        );
    }

    @DisplayName("게시글 좋아요 취소")
    @Test
    void flipPostLike_delete() {
        likeService.flipPostLike(post.getId(), AUTH_INFO);
        entityManager.flush();
        entityManager.clear();

        LikeFlipResponse putLikeResponse = likeService.flipPostLike(post.getId(), AUTH_INFO);

        entityManager.flush();
        entityManager.clear();
        Post foundPost = postRepository.findById(post.getId())
                .orElseThrow(PostNotFoundException::new);
        assertAll(
                () -> assertThat(putLikeResponse.isLike()).isFalse(),
                () -> assertThat(putLikeResponse.getLikeCount()).isZero(),
                () -> assertThat(foundPost.hasLikeOfMember(member.getId())).isFalse()
        );
    }

    @DisplayName("댓글 좋아요 등록")
    @Test
    void flipCommentLike_create() {
        LikeFlipResponse likeFlipResponse = likeService.flipCommentLike(comment.getId(), AUTH_INFO
        );

        entityManager.flush();
        entityManager.clear();
        Comment foundComment = commentRepository.findById(comment.getId())
                .orElseThrow(CommentNotFoundException::new);
        assertAll(
                () -> assertThat(likeFlipResponse.isLike()).isTrue(),
                () -> assertThat(likeFlipResponse.getLikeCount()).isEqualTo(1),
                () -> assertThat(foundComment.hasLikeOfMember(member.getId())).isTrue()
        );
    }

    @DisplayName("댓글 좋아요 취소")
    @Test
    void flipCommentLike_delete() {
        likeService.flipCommentLike(comment.getId(), AUTH_INFO);
        entityManager.flush();
        entityManager.clear();

        LikeFlipResponse likeFlipResponse = likeService.flipCommentLike(comment.getId(), AUTH_INFO);

        entityManager.flush();
        entityManager.clear();
        Comment foundComment = commentRepository.findById(comment.getId())
                .orElseThrow(CommentNotFoundException::new);
        assertAll(
                () -> assertThat(likeFlipResponse.isLike()).isFalse(),
                () -> assertThat(likeFlipResponse.getLikeCount()).isZero(),
                () -> assertThat(foundComment.hasLikeOfMember(member.getId())).isFalse()
        );
    }

    @DisplayName("지원자는 권한이 없는 게시판 게시글에 좋아요를 누를 수 없다.")
    @ParameterizedTest
    @CsvSource({"2", "3", "4"})
    void flipPostLike_Applicant_Exception(Long boardId) {
        Post post = Post.builder()
                .member(member)
                .title(VALID_POST_TITLE)
                .content(VALID_POST_CONTENT)
                .writerNickname("randomNickname")
                .build();
        postRepository.save(post);
        PostBoard postBoard = PostBoard.builder().build();
        postBoard.addBoard(boardRepository.findById(boardId).get());
        postBoard.addPost(post);
        postBoardRepository.save(postBoard);

        assertThatThrownBy(() -> likeService.flipPostLike(post.getId(), APPLICANT_AUTH_INFO))
                .isInstanceOf(AuthorizationException.class);
    }

    @DisplayName("지원자는 권한이 있는 게시판 게시글에 좋아요를 누를 수 있다.")
    @Test
    void flipPostLike_Applicant() {
        LikeFlipResponse likeFlipResponse = likeService.flipPostLike(applicantPost.getId(), APPLICANT_AUTH_INFO
        );

        assertAll(
                () -> assertThat(likeFlipResponse.isLike()).isTrue(),
                () -> assertThat(likeFlipResponse.getLikeCount()).isOne()
        );
    }

    @DisplayName("대댓글 좋아요 등록")
    @Test
    void flipCommentLike_ReplyCreate() {
        LikeFlipResponse likeFlipResponse = likeService.flipCommentLike(reply.getId(), AUTH_INFO
        );

        entityManager.flush();
        entityManager.clear();
        Comment foundReply = commentRepository.findById(reply.getId())
                .orElseThrow(CommentNotFoundException::new);
        assertAll(
                () -> assertThat(likeFlipResponse.isLike()).isTrue(),
                () -> assertThat(likeFlipResponse.getLikeCount()).isEqualTo(1),
                () -> assertThat(foundReply.hasLikeOfMember(member.getId())).isTrue()
        );
    }

    @DisplayName("대댓글 좋아요 취소")
    @Test
    void flipCommentLike_ReplyDelete() {
        likeService.flipCommentLike(reply.getId(), AUTH_INFO);

        LikeFlipResponse likeFlipResponse = likeService.flipCommentLike(reply.getId(), AUTH_INFO);

        entityManager.flush();
        entityManager.clear();
        assertAll(
                () -> assertThat(likeFlipResponse.isLike()).isFalse(),
                () -> assertThat(likeFlipResponse.getLikeCount()).isZero(),
                () -> assertThat(reply.hasLikeOfMember(member.getId())).isFalse()
        );
    }

    @DisplayName("지원자는 권한이 없는 게시판 게시글의 댓글, 대댓글에 좋아요를 누를 수 없다.")
    @ParameterizedTest
    @CsvSource({"2", "3", "4"})
    void flipCommentLike_Applicant_Exception(Long boardId) {
        Post post = Post.builder()
                .member(member)
                .title(VALID_POST_TITLE)
                .content(VALID_POST_CONTENT)
                .writerNickname("randomNickname")
                .build();
        postRepository.save(post);
        PostBoard postBoard = PostBoard.builder().build();
        postBoard.addBoard(boardRepository.findById(boardId).get());
        postBoard.addPost(post);
        postBoardRepository.save(postBoard);
        comment = Comment.parent(member, post, "nickname", "댓글내용");
        commentRepository.save(comment);

        assertThatThrownBy(() -> likeService.flipCommentLike(comment.getId(), APPLICANT_AUTH_INFO))
                .isInstanceOf(AuthorizationException.class);
    }

    @DisplayName("지원자는 권한이 있는 게시판 게시글의 댓글, 대댓글에 좋아요를 누를 수 있다.")
    @Test
    void flipCommentLike_Applicant() {
        LikeFlipResponse likeFlipResponse = likeService.flipCommentLike(applicantComment.getId(), APPLICANT_AUTH_INFO
        );

        assertAll(
                () -> assertThat(likeFlipResponse.isLike()).isTrue(),
                () -> assertThat(likeFlipResponse.getLikeCount()).isOne()
        );
    }

    @DisplayName("댓글 목록 조회 시 좋아요 개수를 알 수 있고 내가 좋아요를 눌렀음을 알 수 있다.")
    @Test
    void getCommentsLikes() {
        likeService.flipCommentLike(comment.getId(), AUTH_INFO);

        List<CommentResponse> comments = commentService.findComments(post.getId(), AUTH_INFO).getComments();

        assertAll(
                () -> assertThat(comments.get(0).getLikeCount()).isEqualTo(1),
                () -> assertThat(comments.get(0).isLike()).isTrue()
        );
    }

    @DisplayName("댓글 목록 조회 시 좋아요 개수와 내가 좋아요를 누르지 않았음을 알 수 있다.")
    @Test
    void getCommentsLikesOfOther() {
        likeService.flipCommentLike(comment.getId(), AUTH_INFO);

        List<CommentResponse> comments = commentService.findComments(post.getId(), AUTH_INFO2)
                .getComments();

        assertAll(
                () -> assertThat(comments.get(0).getLikeCount()).isEqualTo(1),
                () -> assertThat(comments.get(0).isLike()).isFalse()
        );
    }

    @DisplayName("대댓글 목록 조회 시 좋아요 개수를 알 수 있고 내가 좋아요를 눌렀음을 알 수 있다.")
    @Test
    void getCommentsLikesReply() {
        likeService.flipCommentLike(reply.getId(), AUTH_INFO);

        List<ReplyResponse> replies = commentService.findComments(post.getId(), AUTH_INFO)
                .getComments()
                .get(0)
                .getReplies();

        assertAll(
                () -> assertThat(replies.get(0).getLikeCount()).isEqualTo(1),
                () -> assertThat(replies.get(0).isLike()).isTrue()
        );
    }

    @DisplayName("대댓글 목록 조회 시 좋아요 개수와 내가 좋아요를 누르지 않았음을 알 수 있다.")
    @Test
    void getCommentsLikesReplyOfOther() {
        likeService.flipCommentLike(reply.getId(), AUTH_INFO);

        List<ReplyResponse> replies = commentService.findComments(post.getId(), AUTH_INFO2)
                .getComments()
                .get(0)
                .getReplies();

        assertAll(
                () -> assertThat(replies.get(0).getLikeCount()).isEqualTo(1),
                () -> assertThat(replies.get(0).isLike()).isFalse()
        );
    }
}
