package com.wooteco.sokdak.report.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.wooteco.sokdak.comment.domain.Comment;
import com.wooteco.sokdak.comment.repository.CommentRepository;
import com.wooteco.sokdak.post.domain.Post;
import com.wooteco.sokdak.post.repository.PostRepository;
import com.wooteco.sokdak.report.domain.CommentReport;
import com.wooteco.sokdak.util.RepositoryTest;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class CommentReportRepositoryTest extends RepositoryTest {

    @Autowired
    private CommentReportRepository commentReportRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    private Post post;
    private Comment comment;

    @BeforeEach
    void setUp() {
        post = Post.builder()
                .member(member1)
                .title("title")
                .content("content")
                .writerNickname("nickName")
                .postHashtags(new ArrayList<>())
                .postLikes(new ArrayList<>())
                .comments(new ArrayList<>())
                .build();
        postRepository.save(post);

        comment = Comment.builder()
                .member(member1)
                .post(post)
                .nickname("nickName")
                .message("message")
                .build();
        commentRepository.save(comment);
    }

    @Test
    @DisplayName("특정 commentId와 memberId를 가지는 데이터가 있으면 true를 반환한다.")
    void existsCommentReportByCommentAndReporter_True() {
        CommentReport commentReport = CommentReport.builder()
                .reporter(member1)
                .reportMessage("report")
                .comment(comment)
                .build();
        commentReportRepository.save(commentReport);

        assertThat(commentReportRepository.existsCommentReportByCommentAndReporter(comment, member1))
                .isTrue();
    }

    @Test
    @DisplayName("특정 commentId와 memberId를 가지는 데이터가 없으면 false를 반환한다.")
    void existsCommentReportByCommentAndReporter_False() {
        assertThat(commentReportRepository.existsCommentReportByCommentAndReporter(comment, member1))
                .isFalse();
    }
}
