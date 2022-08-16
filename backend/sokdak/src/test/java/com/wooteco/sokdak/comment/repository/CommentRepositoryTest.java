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

    @BeforeEach
    void setUp() {
        post = Post.builder()
                .member(member1)
                .title(VALID_POST_TITLE)
                .content(VALID_POST_CONTENT)
                .build();
        postRepository.save(post);
    }

    @DisplayName("게시물에 해당하는 모든 댓글을 가져온다.")
    @Test
    void findAllByPostId() {
        Comment comment1 = Comment.builder()
                .member(member1)
                .post(post)
                .message("댓글")
                .nickname("josh")
                .build();
        Comment comment2 = Comment.builder()
                .member(member1)
                .post(post)
                .message("댓글2")
                .nickname("josh")
                .build();
        commentRepository.save(comment1);
        commentRepository.save(comment2);

        List<Comment> comments = commentRepository.findAllByPostId(post.getId());

        assertThat(comments).hasSize(2);
    }
}
