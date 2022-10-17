package com.wooteco.sokdak.comment.service;

import static com.wooteco.sokdak.member.domain.RoleType.USER;
import static com.wooteco.sokdak.util.fixture.BoardFixture.APPLICANT_BOARD_ID;
import static com.wooteco.sokdak.util.fixture.BoardFixture.FREE_BOARD_ID;
import static com.wooteco.sokdak.util.fixture.CommentFixture.ANONYMOUS_COMMENT_REQUEST;
import static com.wooteco.sokdak.util.fixture.CommentFixture.ANONYMOUS_REPLY_REQUEST;
import static com.wooteco.sokdak.util.fixture.CommentFixture.APPLICANT_COMMENT_REQUEST;
import static com.wooteco.sokdak.util.fixture.CommentFixture.NON_ANONYMOUS_COMMENT_REQUEST;
import static com.wooteco.sokdak.util.fixture.CommentFixture.NON_ANONYMOUS_REPLY_REQUEST;
import static com.wooteco.sokdak.util.fixture.PostFixture.VALID_POST_CONTENT;
import static com.wooteco.sokdak.util.fixture.PostFixture.VALID_POST_TITLE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.wooteco.sokdak.auth.dto.AuthInfo;
import com.wooteco.sokdak.auth.exception.AuthorizationException;
import com.wooteco.sokdak.board.domain.Board;
import com.wooteco.sokdak.board.domain.PostBoard;
import com.wooteco.sokdak.board.repository.BoardRepository;
import com.wooteco.sokdak.board.repository.PostBoardRepository;
import com.wooteco.sokdak.comment.domain.Comment;
import com.wooteco.sokdak.comment.dto.CommentResponse;
import com.wooteco.sokdak.comment.dto.NewCommentRequest;
import com.wooteco.sokdak.comment.dto.NewReplyRequest;
import com.wooteco.sokdak.comment.dto.ReplyResponse;
import com.wooteco.sokdak.comment.exception.ReplyDepthException;
import com.wooteco.sokdak.comment.repository.CommentRepository;
import com.wooteco.sokdak.member.domain.Member;
import com.wooteco.sokdak.member.repository.MemberRepository;
import com.wooteco.sokdak.member.util.RandomNicknameGenerator;
import com.wooteco.sokdak.post.domain.Post;
import com.wooteco.sokdak.post.repository.PostRepository;
import com.wooteco.sokdak.util.ServiceTest;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

class CommentServiceTest extends ServiceTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private PostBoardRepository postBoardRepository;

    private Post anonymousPost;
    private Post identifiedPost;
    private Member member2;
    private String randomNickname;
    private Board freeBoard;
    private Post applicantPost;

    @BeforeEach
    void setUp() {
        member2 = memberRepository.findById(4L).get();
        randomNickname = RandomNicknameGenerator.generate(new HashSet<>());
        anonymousPost = Post.builder()
                .member(member)
                .title(VALID_POST_TITLE)
                .content(VALID_POST_CONTENT)
                .writerNickname(randomNickname)
                .build();
        identifiedPost = Post.builder()
                .member(member)
                .title(VALID_POST_TITLE)
                .content(VALID_POST_CONTENT)
                .writerNickname(member.getNickname())
                .build();
        postRepository.save(anonymousPost);
        postRepository.save(identifiedPost);

        freeBoard = boardRepository.findById(FREE_BOARD_ID).get();
        PostBoard anonymousPostBoard = PostBoard.builder().build();
        anonymousPostBoard.addBoard(freeBoard);
        anonymousPostBoard.addPost(anonymousPost);
        PostBoard identifiedPostBoard = PostBoard.builder().build();
        identifiedPostBoard.addBoard(freeBoard);
        identifiedPostBoard.addPost(identifiedPost);
        postBoardRepository.save(anonymousPostBoard);
        postBoardRepository.save(identifiedPostBoard);

        applicantPost = Post.builder()
                .member(member)
                .title(VALID_POST_TITLE)
                .content(VALID_POST_CONTENT)
                .writerNickname(randomNickname)
                .build();
        postRepository.save(applicantPost);
        PostBoard postBoard = PostBoard.builder().build();
        postBoard.addPost(applicantPost);
        postBoard.addBoard(boardRepository.findById(APPLICANT_BOARD_ID).get());
        postBoardRepository.save(postBoard);
    }

    @DisplayName("자신이 작성한 글에 기명으로 댓글을 등록")
    @Test
    void addComment_Identified() {
        Long commentId = commentService.addComment(anonymousPost.getId(), NON_ANONYMOUS_COMMENT_REQUEST, AUTH_INFO);

        Comment foundComment = commentRepository.findById(commentId).orElseThrow();
        assertAll(
                () -> assertThat(foundComment.getMessage()).isEqualTo(NON_ANONYMOUS_COMMENT_REQUEST.getContent()),
                () -> assertThat(foundComment.getMember()).isEqualTo(member),
                () -> assertThat(foundComment.getNickname()).isEqualTo(member.getNickname())
        );
    }

    @DisplayName("지원자는 권한이 없는 게시판에 작성된 게시글에 댓글 작성할 수 없다.")
    @ParameterizedTest
    @CsvSource({"1", "2", "3", "4"})
    void addComment_Applicant_Exception(Long boardId) {
        NewCommentRequest newCommentRequest = new NewCommentRequest("content", true);
        assertThatThrownBy(() -> commentService.addComment(anonymousPost.getId(), newCommentRequest, APPLICANT_AUTH_INFO))
                .isInstanceOf(AuthorizationException.class);
    }

    @DisplayName("지원자는 권한이 있는 게시판에 작성된 게시글에 댓글을 작성할 수 있다.")
    @Test
    void addComment_Applicant() {
        NewCommentRequest newCommentRequest = new NewCommentRequest("content", true);

        Post applicantPost = Post.builder()
                .member(member)
                .title(VALID_POST_TITLE)
                .content(VALID_POST_CONTENT)
                .writerNickname(randomNickname)
                .build();
        postRepository.save(applicantPost);
        PostBoard postBoard = PostBoard.builder().build();
        postBoard.addPost(applicantPost);
        postBoard.addBoard(boardRepository.findById(APPLICANT_BOARD_ID).get());
        postBoardRepository.save(postBoard);

        Long commentId = commentService.addComment(applicantPost.getId(), newCommentRequest, APPLICANT_AUTH_INFO);
        Comment foundComment = commentRepository.findById(commentId).orElseThrow();
        assertAll(
                () -> assertThat(foundComment.getMessage()).isEqualTo(newCommentRequest.getContent()),
                () -> assertThat(foundComment.getMember().getId()).isEqualTo(APPLICANT_AUTH_INFO.getId())
        );
    }

    @DisplayName("대댓글 등록")
    @Test
    void addReply() {
        Long commentId = commentService.addComment(anonymousPost.getId(), NON_ANONYMOUS_COMMENT_REQUEST, AUTH_INFO);

        Long replyId = commentService.addReply(commentId, NON_ANONYMOUS_REPLY_REQUEST, AUTH_INFO);

        Comment comment = commentRepository.findById(commentId).orElseThrow();
        Comment reply = commentRepository.findById(replyId).orElseThrow();
        assertAll(
                () -> assertThat(reply.getMessage()).isEqualTo(NON_ANONYMOUS_REPLY_REQUEST.getContent()),
                () -> assertThat(reply.getParent()).isEqualTo(comment)
        );
    }

    @DisplayName("대댓글에 답글을 달면 예외 발생")
    @Test
    void addReply_Exception_Having_Parent() {
        Long commentId = commentService.addComment(anonymousPost.getId(), NON_ANONYMOUS_COMMENT_REQUEST, AUTH_INFO);
        Long replyId = commentService.addReply(commentId, NON_ANONYMOUS_REPLY_REQUEST, AUTH_INFO);

        assertThatThrownBy(() -> commentService.addReply(replyId, ANONYMOUS_REPLY_REQUEST, AUTH_INFO))
                .isInstanceOf(ReplyDepthException.class);
    }

    @DisplayName("지원자는 권한이 없는 게시판에 작성된 게시글에 달린 댓글에 대댓글을 작성할 수 없다.")
    @ParameterizedTest
    @CsvSource({"1", "2", "3", "4"})
    void addReply_Applicant_Exception(Long boardId) {
        Long commentId = commentService.addComment(anonymousPost.getId(), APPLICANT_COMMENT_REQUEST, AUTH_INFO);
        NewReplyRequest newReplyRequest = new NewReplyRequest(boardId, "content", true);
        assertThatThrownBy(() -> commentService.addReply(commentId, newReplyRequest, APPLICANT_AUTH_INFO))
                .isInstanceOf(AuthorizationException.class);
    }

    @DisplayName("지원자는 권한이 있는 게시판에 작성된 게시글에 달린 댓글에 대댓글을 작성할 수 있다.")
    @Test
    void addReply_Applicant() {
        Long commentId = commentService.addComment(applicantPost.getId(), APPLICANT_COMMENT_REQUEST, AUTH_INFO);
        NewReplyRequest newReplyRequest = new NewReplyRequest(APPLICANT_BOARD_ID, "content", true);

        Long replyId = commentService.addReply(commentId, newReplyRequest, APPLICANT_AUTH_INFO);
        Comment foundReply = commentRepository.findById(replyId).orElseThrow();
        assertAll(
                () -> assertThat(foundReply.getMessage()).isEqualTo(newReplyRequest.getContent()),
                () -> assertThat(foundReply.getMember().getId()).isEqualTo(APPLICANT_AUTH_INFO.getId())
        );
    }

    @DisplayName("익명 게시글에서 게시글 작성자가 기명으로 댓글 등록")
    @Test
    void addComment_Identified_PostWriterAnonymous_PostWriterCommentWriterSame() {
        Long commentId = commentService.addComment(anonymousPost.getId(), NON_ANONYMOUS_COMMENT_REQUEST, AUTH_INFO);

        Comment foundComment = commentRepository.findById(commentId).orElseThrow();

        assertAll(
                () -> assertThat(foundComment.getMessage()).isEqualTo(NON_ANONYMOUS_COMMENT_REQUEST.getContent()),
                () -> assertThat(foundComment.getMember()).isEqualTo(member),
                () -> assertThat(foundComment.getNickname()).isEqualTo(member.getNickname())
        );
    }

    @DisplayName("익명 게시글에서 게시글 작성자가 아닌 유저가 기명으로 댓글 등록")
    @Test
    void addComment_Identified_PostWriterAnonymous_PostWriterCommentWriterDifferent() {
        Long commentId = commentService.addComment(anonymousPost.getId(), NON_ANONYMOUS_COMMENT_REQUEST, AUTH_INFO2);

        Comment foundComment = commentRepository.findById(commentId).orElseThrow();

        assertAll(
                () -> assertThat(foundComment.getMessage()).isEqualTo(NON_ANONYMOUS_COMMENT_REQUEST.getContent()),
                () -> assertThat(foundComment.getMember()).isEqualTo(member2),
                () -> assertThat(foundComment.getNickname()).isEqualTo(member2.getNickname())
        );
    }

    @DisplayName("익명 게시글에서 게시글 작성자가 익명으로 댓글 등록")
    @Test
    void addComment_Anonymous_PostWriterAnonymous_PostWriterCommentWriterSame() {
        Long commentId = commentService.addComment(anonymousPost.getId(), ANONYMOUS_COMMENT_REQUEST, AUTH_INFO);

        Comment foundComment = commentRepository.findById(commentId).orElseThrow();

        assertAll(
                () -> assertThat(foundComment.getMessage()).isEqualTo(ANONYMOUS_COMMENT_REQUEST.getContent()),
                () -> assertThat(foundComment.getMember()).isEqualTo(member),
                () -> assertThat(foundComment.getNickname()).isEqualTo(randomNickname)
        );
    }

    @DisplayName("기명 게시글에서 게시글 작성자가 익명으로 댓글 등록")
    @Test
    void addComment_Anonymous_PostWriterIdentified_PostWriterCommentWriterSame() {
        Long commentId = commentService.addComment(identifiedPost.getId(), ANONYMOUS_COMMENT_REQUEST, AUTH_INFO);

        Comment foundComment = commentRepository.findById(commentId).orElseThrow();

        assertAll(
                () -> assertThat(foundComment.getMessage()).isEqualTo(ANONYMOUS_COMMENT_REQUEST.getContent()),
                () -> assertThat(foundComment.getMember()).isEqualTo(member),
                () -> assertThat(foundComment.getNickname()).isNotEqualTo(member.getNickname())
        );
    }

    @DisplayName("기명 게시글에서 게시글 작성자가 익명으로 댓글 등록을 여러번하면 동일한 닉네임이 할당된다.")
    @Test
    void addComment_Anonymous_PostWriterIdentified() {
        Long firstCommentId = commentService
                .addComment(identifiedPost.getId(), NON_ANONYMOUS_COMMENT_REQUEST, AUTH_INFO);
        Long secondCommentId = commentService
                .addComment(identifiedPost.getId(), NON_ANONYMOUS_COMMENT_REQUEST, AUTH_INFO);

        Comment firstFoundComment = commentRepository.findById(firstCommentId).orElseThrow();
        Comment secondFoundComment = commentRepository.findById(secondCommentId).orElseThrow();

        assertThat(firstFoundComment.getNickname()).isEqualTo(secondFoundComment.getNickname());
    }

    @DisplayName("익명 게시글에서 게시글 작성자 아닌 사용자가 익명으로 댓글 등록")
    @Test
    void addComment_Anonymous_PostWriterAnonymous_PostWriterCommentWriterDifferent() {
        Long commentId = commentService.addComment(anonymousPost.getId(), ANONYMOUS_COMMENT_REQUEST, AUTH_INFO2);

        Comment foundComment = commentRepository.findById(commentId).orElseThrow();

        assertAll(
                () -> assertThat(foundComment.getMessage()).isEqualTo(ANONYMOUS_COMMENT_REQUEST.getContent()),
                () -> assertThat(foundComment.getMember()).isEqualTo(member2),
                () -> assertThat(foundComment.getNickname()).isNotEqualTo(anonymousPost.getNickname())
        );
    }

    @DisplayName("기명 게시글에서 게시글 작성자가 아난 사용자가 익명으로 첫 댓글 작성")
    @Test
    void addComment_Anonymous_PostWriterIdentified_PostWriterCommentWriterDifferent_firstComment() {
        Long commentId = commentService.addComment(anonymousPost.getId(), ANONYMOUS_COMMENT_REQUEST, AUTH_INFO);

        Comment foundComment = commentRepository.findById(commentId).orElseThrow();

        assertAll(
                () -> assertThat(foundComment.getMessage()).isEqualTo(ANONYMOUS_COMMENT_REQUEST.getContent()),
                () -> assertThat(foundComment.getMember()).isEqualTo(member),
                () -> assertThat(foundComment.getNickname()).isIn(RandomNicknameGenerator.RANDOM_NICKNAMES)
        );
    }

    @DisplayName("익명 게시글에서 게시글 작성자가 익명으로 댓글을 단 후, 다시 익명으로 댓글 작성")
    @Test
    void addComment_Anonymous_PostWriterIdentified_PostWriterCommentWriterDifferent_SComment() {
        Long firstCommentId = commentService.addComment(anonymousPost.getId(), ANONYMOUS_COMMENT_REQUEST, AUTH_INFO);
        Long secondCommentId = commentService.addComment(anonymousPost.getId(), ANONYMOUS_COMMENT_REQUEST, AUTH_INFO);

        Comment firstComment = commentRepository.findById(firstCommentId).orElseThrow();
        Comment secondComment = commentRepository.findById(secondCommentId).orElseThrow();

        assertAll(
                () -> assertThat(firstComment.getNickname()).isEqualTo(secondComment.getNickname()),
                () -> assertThat(anonymousPost.getNickname()).isEqualTo(firstComment.getNickname())
        );
    }

    @DisplayName("특정 게시물의 달린 댓글을 가져옴")
    @Test
    void findComments() {
        Post otherPost = Post.builder()
                .member(member)
                .writerNickname(member.getNickname())
                .title("다른 게시글")
                .content("본문")
                .build();
        PostBoard postBoard = PostBoard.builder().build();
        postBoard.addPost(otherPost);
        postBoard.addBoard(freeBoard);

        postRepository.save(otherPost);
        NewCommentRequest newCommentRequestToOtherPost = new NewCommentRequest("다른 게시글의 댓글", true);
        commentService.addComment(anonymousPost.getId(), ANONYMOUS_COMMENT_REQUEST, AUTH_INFO);
        commentService.addComment(otherPost.getId(), newCommentRequestToOtherPost, AUTH_INFO);

        List<CommentResponse> commentResponses = commentService.findComments(anonymousPost.getId(), AUTH_INFO)
                .getComments();

        assertAll(
                () -> assertThat(commentResponses.size()).isEqualTo(1),
                () -> assertThat(commentResponses.get(0).getContent())
                        .isEqualTo(ANONYMOUS_COMMENT_REQUEST.getContent()),
                () -> assertThat(commentResponses.get(0).getLikeCount()).isZero()
        );
    }

    @DisplayName("특정 게시글의 댓글 목록을 가져오고 내가 작성한 댓글을 표시함")
    @Test
    void findComments_authorized() {
        Member otherMember = Member.builder()
                .username("otherUser")
                .password("test1234!")
                .nickname("다른유저")
                .build();
        memberRepository.save(otherMember);
        commentService.addComment(anonymousPost.getId(), ANONYMOUS_COMMENT_REQUEST, AUTH_INFO);
        commentService.addComment(
                anonymousPost.getId(), ANONYMOUS_COMMENT_REQUEST,
                new AuthInfo(otherMember.getId(), "USER", member.getNickname()));

        List<CommentResponse> commentResponses = commentService.findComments(anonymousPost.getId(), AUTH_INFO)
                .getComments();

        assertAll(
                () -> assertThat(commentResponses.get(0).isAuthorized()).isTrue(),
                () -> assertThat(commentResponses.get(1).isAuthorized()).isFalse()
        );
    }

    @DisplayName("댓글, 대댓글을 등록하고 해당 게시물의 댓글을 조회하면 댓글과 대댓글이 같이 조회된다.")
    @Test
    void findComments_Having_Reply() {
        NewReplyRequest newReplyRequest2 = new NewReplyRequest(FREE_BOARD_ID, "대댓글2", false);
        Long commentId = commentService.addComment(anonymousPost.getId(), NON_ANONYMOUS_COMMENT_REQUEST, AUTH_INFO);
        commentService.addReply(commentId, NON_ANONYMOUS_REPLY_REQUEST, AUTH_INFO);
        commentService.addReply(commentId, newReplyRequest2, AUTH_INFO);

        CommentResponse commentResponse = commentService.findComments(anonymousPost.getId(), AUTH_INFO).getComments()
                .get(0);

        List<ReplyResponse> replyResponses = commentResponse.getReplies();
        assertAll(
                () -> assertThat(commentResponse.getId()).isEqualTo(commentId),
                () -> assertThat(commentResponse.getContent()).isEqualTo(NON_ANONYMOUS_COMMENT_REQUEST.getContent()),
                () -> assertThat(replyResponses).hasSize(2),
                () -> assertThat(replyResponses.get(0).getContent())
                        .isEqualTo(NON_ANONYMOUS_REPLY_REQUEST.getContent()),
                () -> assertThat(replyResponses.get(1).getContent()).isEqualTo(newReplyRequest2.getContent())
        );
    }

    @DisplayName("댓글 삭제")
    @Test
    void deleteComment() {
        Long commentId = commentService.addComment(anonymousPost.getId(), ANONYMOUS_COMMENT_REQUEST, AUTH_INFO);

        commentService.deleteComment(commentId, new AuthInfo(member.getId(), USER.getName(), member.getNickname()));

        assertThat(commentRepository.findById(commentId)).isEmpty();
    }

    @DisplayName("댓글 작성자가 아닌 유저가 댓글을 삭제하면 예외 발생")
    @Test
    void deleteComment_Exception_NotOwner() {
        Long commentId = commentService.addComment(anonymousPost.getId(), ANONYMOUS_COMMENT_REQUEST, AUTH_INFO);
        Long invalidOwnerId = 9999L;

        assertThatThrownBy(() -> commentService
                .deleteComment(commentId, new AuthInfo(invalidOwnerId, USER.getName(), member.getNickname())))
                .isInstanceOf(AuthorizationException.class);
    }

    @DisplayName("대댓글이 존재하는 댓글을 삭제하면 해당 댓글은 완전 삭제 되지 않고 softRemoved 필드를 true로 변환한다.")
    @Test
    void deleteComment_Having_Reply() {
        Long commentId = commentService.addComment(anonymousPost.getId(), NON_ANONYMOUS_COMMENT_REQUEST, AUTH_INFO);
        commentService.addReply(commentId, NON_ANONYMOUS_REPLY_REQUEST, AUTH_INFO);

        commentService.deleteComment(commentId, AUTH_INFO);

        Comment comment = commentRepository.findById(commentId).orElseThrow();
        assertThat(comment.isSoftRemoved()).isTrue();
    }

    @DisplayName("대댓글이 없는 댓글을 삭제하면 해당 댓글은 완전 삭제된다.")
    @Test
    void deleteComment_Without_Reply() {
        Long commentId = commentService.addComment(anonymousPost.getId(), NON_ANONYMOUS_COMMENT_REQUEST, AUTH_INFO);

        commentService.deleteComment(commentId, AUTH_INFO);

        Optional<Comment> comment = commentRepository.findById(commentId);
        assertThat(comment).isEmpty();
    }

    @DisplayName("대댓글 삭제")
    @Test
    void deleteComment_Reply() {
        Long commentId = commentService.addComment(anonymousPost.getId(), NON_ANONYMOUS_COMMENT_REQUEST, AUTH_INFO);
        Long replyId = commentService.addReply(commentId, NON_ANONYMOUS_REPLY_REQUEST, AUTH_INFO);

        commentService.deleteComment(replyId, AUTH_INFO);

        Optional<Comment> reply = commentRepository.findById(replyId);
        assertThat(reply).isEmpty();
    }

    @DisplayName("대댓글이 달린 상태에서 댓글을 삭제하고, 대댓글 삭제 후 더이상 댓글에 대댓글이 없는 경우 댓글도 완전 삭제")
    @Test
    void deleteComment_Reply_Empty_Children() {
        Long commentId = commentService.addComment(anonymousPost.getId(), NON_ANONYMOUS_COMMENT_REQUEST, AUTH_INFO);
        Long replyId = commentService.addReply(commentId, NON_ANONYMOUS_REPLY_REQUEST, AUTH_INFO);
        commentService.deleteComment(commentId, AUTH_INFO);

        commentService.deleteComment(replyId, AUTH_INFO);

        Optional<Comment> comment = commentRepository.findById(commentId);
        Optional<Comment> reply = commentRepository.findById(replyId);
        assertAll(
                () -> assertThat(comment).isEmpty(),
                () -> assertThat(reply).isEmpty()
        );
    }

    @DisplayName("댓글을 삭제하지 않고, 대댓글 삭제후 더이상 댓글에 대댓글이 없는 경우엔 댓글은 그대로이다.")
    @Test
    void deleteComment_Reply_Without_Deleting_Comment() {
        Long commentId = commentService.addComment(anonymousPost.getId(), NON_ANONYMOUS_COMMENT_REQUEST, AUTH_INFO);
        Long replyId = commentService.addReply(commentId, NON_ANONYMOUS_REPLY_REQUEST, AUTH_INFO);

        commentService.deleteComment(replyId, AUTH_INFO);

        Comment comment = commentRepository.findById(commentId).orElseThrow();
        Optional<Comment> reply = commentRepository.findById(replyId);
        assertAll(
                () -> assertThat(comment.getId()).isEqualTo(commentId),
                () -> assertThat(comment.getMessage()).isEqualTo(NON_ANONYMOUS_COMMENT_REQUEST.getContent()),
                () -> assertThat(reply).isEmpty()
        );
    }
}
