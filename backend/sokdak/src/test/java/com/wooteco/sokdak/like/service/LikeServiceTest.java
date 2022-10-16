package com.wooteco.sokdak.like.service;

import static com.wooteco.sokdak.util.fixture.BoardFixture.APPLICANT_BOARD_ID;
import static com.wooteco.sokdak.util.fixture.BoardFixture.FREE_BOARD_ID;
import static com.wooteco.sokdak.util.fixture.PostFixture.VALID_POST_CONTENT;
import static com.wooteco.sokdak.util.fixture.PostFixture.VALID_POST_TITLE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.wooteco.sokdak.auth.exception.AuthorizationException;
import com.wooteco.sokdak.comment.domain.Comment;
import com.wooteco.sokdak.comment.dto.CommentResponse;
import com.wooteco.sokdak.comment.dto.ReplyResponse;
import com.wooteco.sokdak.comment.repository.CommentRepository;
import com.wooteco.sokdak.comment.service.CommentService;
import com.wooteco.sokdak.like.dto.LikeFlipRequest;
import com.wooteco.sokdak.like.dto.LikeFlipResponse;
import com.wooteco.sokdak.post.domain.Post;
import com.wooteco.sokdak.post.repository.PostRepository;
import com.wooteco.sokdak.util.ServiceTest;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

class LikeServiceTest extends ServiceTest {

    @Autowired
    private LikeService likeService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    private Post post;
    private Comment comment;
    private Comment reply;

    @BeforeEach
    void setUp() {
        post = Post.builder()
                .title(VALID_POST_TITLE)
                .content(VALID_POST_CONTENT)
                .member(member)
                .build();
        postRepository.save(post);

        comment = Comment.parent(member, post, "nickname", "댓글내용");
        commentRepository.save(comment);
        reply = Comment.child(member, post, "닉네임2", "대댓글", comment);
        commentRepository.save(reply);
    }

    @DisplayName("게시글 좋아요 등록")
    @Test
    void flipPostLike_create() {
        LikeFlipResponse putLikeResponse = likeService.flipPostLike(post.getId(), AUTH_INFO, new LikeFlipRequest(
                FREE_BOARD_ID));

        assertAll(
                () -> assertThat(putLikeResponse.isLike()).isTrue(),
                () -> assertThat(putLikeResponse.getLikeCount()).isEqualTo(1)
        );
    }

    @DisplayName("게시글 좋아요 취소")
    @Test
    void flipPostLike_delete() {
        LikeFlipRequest likeFlipRequest = new LikeFlipRequest(FREE_BOARD_ID);
        likeService.flipPostLike(post.getId(), AUTH_INFO, likeFlipRequest);

        LikeFlipResponse putLikeResponse2 = likeService.flipPostLike(post.getId(), AUTH_INFO, likeFlipRequest);

        assertAll(
                () -> assertThat(putLikeResponse2.isLike()).isFalse(),
                () -> assertThat(putLikeResponse2.getLikeCount()).isZero()
        );
    }

    @DisplayName("댓글 좋아요 등록")
    @Test
    void flipCommentLike_create() {
        LikeFlipResponse likeFlipResponse = likeService.flipCommentLike(comment.getId(), AUTH_INFO, new LikeFlipRequest(FREE_BOARD_ID));

        assertAll(
                () -> assertThat(likeFlipResponse.isLike()).isTrue(),
                () -> assertThat(likeFlipResponse.getLikeCount()).isEqualTo(1)
        );
    }

    @DisplayName("댓글 좋아요 취소")
    @Test
    void flipCommentLike_delete() {
        LikeFlipRequest likeFlipRequest = new LikeFlipRequest(FREE_BOARD_ID);
        likeService.flipCommentLike(comment.getId(), AUTH_INFO, likeFlipRequest);

        LikeFlipResponse likeFlipResponse = likeService.flipCommentLike(comment.getId(), AUTH_INFO, likeFlipRequest);

        assertAll(
                () -> assertThat(likeFlipResponse.isLike()).isFalse(),
                () -> assertThat(likeFlipResponse.getLikeCount()).isZero()
        );
    }

    @DisplayName("지원자는 권한이 없는 게시판 게시글에 좋아요를 누를 수 없다.")
    @ParameterizedTest
    @CsvSource({"1", "2", "3", "4"})
    void flipPostLike_Applicant_Exception(Long boardId) {
        LikeFlipRequest likeFlipRequest = new LikeFlipRequest(boardId);

        assertThatThrownBy(() -> likeService.flipPostLike(post.getId(), APPLICANT_AUTH_INFO, likeFlipRequest))
                .isInstanceOf(AuthorizationException.class);
    }

    @DisplayName("지원자는 권한이 있는 게시판 게시글에 좋아요를 누를 수 있다.")
    @Test
    void flipPostLike_Applicant() {
        LikeFlipRequest likeFlipRequest = new LikeFlipRequest(APPLICANT_BOARD_ID);

        LikeFlipResponse likeFlipResponse = likeService.flipPostLike(post.getId(), APPLICANT_AUTH_INFO, likeFlipRequest);

        assertAll(
                () -> assertThat(likeFlipResponse.isLike()).isTrue(),
                () -> assertThat(likeFlipResponse.getLikeCount()).isOne()
        );
    }

    @DisplayName("대댓글 좋아요 등록")
    @Test
    void flipCommentLike_ReplyCreate() {
        LikeFlipResponse likeFlipResponse = likeService.flipCommentLike(reply.getId(), AUTH_INFO, new LikeFlipRequest(FREE_BOARD_ID));

        assertAll(
                () -> assertThat(likeFlipResponse.isLike()).isTrue(),
                () -> assertThat(likeFlipResponse.getLikeCount()).isEqualTo(1)
        );
    }

    @DisplayName("대댓글 좋아요 취소")
    @Test
    void flipCommentLike_ReplyDelete() {
        LikeFlipRequest likeFlipRequest = new LikeFlipRequest(FREE_BOARD_ID);
        likeService.flipCommentLike(reply.getId(), AUTH_INFO, likeFlipRequest);

        LikeFlipResponse likeFlipResponse = likeService.flipCommentLike(reply.getId(), AUTH_INFO, likeFlipRequest);

        assertAll(
                () -> assertThat(likeFlipResponse.isLike()).isFalse(),
                () -> assertThat(likeFlipResponse.getLikeCount()).isZero()
        );
    }

    @DisplayName("지원자는 권한이 없는 게시판 게시글의 댓글, 대댓글에 좋아요를 누를 수 없다.")
    @ParameterizedTest
    @CsvSource({"1", "2", "3", "4"})
    void flipCommentLike_Applicant_Exception(Long boardId) {
        LikeFlipRequest likeFlipRequest = new LikeFlipRequest(boardId);

        assertThatThrownBy(() -> likeService.flipCommentLike(comment.getId(), APPLICANT_AUTH_INFO, likeFlipRequest))
                .isInstanceOf(AuthorizationException.class);
    }

    @DisplayName("지원자는 권한이 있는 게시판 게시글의 댓글, 대댓글에 좋아요를 누를 수 있다.")
    @Test
    void flipCommentLike_Applicant() {
        LikeFlipRequest likeFlipRequest = new LikeFlipRequest(APPLICANT_BOARD_ID);

        LikeFlipResponse likeFlipResponse = likeService.flipCommentLike(comment.getId(), APPLICANT_AUTH_INFO, likeFlipRequest);

        assertAll(
                () -> assertThat(likeFlipResponse.isLike()).isTrue(),
                () -> assertThat(likeFlipResponse.getLikeCount()).isOne()
        );
    }

    @DisplayName("댓글 목록 조회 시 좋아요 개수를 알 수 있고 내가 좋아요를 눌렀음을 알 수 있다.")
    @Test
    @Transactional(propagation = Propagation.NEVER)
    void getCommentsLikes() {
        likeService.flipCommentLike(comment.getId(), AUTH_INFO, new LikeFlipRequest(FREE_BOARD_ID));

        List<CommentResponse> comments = commentService.findComments(post.getId(), AUTH_INFO).getComments();

        assertAll(
                () -> assertThat(comments.get(0).getLikeCount()).isEqualTo(1),
                () -> assertThat(comments.get(0).isLike()).isTrue()
        );
    }

    @DisplayName("댓글 목록 조회 시 좋아요 개수와 내가 좋아요를 누르지 않았음을 알 수 있다.")
    @Test
    @Transactional(propagation = Propagation.NEVER)
    void getCommentsLikesOfOther() {
        likeService.flipCommentLike(comment.getId(), AUTH_INFO, new LikeFlipRequest(FREE_BOARD_ID));

        List<CommentResponse> comments = commentService.findComments(post.getId(), AUTH_INFO2)
                .getComments();

        assertAll(
                () -> assertThat(comments.get(0).getLikeCount()).isEqualTo(1),
                () -> assertThat(comments.get(0).isLike()).isFalse()
        );
    }

    @DisplayName("대댓글 목록 조회 시 좋아요 개수를 알 수 있고 내가 좋아요를 눌렀음을 알 수 있다.")
    @Test
    @Transactional(propagation = Propagation.NEVER)
    void getCommentsLikesReply() {
        likeService.flipCommentLike(reply.getId(), AUTH_INFO, new LikeFlipRequest(FREE_BOARD_ID));

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
    @Transactional(propagation = Propagation.NEVER)
    void getCommentsLikesReplyOfOther() {
        likeService.flipCommentLike(reply.getId(), AUTH_INFO, new LikeFlipRequest(FREE_BOARD_ID));

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
