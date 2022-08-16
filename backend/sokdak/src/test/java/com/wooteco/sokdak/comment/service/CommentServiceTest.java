package com.wooteco.sokdak.comment.service;

import static com.wooteco.sokdak.member.domain.RoleType.USER;
import static com.wooteco.sokdak.util.fixture.PostFixture.VALID_POST_CONTENT;
import static com.wooteco.sokdak.util.fixture.PostFixture.VALID_POST_TITLE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.wooteco.sokdak.auth.dto.AuthInfo;
import com.wooteco.sokdak.auth.exception.AuthenticationException;
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
import com.wooteco.sokdak.util.IntegrationTest;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class CommentServiceTest extends IntegrationTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    private Post anonymousPost;
    private Post identifiedPost;
    private Member member2;
    private String randomNickname;

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
    }

    @DisplayName("자신이 작성한 글에 기명으로 댓글을 등록")
    @Test
    void addComment_Identified() {
        NewCommentRequest newCommentRequest = new NewCommentRequest("댓글", false);

        Long commentId = commentService.addComment(anonymousPost.getId(), newCommentRequest, AUTH_INFO);

        Comment foundComment = commentRepository.findById(commentId).get();
        assertAll(
                () -> assertThat(foundComment.getMessage()).isEqualTo("댓글"),
                () -> assertThat(foundComment.getMember()).isEqualTo(member),
                () -> assertThat(foundComment.getNickname()).isEqualTo(member.getNickname())
        );
    }

    @DisplayName("대댓글 등록")
    @Test
    void addReply() {
        NewCommentRequest newCommentRequest = new NewCommentRequest("댓글", false);
        NewReplyRequest newReplyRequest = new NewReplyRequest("대댓글", false);
        Long commentId = commentService.addComment(anonymousPost.getId(), newCommentRequest, AUTH_INFO);

        Long replyId = commentService.addReply(commentId, newReplyRequest, AUTH_INFO);

        Comment comment = commentRepository.findById(commentId).get();
        Comment reply = commentRepository.findById(replyId).get();
        assertAll(
                () -> assertThat(reply.getMessage()).isEqualTo("대댓글"),
                () -> assertThat(reply.getParent()).isEqualTo(comment)
        );
    }

    @DisplayName("대댓글에 답글을 달면 예외 발생")
    @Test
    void addReply_Exception_Having_Parent() {
        NewCommentRequest newCommentRequest = new NewCommentRequest("댓글", false);
        NewReplyRequest newReplyRequest = new NewReplyRequest("대댓글", false);
        NewReplyRequest newReplyRequest2 = new NewReplyRequest("대대댓글", false);
        Long commentId = commentService.addComment(anonymousPost.getId(), newCommentRequest, AUTH_INFO);
        Long replyId = commentService.addReply(commentId, newReplyRequest, AUTH_INFO);

        assertThatThrownBy(() -> commentService.addReply(replyId, newReplyRequest2, AUTH_INFO))
                .isInstanceOf(ReplyDepthException.class);
    }

    @DisplayName("익명 게시글에서 게시글 작성자가 기명으로 댓글 등록")
    @Test
    void addComment_Identified_PostWriterAnonymous_PostWriterCommentWriterSame() {
        NewCommentRequest newCommentRequest = new NewCommentRequest("댓글", false);
        Long commentId = commentService.addComment(anonymousPost.getId(), newCommentRequest, AUTH_INFO);

        Comment foundComment = commentRepository.findById(commentId).get();

        assertAll(
                () -> assertThat(foundComment.getMessage()).isEqualTo("댓글"),
                () -> assertThat(foundComment.getMember()).isEqualTo(member),
                () -> assertThat(foundComment.getNickname()).isEqualTo(member.getNickname())
        );
    }

    @DisplayName("익명 게시글에서 게시글 작성자가 아닌 유저가 기명으로 댓글 등록")
    @Test
    void addComment_Identified_PostWriterAnonymous_PostWriterCommentWriterDifferent() {
        NewCommentRequest newCommentRequest = new NewCommentRequest("댓글", false);
        Long commentId = commentService.addComment(anonymousPost.getId(), newCommentRequest, AUTH_INFO2);

        Comment foundComment = commentRepository.findById(commentId).get();

        assertAll(
                () -> assertThat(foundComment.getMessage()).isEqualTo("댓글"),
                () -> assertThat(foundComment.getMember()).isEqualTo(member2),
                () -> assertThat(foundComment.getNickname()).isEqualTo(member2.getNickname())
        );
    }

    @DisplayName("익명 게시글에서 게시글 작성자가 익명으로 댓글 등록")
    @Test
    void addComment_Anonymous_PostWriterAnonymous_PostWriterCommentWriterSame() {
        NewCommentRequest newCommentRequest = new NewCommentRequest("댓글", true);
        Long commentId = commentService.addComment(anonymousPost.getId(), newCommentRequest, AUTH_INFO);

        Comment foundComment = commentRepository.findById(commentId).get();

        assertAll(
                () -> assertThat(foundComment.getMessage()).isEqualTo("댓글"),
                () -> assertThat(foundComment.getMember()).isEqualTo(member),
                () -> assertThat(foundComment.getNickname()).isEqualTo(randomNickname)
        );
    }

    @DisplayName("기명 게시글에서 게시글 작성자가 익명으로 댓글 등록")
    @Test
    void addComment_Anonymous_PostWriterIdentified_PostWriterCommentWriterSame() {
        NewCommentRequest newCommentRequest = new NewCommentRequest("댓글", true);
        Long commentId = commentService.addComment(identifiedPost.getId(), newCommentRequest, AUTH_INFO);

        Comment foundComment = commentRepository.findById(commentId).get();

        assertAll(
                () -> assertThat(foundComment.getMessage()).isEqualTo("댓글"),
                () -> assertThat(foundComment.getMember()).isEqualTo(member),
                () -> assertThat(foundComment.getNickname()).isNotEqualTo(member.getNickname())
        );
    }

    @DisplayName("익명 게시글에서 게시글 작성자 아닌 사용자가 익명으로 댓글 등록")
    @Test
    void addComment_Anonymous_PostWriterAnonymous_PostWriterCommentWriterDifferent() {
        NewCommentRequest newCommentRequest = new NewCommentRequest("댓글", true);
        Long commentId = commentService.addComment(anonymousPost.getId(), newCommentRequest, AUTH_INFO2);

        Comment foundComment = commentRepository.findById(commentId).get();

        assertAll(
                () -> assertThat(foundComment.getMessage()).isEqualTo("댓글"),
                () -> assertThat(foundComment.getMember()).isEqualTo(member2),
                () -> assertThat(foundComment.getNickname()).isNotEqualTo(anonymousPost.getNickname())
        );
    }

    @DisplayName("기명 게시글에서 게시글 작성자가 아난 사용자가 익명으로 첫 댓글 작성")
    @Test
    void addComment_Anonymous_PostWriterIdentified_PostWriterCommentWriterDifferent_firstComment() {
        NewCommentRequest newCommentRequest = new NewCommentRequest("댓글", true);
        Long commentId = commentService.addComment(anonymousPost.getId(), newCommentRequest, AUTH_INFO);

        Comment foundComment = commentRepository.findById(commentId).get();

        assertAll(
                () -> assertThat(foundComment.getMessage()).isEqualTo("댓글"),
                () -> assertThat(foundComment.getMember()).isEqualTo(member),
                () -> assertThat(foundComment.getNickname()).isIn(RandomNicknameGenerator.RANDOM_NICKNAMES)
        );
    }

    @DisplayName("익명 게시글에서 게시글 작성자가 익명으로 댓글을 단 후, 다시 익명으로 댓글 작성")
    @Test
    void addComment_Anonymous_PostWriterIdentified_PostWriterCommentWriterDifferent_SComment() {
        NewCommentRequest newCommentRequest = new NewCommentRequest("댓글", true);
        Long firstCommentId = commentService.addComment(anonymousPost.getId(), newCommentRequest, AUTH_INFO);
        Long secondCommentId = commentService.addComment(anonymousPost.getId(), newCommentRequest, AUTH_INFO);

        Comment firstComment = commentRepository.findById(firstCommentId).get();
        Comment secondComment = commentRepository.findById(secondCommentId).get();

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
        postRepository.save(otherPost);
        NewCommentRequest newCommentRequest = new NewCommentRequest("댓글", true);
        NewCommentRequest newCommentRequestToOtherPost = new NewCommentRequest("다른 게시글의 댓글", true);
        commentService.addComment(anonymousPost.getId(), newCommentRequest, AUTH_INFO);
        commentService.addComment(otherPost.getId(), newCommentRequestToOtherPost, AUTH_INFO);

        List<CommentResponse> commentResponses = commentService.findComments(anonymousPost.getId(), AUTH_INFO)
                .getComments();

        assertAll(
                () -> assertThat(commentResponses.size()).isEqualTo(1),
                () -> assertThat(commentResponses.get(0).getContent()).isEqualTo("댓글")
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
        NewCommentRequest newCommentRequest = new NewCommentRequest("댓글", true);
        commentService.addComment(anonymousPost.getId(), newCommentRequest, AUTH_INFO);
        commentService.addComment(
                anonymousPost.getId(), newCommentRequest,
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
        NewCommentRequest newCommentRequest = new NewCommentRequest("댓글", false);
        NewReplyRequest newReplyRequest = new NewReplyRequest("대댓글", false);
        NewReplyRequest newReplyRequest2 = new NewReplyRequest("대댓글2", false);
        Long commentId = commentService.addComment(anonymousPost.getId(), newCommentRequest, AUTH_INFO);
        commentService.addReply(commentId, newReplyRequest, AUTH_INFO);
        commentService.addReply(commentId, newReplyRequest2, AUTH_INFO);

        CommentResponse commentResponse = commentService.findComments(anonymousPost.getId(), AUTH_INFO).getComments()
                .get(0);
        List<ReplyResponse> replyResponses = commentResponse.getReplies();
        assertAll(
                () -> assertThat(commentResponse.getId()).isEqualTo(commentId),
                () -> assertThat(commentResponse.getContent()).isEqualTo("댓글"),
                () -> assertThat(replyResponses).hasSize(2),
                () -> assertThat(replyResponses.get(0).getContent()).isEqualTo("대댓글"),
                () -> assertThat(replyResponses.get(1).getContent()).isEqualTo("대댓글2")
        );
    }

    @DisplayName("댓글 삭제")
    @Test
    void deleteComment() {
        NewCommentRequest newCommentRequest = new NewCommentRequest("댓글", true);
        Long commentId = commentService.addComment(anonymousPost.getId(), newCommentRequest, AUTH_INFO);

        commentService.deleteComment(commentId, new AuthInfo(member.getId(), USER.getName(), member.getNickname()));

        assertThat(commentRepository.findById(commentId)).isEmpty();
    }

    @DisplayName("댓글 작성자가 아닌 유저가 댓글을 삭제하면 예외 발생")
    @Test
    void deleteComment_Exception_NotOwner() {
        NewCommentRequest newCommentRequest = new NewCommentRequest("댓글", true);
        Long commentId = commentService.addComment(anonymousPost.getId(), newCommentRequest, AUTH_INFO);
        Long invalidOwnerId = 9999L;

        assertThatThrownBy(() -> commentService
                .deleteComment(commentId, new AuthInfo(invalidOwnerId, USER.getName(), member.getNickname())))
                .isInstanceOf(AuthenticationException.class);
    }

    @DisplayName("대댓글이 존재하는 댓글을 삭제하면 해당 댓글은 완전 삭제 되지 않고 softRemoved 필드를 true로 변환한다.")
    @Test
    void deleteComment_Having_Reply() {
        NewCommentRequest newCommentRequest = new NewCommentRequest("댓글", false);
        NewReplyRequest newReplyRequest = new NewReplyRequest("대댓글", false);
        Long commentId = commentService.addComment(anonymousPost.getId(), newCommentRequest, AUTH_INFO);
        commentService.addReply(commentId, newReplyRequest, AUTH_INFO);

        commentService.deleteComment(commentId, AUTH_INFO);

        Comment comment = commentRepository.findById(commentId).get();
        assertThat(comment.isSoftRemoved()).isTrue();
    }

    @DisplayName("대댓글이 없는 댓글을 삭제하면 해당 댓글은 완전 삭제된다.")
    @Test
    void deleteComment_Without_Reply() {
        NewCommentRequest newCommentRequest = new NewCommentRequest("댓글", false);
        Long commentId = commentService.addComment(anonymousPost.getId(), newCommentRequest, AUTH_INFO);

        commentService.deleteComment(commentId, AUTH_INFO);

        Optional<Comment> comment = commentRepository.findById(commentId);
        assertThat(comment).isEmpty();
    }

    @DisplayName("대댓글 삭제")
    @Test
    void deleteComment_Reply() {
        NewCommentRequest newCommentRequest = new NewCommentRequest("댓글", false);
        NewReplyRequest newReplyRequest = new NewReplyRequest("대댓글", false);
        Long commentId = commentService.addComment(anonymousPost.getId(), newCommentRequest, AUTH_INFO);
        Long replyId = commentService.addReply(commentId, newReplyRequest, AUTH_INFO);

        commentService.deleteComment(replyId, AUTH_INFO);

        Optional<Comment> reply = commentRepository.findById(replyId);
        assertThat(reply).isEmpty();
    }

    @DisplayName("대댓글이 달린 상태에서 댓글을 삭제하고, 대댓글 삭제 후 더이상 댓글에 대댓글이 없는 경우 댓글도 완전 삭제")
    @Test
    void deleteComment_Reply_Empty_Children() {
        NewCommentRequest newCommentRequest = new NewCommentRequest("댓글", false);
        NewReplyRequest newReplyRequest = new NewReplyRequest("대댓글", false);
        Long commentId = commentService.addComment(anonymousPost.getId(), newCommentRequest, AUTH_INFO);
        Long replyId = commentService.addReply(commentId, newReplyRequest, AUTH_INFO);
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
        NewCommentRequest newCommentRequest = new NewCommentRequest("댓글", false);
        NewReplyRequest newReplyRequest = new NewReplyRequest("대댓글", false);
        Long commentId = commentService.addComment(anonymousPost.getId(), newCommentRequest, AUTH_INFO);
        Long replyId = commentService.addReply(commentId, newReplyRequest, AUTH_INFO);

        commentService.deleteComment(replyId, AUTH_INFO);

        Comment comment = commentRepository.findById(commentId).get();
        Optional<Comment> reply = commentRepository.findById(replyId);
        assertAll(
                () -> assertThat(comment.getId()).isEqualTo(commentId),
                () -> assertThat(comment.getMessage()).isEqualTo("댓글"),
                () -> assertThat(reply).isEmpty()
        );
    }
}
