package com.wooteco.sokdak.notification.service;

import static com.wooteco.sokdak.notification.domain.NotificationType.COMMENT_REPORT;
import static com.wooteco.sokdak.notification.domain.NotificationType.HOT_BOARD;
import static com.wooteco.sokdak.notification.domain.NotificationType.NEW_COMMENT;
import static com.wooteco.sokdak.notification.domain.NotificationType.NEW_REPLY;
import static com.wooteco.sokdak.notification.domain.NotificationType.POST_REPORT;
import static com.wooteco.sokdak.notification.domain.NotificationType.REPLY_REPORT;
import static com.wooteco.sokdak.util.fixture.MemberFixture.VALID_NICKNAME;
import static com.wooteco.sokdak.util.fixture.PostFixture.VALID_POST_CONTENT;
import static com.wooteco.sokdak.util.fixture.PostFixture.VALID_POST_TITLE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.wooteco.sokdak.auth.dto.AuthInfo;
import com.wooteco.sokdak.member.domain.Member;
import com.wooteco.sokdak.member.exception.MemberNotFoundException;
import com.wooteco.sokdak.member.repository.MemberRepository;
import com.wooteco.sokdak.notification.domain.Notification;
import com.wooteco.sokdak.notification.dto.NewNotificationCheckResponse;
import com.wooteco.sokdak.notification.repository.NotificationRepository;
import com.wooteco.sokdak.post.domain.Post;
import com.wooteco.sokdak.post.repository.PostRepository;
import com.wooteco.sokdak.util.IntegrationTest;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

class NotificationServiceTest extends IntegrationTest {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    private Member member;
    private Post post;

    @BeforeEach
    void setUp() {
        member = memberRepository.findById(1L)
                .orElseThrow(MemberNotFoundException::new);
        post = Post.builder()
                .member(member)
                .title(VALID_POST_TITLE)
                .content(VALID_POST_CONTENT)
                .writerNickname(member.getNickname())
                .build();
        postRepository.save(post);
    }

    @DisplayName("새 댓글 알림을 등록한다.")
    @Test
    void notifyNewComment() {
        notificationService.notifyNewComment(member, post, "댓글 내용");

        List<Notification> notifications = notificationRepository.findByMemberId(member.getId());
        Notification notification = notifications.get(0);

        assertAll(
                () -> assertThat(notifications).hasSize(1),
                () -> assertThat(notification.getMember()).isEqualTo(member),
                () -> assertThat(notification.getPost()).isEqualTo(post),
                () -> assertThat(notification.getNotificationType()).isEqualTo(NEW_COMMENT),
                () -> assertThat(notification.getContent()).isEqualTo("댓글 내용"),
                () -> assertThat(notification.isInquired()).isFalse()
        );
    }

    @DisplayName("댓글 신고 알림을 등록한다.")
    @Test
    void notifyCommentReport() {
        notificationService.notifyCommentReport(member, post, "댓글 내용");

        List<Notification> notifications = notificationRepository.findByMemberId(member.getId());
        Notification notification = notifications.get(0);

        assertAll(
                () -> assertThat(notifications).hasSize(1),
                () -> assertThat(notification.getMember()).isEqualTo(member),
                () -> assertThat(notification.getPost()).isEqualTo(post),
                () -> assertThat(notification.getNotificationType()).isEqualTo(COMMENT_REPORT),
                () -> assertThat(notification.getContent()).isEqualTo("댓글 내용"),
                () -> assertThat(notification.isInquired()).isFalse()
        );
    }

    @DisplayName("새 대댓글 알림을 등록한다.")
    @Test
    void notifyNewReply() {
        notificationService.notifyNewReply(member, post, "대댓글 내용");

        List<Notification> notifications = notificationRepository.findByMemberId(member.getId());
        Notification notification = notifications.get(0);

        assertAll(
                () -> assertThat(notifications).hasSize(1),
                () -> assertThat(notification.getMember()).isEqualTo(member),
                () -> assertThat(notification.getPost()).isEqualTo(post),
                () -> assertThat(notification.getNotificationType()).isEqualTo(NEW_REPLY),
                () -> assertThat(notification.getContent()).isEqualTo("대댓글 내용"),
                () -> assertThat(notification.isInquired()).isFalse()
        );
    }

    @DisplayName("댓글 신고 알림을 등록한다.")
    @Test
    void notifyReplyReport() {
        notificationService.notifyReplyReport(member, post, "대댓글 내용");

        List<Notification> notifications = notificationRepository.findByMemberId(member.getId());
        Notification notification = notifications.get(0);

        assertAll(
                () -> assertThat(notifications).hasSize(1),
                () -> assertThat(notification.getMember()).isEqualTo(member),
                () -> assertThat(notification.getPost()).isEqualTo(post),
                () -> assertThat(notification.getNotificationType()).isEqualTo(REPLY_REPORT),
                () -> assertThat(notification.getContent()).isEqualTo("대댓글 내용"),
                () -> assertThat(notification.isInquired()).isFalse()
        );
    }

    @DisplayName("게시글이 핫 게시판으로 이동했다는 알림을 등록한다.")
    @Test
    void notifyHotBoard() {
        notificationService.notifyHotBoard(post);

        List<Notification> notifications = notificationRepository.findByMemberId(member.getId());
        Notification notification = notifications.get(0);

        assertAll(
                () -> assertThat(notifications).hasSize(1),
                () -> assertThat(notification.getMember()).isEqualTo(member),
                () -> assertThat(notification.getPost()).isEqualTo(post),
                () -> assertThat(notification.getNotificationType()).isEqualTo(HOT_BOARD),
                () -> assertThat(notification.getContent()).isNull(),
                () -> assertThat(notification.isInquired()).isFalse()
        );
    }

    @DisplayName("게시글 신고 알림을 등록한다.")
    @Test
    void notifyPostReport() {
        notificationService.notifyPostReport(post);

        List<Notification> notifications = notificationRepository.findByMemberId(member.getId());
        Notification notification = notifications.get(0);

        assertAll(
                () -> assertThat(notifications).hasSize(1),
                () -> assertThat(notification.getMember()).isEqualTo(member),
                () -> assertThat(notification.getPost()).isEqualTo(post),
                () -> assertThat(notification.getNotificationType()).isEqualTo(POST_REPORT),
                () -> assertThat(notification.getContent()).isNull(),
                () -> assertThat(notification.isInquired()).isFalse()
        );
    }

    @DisplayName("새로 등록된 알림이 있는지 반환한다.")
    @ParameterizedTest
    @CsvSource({"1, true", "2, false"})
    void existsNewNotification(Long memberId, boolean expected) {
        notificationService.notifyPostReport(post);
        AuthInfo authInfo = new AuthInfo(memberId, "USER", VALID_NICKNAME);

        NewNotificationCheckResponse newNotificationCheckResponse = notificationService.checkNewNotification(authInfo);

        assertThat(newNotificationCheckResponse.isExistence()).isEqualTo(expected);
    }
}
