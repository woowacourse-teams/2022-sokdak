package com.wooteco.sokdak.report.service;

import static com.wooteco.sokdak.post.util.PostFixture.*;
import static com.wooteco.sokdak.util.fixture.MemberFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.wooteco.sokdak.auth.dto.AuthInfo;
import com.wooteco.sokdak.member.domain.Member;
import com.wooteco.sokdak.member.repository.MemberRepository;
import com.wooteco.sokdak.post.domain.Post;
import com.wooteco.sokdak.post.repository.PostRepository;
import com.wooteco.sokdak.report.dto.ReportRequest;
import com.wooteco.sokdak.report.exception.AlreadyReportPostException;
import com.wooteco.sokdak.report.exception.InvalidReportMessageException;
import com.wooteco.sokdak.report.repository.PostReportRepository;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class PostReportServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostReportRepository postReportRepository;

    @Autowired
    private PostReportService postReportService;

    private Member member;
    private Post post;

    @BeforeEach
    void setUp() {
        member = Member.builder()
                .username(VALID_USERNAME)
                .nickname(VALID_NICKNAME)
                .password(VALID_PASSWORD)
                .build();
        memberRepository.save(member);

        post = Post.builder()
                .member(member)
                .title(VALID_POST_TITLE)
                .content(VALID_POST_CONTENT)
                .postHashtags(Collections.emptyList())
                .comments(Collections.emptyList())
                .likes(Collections.emptyList())
                .build();
        postRepository.save(post);
    }

    @DisplayName("글 신고 기능")
    @Test
    void reportPost() {
        ReportRequest reportRequest = new ReportRequest("나쁜글");
        int reportCountBeforeReport = postReportRepository.countByPostId(post.getId());

        postReportService.reportPost(post.getId(), reportRequest, new AuthInfo(member.getId()));
        int reportCountAfterReport = postReportRepository.countByPostId(post.getId());

        assertThat(reportCountBeforeReport + 1).isEqualTo(reportCountAfterReport);
    }

    @DisplayName("이미 신고한 게시글을 신고 시 예외발생")
    @Test
    void reportPost_Exception_Already_Report() {
        ReportRequest reportRequest = new ReportRequest("나쁜글");
        postReportService.reportPost(post.getId(), reportRequest, new AuthInfo(member.getId()));

        assertThatThrownBy(() -> postReportService.reportPost(post.getId(), reportRequest, new AuthInfo(member.getId())))
                .isInstanceOf(AlreadyReportPostException.class);
    }

    @DisplayName("신고내용 없이 신고하면 예외발생")
    @Test
    void reportPost_Exception_No_Content() {
        ReportRequest reportRequest = new ReportRequest("  ");

        assertThatThrownBy(() -> postReportService.reportPost(post.getId(), reportRequest, new AuthInfo(member.getId())))
                .isInstanceOf(InvalidReportMessageException.class);
    }
}
