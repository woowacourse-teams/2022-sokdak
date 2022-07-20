package com.wooteco.sokdak.comment.service;

import static com.wooteco.sokdak.post.util.PostFixture.VALID_POST_CONTENT;
import static com.wooteco.sokdak.post.util.PostFixture.VALID_POST_TITLE;
import static com.wooteco.sokdak.util.fixture.MemberFixture.AUTH_INFO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.wooteco.sokdak.comment.domain.Comment;
import com.wooteco.sokdak.comment.dto.NewCommentRequest;
import com.wooteco.sokdak.comment.repository.CommentRepository;
import com.wooteco.sokdak.member.domain.Member;
import com.wooteco.sokdak.member.repository.MemberRepository;
import com.wooteco.sokdak.member.util.RandomNicknameGenerator;
import com.wooteco.sokdak.post.domain.Post;
import com.wooteco.sokdak.post.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    private Post post;
    private Member member;

    @BeforeEach
    void setUp() {
        member = memberRepository.findById(1L).get();
        post = Post.builder()
                .member(member)
                .title(VALID_POST_TITLE)
                .content(VALID_POST_CONTENT)
                .build();
        postRepository.save(post);
    }

    @DisplayName("기명으로 댓글을 등록")
    @Test
    void addComment_Identified() {
        NewCommentRequest newCommentRequest = new NewCommentRequest("댓글", false);

        Long commentId = commentService.addComment(post.getId(), newCommentRequest, AUTH_INFO);

        Comment foundComment = commentRepository.findById(commentId).get();
        assertAll(
                () -> assertThat(foundComment.getMessage()).isEqualTo("댓글"),
                () -> assertThat(foundComment.getMember()).isEqualTo(member)
        );
    }

    @DisplayName("익명으로 첫 댓글 작성")
    @Test
    void addComment_FirstAnonymous() {
        NewCommentRequest newCommentRequest = new NewCommentRequest("댓글", true);

        Long commentId = commentService.addComment(post.getId(), newCommentRequest, AUTH_INFO);

        Comment foundComment = commentRepository.findById(commentId).get();
        assertAll(
                () -> assertThat(foundComment.getMessage()).isEqualTo("댓글"),
                () -> assertThat(foundComment.getMember()).isEqualTo(member),
                () -> assertThat(foundComment.getNickname()).isIn(RandomNicknameGenerator.RANDOM_NICKNAMES)
        );
    }

    @DisplayName("익명으로 댓글을 단 후, 다시 익명으로 댓글 작성")
    @Test
    void addComment_SecondAnonymous() {
        NewCommentRequest newCommentRequest = new NewCommentRequest("댓글", true);
        Long firstCommentId = commentService.addComment(post.getId(), newCommentRequest, AUTH_INFO);
        Long secondCommentId = commentService.addComment(post.getId(), newCommentRequest, AUTH_INFO);

        Comment firstComment = commentRepository.findById(firstCommentId).get();
        Comment secondComment = commentRepository.findById(secondCommentId).get();

        assertThat(secondComment.getNickname()).isEqualTo(firstComment.getNickname());
    }
}
