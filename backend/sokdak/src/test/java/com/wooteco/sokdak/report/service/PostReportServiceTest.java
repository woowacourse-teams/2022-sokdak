package com.wooteco.sokdak.report.service;

import static com.wooteco.sokdak.member.domain.RoleType.USER;
import static com.wooteco.sokdak.util.fixture.MemberFixture.VALID_NICKNAME;
import static com.wooteco.sokdak.util.fixture.PostFixture.VALID_POST_CONTENT;
import static com.wooteco.sokdak.util.fixture.PostFixture.VALID_POST_TITLE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.wooteco.sokdak.auth.dto.AuthInfo;
import com.wooteco.sokdak.member.exception.MemberNotFoundException;
import com.wooteco.sokdak.member.repository.MemberRepository;
import com.wooteco.sokdak.notification.repository.NotificationRepository;
import com.wooteco.sokdak.post.domain.Post;
import com.wooteco.sokdak.post.repository.PostRepository;
import com.wooteco.sokdak.report.dto.ReportRequest;
import com.wooteco.sokdak.report.exception.AlreadyReportPostException;
import com.wooteco.sokdak.report.exception.InvalidReportMessageException;
import com.wooteco.sokdak.report.repository.PostReportRepository;
import com.wooteco.sokdak.util.IntegrationTest;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

class PostReportServiceTest extends IntegrationTest {

    private static final ReportRequest REPORT_REQUEST = new ReportRequest("나쁜글");

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostReportRepository postReportRepository;

    @Autowired
    private PostReportService postReportService;

    @Autowired
    private NotificationRepository notificationRepository;

    private Post post;

    @BeforeEach
    void setUp() {
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
        int reportCountBeforeReport = postReportRepository.countByPostId(post.getId());

        postReportService
                .reportPost(post.getId(), REPORT_REQUEST, new AuthInfo(member.getId(), USER.getName(), "nickname"));
        int reportCountAfterReport = postReportRepository.countByPostId(post.getId());

        assertThat(reportCountBeforeReport + 1).isEqualTo(reportCountAfterReport);
    }

    @DisplayName("이미 신고한 게시글을 신고 시 예외발생")
    @Test
    void reportPost_Exception_Already_Report() {
        postReportService
                .reportPost(post.getId(), REPORT_REQUEST, new AuthInfo(member.getId(), USER.getName(), "nickname"));

        assertThatThrownBy(
                () -> postReportService.reportPost(post.getId(), REPORT_REQUEST,
                        new AuthInfo(member.getId(), USER.getName(), "nickname")))
                .isInstanceOf(AlreadyReportPostException.class);
    }

    @DisplayName("신고내용 없이 신고하면 예외발생")
    @Test
    void reportPost_Exception_No_Content() {
        ReportRequest invalidReportRequest = new ReportRequest("  ");

        assertThatThrownBy(
                () -> postReportService.reportPost(post.getId(), invalidReportRequest,
                        new AuthInfo(member.getId(), USER.getName(), "nickname")))
                .isInstanceOf(InvalidReportMessageException.class);
    }

    @DisplayName("게시글 5회 신고시 알림 등록")
    @ParameterizedTest
    @CsvSource({"4, false", "5, true"})
    void reportPost_blockNotification(int reportCount, boolean expected) {
        AuthInfo authInfo;
        for (long i = 1; i <= reportCount; i++) {
            authInfo = new AuthInfo(i, "USER", VALID_NICKNAME);
            postReportService.reportPost(post.getId(), REPORT_REQUEST, authInfo);
        }

        boolean actual = notificationRepository.existsByMemberIdAndInquiredIsFalse(member.getId());

        assertThat(actual).isEqualTo(expected);
    }
}
