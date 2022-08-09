package com.wooteco.sokdak.comment.service;

import static com.wooteco.sokdak.member.domain.RoleType.USER;
import static com.wooteco.sokdak.util.fixture.MemberFixture.AUTH_INFO;
import static com.wooteco.sokdak.util.fixture.MemberFixture.AUTH_INFO2;
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
import com.wooteco.sokdak.comment.repository.CommentRepository;
import com.wooteco.sokdak.member.domain.Member;
import com.wooteco.sokdak.member.repository.MemberRepository;
import com.wooteco.sokdak.member.util.RandomNicknameGenerator;
import com.wooteco.sokdak.notification.repository.NotificationRepository;
import com.wooteco.sokdak.post.domain.Post;
import com.wooteco.sokdak.post.repository.PostRepository;
import com.wooteco.sokdak.util.IntegrationTest;
import java.util.HashSet;
import java.util.List;
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

    @Autowired
    private NotificationRepository notificationRepository;

    private Post anonymousPost;
    private Post identifiedPost;
    private Member member;
    private Member member2;
    private String randomNickname;

    @BeforeEach
    void setUp() {
        member = memberRepository.findById(1L).get();
        member2 = memberRepository.findById(2L).get();
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

    @DisplayName("기명으로 댓글을 등록")
    @Test
    void addComment_Identified() {
        NewCommentRequest newCommentRequest = new NewCommentRequest("댓글", false);

        Long commentId = commentService.addComment(anonymousPost.getId(), newCommentRequest, AUTH_INFO);

        Comment foundComment = commentRepository.findById(commentId).get();
        assertAll(
                () -> assertThat(foundComment.getMessage()).isEqualTo("댓글"),
                () -> assertThat(foundComment.getMember()).isEqualTo(member),
                () -> assertThat(foundComment.getNickname()).isEqualTo(member.getNickname()),
                () -> assertNewNotification(foundComment)
        );
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
                () -> assertThat(foundComment.getNickname()).isEqualTo(member.getNickname()),
                () -> assertNewNotification(foundComment)
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
                () -> assertThat(foundComment.getNickname()).isEqualTo(member2.getNickname()),
                () -> assertNewNotification(foundComment)
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
                () -> assertThat(foundComment.getNickname()).isEqualTo(randomNickname),
                () -> assertNewNotification(foundComment)
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
                () -> assertThat(foundComment.getNickname()).isNotEqualTo(member.getNickname()),
                () -> assertNewNotification(foundComment)
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
                () -> assertThat(foundComment.getNickname()).isNotEqualTo(anonymousPost.getNickname()),
                () -> assertNewNotification(foundComment)
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
                () -> assertThat(foundComment.getNickname()).isIn(RandomNicknameGenerator.RANDOM_NICKNAMES),
                () -> assertNewNotification(foundComment)
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

    private void assertNewNotification(Comment comment) {
        Post post = comment.getPost();
        boolean newNotification = notificationRepository.existsByMemberIdAndInquiredIsFalse(post.getMember().getId());
        assertThat(newNotification).isTrue();
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
}
