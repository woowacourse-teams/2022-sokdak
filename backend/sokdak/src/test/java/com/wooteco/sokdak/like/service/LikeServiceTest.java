package com.wooteco.sokdak.like.service;

<<<<<<< HEAD
=======
import static com.wooteco.sokdak.util.fixture.MemberFixture.AUTH_INFO;
import static com.wooteco.sokdak.util.fixture.MemberFixture.AUTH_INFO2;
import static com.wooteco.sokdak.util.fixture.MemberFixture.VALID_NICKNAME;
import static com.wooteco.sokdak.util.fixture.MemberFixture.VALID_PASSWORD;
import static com.wooteco.sokdak.util.fixture.MemberFixture.VALID_USERNAME;
>>>>>>> b5db00c (feat: 댓글 좋아요 기능 #395 (#501))
import static com.wooteco.sokdak.util.fixture.PostFixture.VALID_POST_CONTENT;
import static com.wooteco.sokdak.util.fixture.PostFixture.VALID_POST_TITLE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.wooteco.sokdak.comment.domain.Comment;
import com.wooteco.sokdak.comment.dto.CommentResponse;
import com.wooteco.sokdak.comment.dto.ReplyResponse;
import com.wooteco.sokdak.comment.repository.CommentRepository;
import com.wooteco.sokdak.comment.service.CommentService;
import com.wooteco.sokdak.like.dto.LikeFlipResponse;
import com.wooteco.sokdak.post.domain.Post;
import com.wooteco.sokdak.post.repository.PostRepository;
<<<<<<< HEAD
import com.wooteco.sokdak.util.ServiceTest;
=======
import com.wooteco.sokdak.util.IntegrationTest;
import java.util.List;
>>>>>>> b5db00c (feat: 댓글 좋아요 기능 #395 (#501))
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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

<<<<<<< HEAD
=======
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CommentRepository commentRepository;

    private Member member;
>>>>>>> b5db00c (feat: 댓글 좋아요 기능 #395 (#501))
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

        Post anonymousPost = postRepository.save(post);

        comment = Comment.parent(member, anonymousPost, "nickname", "댓글내용");
        commentRepository.save(comment);
        reply = Comment.child(member, anonymousPost, "닉네임2", "대댓글", comment);
        commentRepository.save(reply);
    }

    @DisplayName("게시글 좋아요 등록")
    @Test
    void flipLike_create() {
        LikeFlipResponse putLikeResponse = likeService.flipPostLike(post.getId(), AUTH_INFO);

        assertAll(
                () -> assertThat(putLikeResponse.isLike()).isTrue(),
                () -> assertThat(putLikeResponse.getLikeCount()).isEqualTo(1)
        );
    }

    @DisplayName("게시글 좋아요 취소")
    @Test
    void flipLike_delete() {
        likeService.flipPostLike(post.getId(), AUTH_INFO);

        LikeFlipResponse putLikeResponse2 = likeService.flipPostLike(post.getId(), AUTH_INFO);

        assertAll(
                () -> assertThat(putLikeResponse2.isLike()).isFalse(),
                () -> assertThat(putLikeResponse2.getLikeCount()).isZero()
        );
    }

    @DisplayName("댓글 좋아요 등록")
    @Test
    void flipLikeComment_create() {
        LikeFlipResponse likeFlipResponse = likeService.flipCommentLike(comment.getId(), AUTH_INFO);

        assertAll(
                () -> assertThat(likeFlipResponse.isLike()).isTrue(),
                () -> assertThat(likeFlipResponse.getLikeCount()).isEqualTo(1)
        );
    }

    @DisplayName("댓글 좋아요 취소")
    @Test
    void flipLikeComment_delete() {
        likeService.flipCommentLike(comment.getId(), AUTH_INFO);

        LikeFlipResponse likeFlipResponse = likeService.flipCommentLike(comment.getId(), AUTH_INFO);

        assertAll(
                () -> assertThat(likeFlipResponse.isLike()).isFalse(),
                () -> assertThat(likeFlipResponse.getLikeCount()).isZero()
        );
    }

    @DisplayName("대댓글 좋아요 등록")
    @Test
    void flipLikeReply_create() {
        LikeFlipResponse likeFlipResponse = likeService.flipCommentLike(reply.getId(), AUTH_INFO);

        assertAll(
                () -> assertThat(likeFlipResponse.isLike()).isTrue(),
                () -> assertThat(likeFlipResponse.getLikeCount()).isEqualTo(1)
        );
    }

    @DisplayName("대댓글 좋아요 취소")
    @Test
    void flipLikeReply_delete() {
        likeService.flipCommentLike(reply.getId(), AUTH_INFO);

        LikeFlipResponse likeFlipResponse = likeService.flipCommentLike(reply.getId(), AUTH_INFO);

        assertAll(
                () -> assertThat(likeFlipResponse.isLike()).isFalse(),
                () -> assertThat(likeFlipResponse.getLikeCount()).isZero()
        );
    }

    @DisplayName("댓글 목록 조회 시 좋아요 개수를 알 수 있고 내가 좋아요를 눌렀음을 알 수 있다.")
    @Test
    @Transactional(propagation = Propagation.NEVER)
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
    @Transactional(propagation = Propagation.NEVER)
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
    @Transactional(propagation = Propagation.NEVER)
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
    @Transactional(propagation = Propagation.NEVER)
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
