package com.wooteco.sokdak.report.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.wooteco.sokdak.member.domain.Member;
import com.wooteco.sokdak.member.domain.Nickname;
import com.wooteco.sokdak.member.domain.Password;
import com.wooteco.sokdak.member.domain.Username;
import com.wooteco.sokdak.post.domain.Post;
import com.wooteco.sokdak.post.repository.PostRepository;
import com.wooteco.sokdak.report.domain.PostReport;
import com.wooteco.sokdak.util.RepositoryTest;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class PostReportRepositoryTest extends RepositoryTest {

    @Autowired
    private PostReportRepository postReportRepository;

    @Autowired
    private PostRepository postRepository;

    private Post post;

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
    }

    @Test
    @DisplayName("특정 postId와 memberId를 가지는 데이터가 있으면 true를 반환한다.")
    void existsPostReportByPostIdAndMemberId_True() {
        PostReport postReport = PostReport.builder()
                .reporter(member1)
                .post(post)
                .reportMessage("report")
                .build();
        postReportRepository.save(postReport);

        assertThat(postReportRepository.existsPostReportByPostAndReporter(post, member1))
                .isTrue();
    }

    @Test
    @DisplayName("특정 postId와 memberId를 가지는 데이터가 없으면 false를 반환한다.")
    void existsPostReportByPostIdAndMemberId_False() {
        assertThat(postReportRepository.existsPostReportByPostAndReporter(post, member1))
                .isFalse();
    }

    @Test
    @DisplayName("특정 postId를 가지는 데이터 개수를 반환한다.")
    void countByPostId() {
        int expected = 3;
        for (int i = 0; i < expected; ++i) {
            Member member = Member.builder()
                    .username(Username.of(encryptor, "username" + i))
                    .nickname(new Nickname("nickname" + i))
                    .password(Password.of(encryptor, "Abcd123!@"))
                    .build();
            memberRepository.save(member);
            PostReport postReport = PostReport.builder()
                    .post(post)
                    .reporter(member)
                    .reportMessage("report")
                    .build();
            postReportRepository.save(postReport);
        }

        int result = post.getPostReports().size();

        assertThat(expected).isEqualTo(result);
    }
}
