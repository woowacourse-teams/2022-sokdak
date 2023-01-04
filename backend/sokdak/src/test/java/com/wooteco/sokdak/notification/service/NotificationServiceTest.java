package com.wooteco.sokdak.notification.service;

import static com.wooteco.sokdak.notification.domain.NotificationType.HOT_BOARD;
import static com.wooteco.sokdak.notification.domain.NotificationType.NEW_COMMENT;
import static com.wooteco.sokdak.notification.domain.NotificationType.POST_REPORT;
import static com.wooteco.sokdak.notification.fixture.NotificationFixture.ZERO_PAGE_TWO_SIZE_CREATE_AT_DESCENDING_PAGEABLE;
import static com.wooteco.sokdak.util.fixture.MemberFixture.VALID_NICKNAME_TEXT;
import static com.wooteco.sokdak.util.fixture.PostFixture.VALID_POST_CONTENT;
import static com.wooteco.sokdak.util.fixture.PostFixture.VALID_POST_TITLE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.wooteco.sokdak.auth.dto.AuthInfo;
import com.wooteco.sokdak.comment.domain.Comment;
import com.wooteco.sokdak.comment.repository.CommentRepository;
import com.wooteco.sokdak.member.domain.Member;
import com.wooteco.sokdak.member.domain.RoleType;
import com.wooteco.sokdak.member.exception.MemberNotFoundException;
import com.wooteco.sokdak.member.repository.MemberRepository;
import com.wooteco.sokdak.notification.domain.Notification;
import com.wooteco.sokdak.notification.dto.NewNotificationCheckResponse;
import com.wooteco.sokdak.notification.dto.NotificationResponse;
import com.wooteco.sokdak.notification.dto.NotificationsResponse;
import com.wooteco.sokdak.notification.repository.NotificationRepository;
import com.wooteco.sokdak.post.domain.Post;
import com.wooteco.sokdak.post.repository.PostRepository;
import com.wooteco.sokdak.util.ServiceTest;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

class NotificationServiceTest extends ServiceTest {

    private static final String NICKNAME = "닉네임";
    private static final String MESSAGE = "내용";

    @PersistenceContext
    private EntityManager em;

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

    private Member member2;
    private Post post;
    private Comment comment;
    private Comment comment2;

    @BeforeEach
    void setUp() {
        member2 = memberRepository.findById(2L)
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
                .nickname(NICKNAME)
                .message(MESSAGE)
                .build();
        comment2 = Comment.builder()
                .post(post)
                .member(member)
                .nickname(NICKNAME)
                .message(MESSAGE)
                .build();
        commentRepository.save(comment);
        commentRepository.save(comment2);
    }

    @DisplayName("새로 등록된 알림이 있는지 반환한다.")
    @ParameterizedTest
    @CsvSource({"3, true", "4, false"})
    void existsNewNotification(Long memberId, boolean expected) {
        Notification notification = new Notification(NEW_COMMENT, member.getId(), post.getId(), null);
        notificationRepository.save(notification);
        AuthInfo authInfo = new AuthInfo(memberId, RoleType.USER.getName(), VALID_NICKNAME_TEXT);

        NewNotificationCheckResponse newNotificationCheckResponse = notificationService.checkNewNotification(authInfo);

        assertThat(newNotificationCheckResponse.isExistence()).isEqualTo(expected);
    }

    @DisplayName("알림 목록을 반환하고 조회한 알림으로 변경하고 새 알림을 존재하지 않는 상태로 변경한다.")
    @Test
    void findNotifications() {
        Notification notification2 = new Notification(POST_REPORT, member.getId(), post.getId(), null);
        notificationRepository.save(notification2);
        Notification notification3 = new Notification(NEW_COMMENT, member.getId(), post.getId(), null);
        notificationRepository.save(notification3);

        NotificationsResponse notificationsResponse =
                notificationService.findNotifications(AUTH_INFO, ZERO_PAGE_TWO_SIZE_CREATE_AT_DESCENDING_PAGEABLE);
        List<NotificationResponse> notificationResponses = notificationsResponse.getNotifications();
        NewNotificationCheckResponse newNotificationCheckResponse = notificationService.checkNewNotification(AUTH_INFO);

        assertAll(
                () -> assertThat(notificationsResponse.isLastPage()).isTrue(),
                () -> assertThat(notificationResponses).hasSize(2),
                () -> assertThat(notificationResponses.get(0).getPostId()).isEqualTo(post.getId()),
                () -> assertThat(notificationResponses.get(0).getType()).isEqualTo(NEW_COMMENT.name()),
                () -> assertThat(notificationResponses.get(0).getContent()).isEqualTo(post.getTitle()),
                () -> assertThat(notificationResponses.get(1).getPostId()).isEqualTo(post.getId()),
                () -> assertThat(notificationResponses.get(1).getType()).isEqualTo(POST_REPORT.name()),
                () -> assertThat(notificationResponses.get(1).getContent()).isEqualTo(post.getTitle()),
                () -> assertThat(newNotificationCheckResponse.isExistence()).isFalse()
        );
    }

    @DisplayName("알림을 삭제한다.")
    @Test
    void deleteNotification() {
        Notification notification = new Notification(HOT_BOARD, member.getId(), post.getId(), null);
        notificationRepository.save(notification);

        notificationService.deleteNotification(AUTH_INFO, notification.getId());

        List<Notification> notifications = notificationRepository
                .findNotificationsByMemberId(member.getId(), ZERO_PAGE_TWO_SIZE_CREATE_AT_DESCENDING_PAGEABLE)
                .getContent();
        assertThat(notifications).isEmpty();
    }
}
