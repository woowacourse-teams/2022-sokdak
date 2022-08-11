package com.wooteco.sokdak.notification.service;

import static com.wooteco.sokdak.notification.domain.NotificationType.COMMENT_REPORT;
import static com.wooteco.sokdak.notification.domain.NotificationType.HOT_BOARD;
import static com.wooteco.sokdak.notification.domain.NotificationType.NEW_COMMENT;
import static com.wooteco.sokdak.notification.domain.NotificationType.POST_REPORT;
import static com.wooteco.sokdak.util.fixture.MemberFixture.AUTH_INFO;
import static com.wooteco.sokdak.util.fixture.MemberFixture.VALID_NICKNAME;
import static com.wooteco.sokdak.util.fixture.PostFixture.VALID_POST_CONTENT;
import static com.wooteco.sokdak.util.fixture.PostFixture.VALID_POST_TITLE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.wooteco.sokdak.auth.dto.AuthInfo;
import com.wooteco.sokdak.comment.domain.Comment;
import com.wooteco.sokdak.comment.repository.CommentRepository;
import com.wooteco.sokdak.member.domain.Member;
import com.wooteco.sokdak.member.exception.MemberNotFoundException;
import com.wooteco.sokdak.member.repository.MemberRepository;
import com.wooteco.sokdak.notification.domain.Notification;
import com.wooteco.sokdak.notification.dto.NewNotificationCheckResponse;
import com.wooteco.sokdak.notification.dto.NotificationResponse;
import com.wooteco.sokdak.notification.dto.NotificationsResponse;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

class NotificationServiceTest extends IntegrationTest {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    private Member member;
    private Post post;
    private Comment comment;

    @BeforeEach
    void setUp() {
        member = memberRepository.findById(1L)
                .orElseThrow(MemberNotFoundException::new);
        Member member2 = memberRepository.findById(2L)
                .orElseThrow(MemberNotFoundException::new);
        post = Post.builder()
                .member(member)
                .title(VALID_POST_TITLE)
                .content(VALID_POST_CONTENT)
                .writerNickname(member.getNickname())
                .build();
        postRepository.save(post);
        comment = Comment.builder()
                .post(post)
                .member(member2)
                .nickname("닉네임")
                .message("내용")
                .build();
        commentRepository.save(comment);
    }

    @DisplayName("새 댓글 알림을 등록한다.")
    @Test
    void notifyNewComment() {
        notificationService.notifyNewComment(member, post, comment);

        List<Notification> notifications = notificationRepository.findByMemberId(member.getId());
        Notification notification = notifications.get(0);

        assertAll(
                () -> assertThat(notifications).hasSize(1),
                () -> assertThat(notification.getMember()).isEqualTo(member),
                () -> assertThat(notification.getPost()).isEqualTo(post),
                () -> assertThat(notification.getNotificationType()).isEqualTo(NEW_COMMENT),
                () -> assertThat(notification.getContent()).isEqualTo(post.getTitle()),
                () -> assertThat(notification.isInquired()).isFalse()
        );
    }

    @DisplayName("댓글 신고 알림을 등록한다.")
    @Test
    void notifyCommentReport() {
        notificationService.notifyCommentReport(member, post, comment);

        List<Notification> notifications = notificationRepository.findByMemberId(member.getId());
        Notification notification = notifications.get(0);

        assertAll(
                () -> assertThat(notifications).hasSize(1),
                () -> assertThat(notification.getMember()).isEqualTo(member),
                () -> assertThat(notification.getPost()).isEqualTo(post),
                () -> assertThat(notification.getNotificationType()).isEqualTo(COMMENT_REPORT),
                () -> assertThat(notification.getContent()).isEqualTo("내용"),
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
                () -> assertThat(notification.getContent()).isEqualTo(VALID_POST_TITLE),
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
                () -> assertThat(notification.getContent()).isEqualTo(VALID_POST_TITLE),
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

    @DisplayName("알림을 반환한다.")
    @Test
    void findNotifications() {
        Notification notification = Notification.builder()
                .member(member)
                .notificationType(HOT_BOARD)
                .post(post)
                .build();
        Notification notification2 = Notification.builder()
                .member(member)
                .notificationType(POST_REPORT)
                .post(post)
                .build();
        Notification notification3 = Notification.builder()
                .member(member)
                .notificationType(NEW_COMMENT)
                .post(post)
                .comment(comment)
                .build();
        notificationRepository.save(notification);
        notificationRepository.save(notification2);
        notificationRepository.save(notification3);

        NotificationsResponse notificationsResponse = notificationService
                .findNotifications(AUTH_INFO, PageRequest.of(0, 2, Sort.by("createdAt").descending()));
        List<NotificationResponse> notificationResponses = notificationsResponse.getNotifications();

        assertAll(
                () -> assertThat(notificationsResponse.isLastPage()).isFalse(),
                () -> assertThat(notificationResponses).hasSize(2),
                () -> assertThat(notificationResponses.get(0).getPostId()).isEqualTo(post.getId()),
                () -> assertThat(notificationResponses.get(0).getType()).isEqualTo("NEW_COMMENT"),
                () -> assertThat(notificationResponses.get(0).getContent()).isEqualTo(post.getTitle()),
                () -> assertThat(notificationResponses.get(1).getPostId()).isEqualTo(post.getId()),
                () -> assertThat(notificationResponses.get(1).getType()).isEqualTo("POST_REPORT"),
                () -> assertThat(notificationResponses.get(1).getContent()).isEqualTo(post.getTitle())
        );
    }
}
