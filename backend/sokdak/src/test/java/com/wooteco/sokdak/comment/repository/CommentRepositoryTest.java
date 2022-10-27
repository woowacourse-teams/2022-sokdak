package com.wooteco.sokdak.comment.repository;

import static com.wooteco.sokdak.util.fixture.PostFixture.VALID_POST_CONTENT;
import static com.wooteco.sokdak.util.fixture.PostFixture.VALID_POST_TITLE;
import static org.assertj.core.api.Assertions.assertThat;

import com.wooteco.sokdak.comment.domain.Comment;
import com.wooteco.sokdak.post.domain.Post;
import com.wooteco.sokdak.post.repository.PostRepository;
import com.wooteco.sokdak.util.RepositoryTest;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class CommentRepositoryTest extends RepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    private Post post;
    private Comment comment1;
    private Comment comment2;

    @BeforeEach
    void setUp() {
        post = Post.builder()
                .member(member1)
                .title(VALID_POST_TITLE)
                .content(VALID_POST_CONTENT)
                .build();
        postRepository.save(post);
        comment1 = Comment.parent(member1, post, "east", "댓글");
        comment2 = Comment.parent(member1, post, "east", "댓글2");
        commentRepository.save(comment1);
        commentRepository.save(comment2);
    }

    @DisplayName("게시물에 해당하는 모든 댓글을 가져온다.")
    @Test
    void findAllByPostId() {
        List<Comment> comments = commentRepository.findCommentsByPostId(post.getId());

        assertThat(comments).hasSize(2);
    }

    @DisplayName("좋아요 개수를 1 증가한다.")
    @Test
    void increaseLikeCount() {
        int originLikeCount = comment1.getCommentLikesCount();

        commentRepository.increaseLikeCount(comment1.getId());

        Comment foundComment = commentRepository.findById(comment1.getId()).orElseThrow();
        assertThat(foundComment.getCommentLikesCount() - originLikeCount).isEqualTo(1);
    }

    @DisplayName("좋아요 개수를 1 감소한다.")
    @Test
    void decreaseLikeCount() {
        commentRepository.increaseLikeCount(comment1.getId());
        int originLikeCount = commentRepository.findById(comment1.getId()).get().getCommentLikesCount();

        commentRepository.decreaseLikeCount(comment1.getId());

        Comment foundComment = commentRepository.findById(comment1.getId()).orElseThrow();
        assertThat(foundComment.getCommentLikesCount() - originLikeCount).isEqualTo(-1);
    }
}
