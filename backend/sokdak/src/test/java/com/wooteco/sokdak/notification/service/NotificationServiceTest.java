package com.wooteco.sokdak.notification.service;

import static com.wooteco.sokdak.util.fixture.MemberFixture.VALID_NICKNAME_TEXT;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

class NotificationServiceTest extends ServiceTest {

    private static final Pageable PAGEABLE = PageRequest.of(0, 100);

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
                .nickname("닉네임")
                .message("내용")
                .build();
        comment2 = Comment.builder()
                .post(post)
                .member(member)
                .nickname("닉네임")
                .message("내용")
                .build();
        commentRepository.save(comment);
        commentRepository.save(comment2);
    }

    @DisplayName("새로 등록된 알림이 있는지 반환한다.")
    @ParameterizedTest
    @CsvSource({"3, true", "4, false"})
    void existsNewNotification(Long memberId, boolean expected) {
        Notification notification = Notification.newComment(member.getId(), comment.getId());
        notificationRepository.save(notification);
        AuthInfo authInfo = new AuthInfo(memberId, "USER", VALID_NICKNAME_TEXT);

        NewNotificationCheckResponse newNotificationCheckResponse = notificationService.checkNewNotification(authInfo);

        assertThat(newNotificationCheckResponse.isExistence()).isEqualTo(expected);
    }

    @DisplayName("알림 목록을 반환하고 조회한 알림으로 변경하고 새 알림을 존재하지 않는 상태로 변경한다.")
    @Test
    void findNotifications() {
        Notification notification2 = Notification.postReport(member.getId(), post.getId());
        Notification notification3 = Notification.newComment(member.getId(), post.getId());
        notificationRepository.saveAll(List.of(notification2, notification3));

        NotificationsResponse notificationsResponse = notificationService
                .findNotifications(AUTH_INFO, PageRequest.of(0, 2, Sort.by("createdAt").descending()));
        List<NotificationResponse> notificationResponses = notificationsResponse.getNotifications();
        NewNotificationCheckResponse newNotificationCheckResponse = notificationService.checkNewNotification(AUTH_INFO);

        assertAll(
                () -> assertThat(notificationsResponse.isLastPage()).isTrue(),
                () -> assertThat(notificationResponses).hasSize(2),
                () -> assertThat(notificationResponses.get(0).getPostId()).isEqualTo(post.getId()),
                () -> assertThat(notificationResponses.get(0).getType()).isEqualTo("NEW_COMMENT"),
                () -> assertThat(notificationResponses.get(0).getContent()).isEqualTo(post.getTitle()),
                () -> assertThat(notificationResponses.get(1).getPostId()).isEqualTo(post.getId()),
                () -> assertThat(notificationResponses.get(1).getType()).isEqualTo("POST_REPORT"),
                () -> assertThat(notificationResponses.get(1).getContent()).isEqualTo(post.getTitle()),
                () -> assertThat(newNotificationCheckResponse.isExistence()).isFalse()
        );
    }

    @DisplayName("댓글에 해당하는 알림들을 삭제한다.")
    @Test
    void deleteCommentNotification() {
        Notification notification1 = Notification.commentReport(member.getId(), post.getId(), comment.getId());
        Notification notification2 = Notification.commentReport(member.getId(), post.getId(), comment.getId());

        notificationRepository.saveAll(List.of(notification1, notification2));

        em.clear();

        notificationService.deleteCommentNotification(comment.getId());

        NotificationsResponse notifications = notificationService.findNotifications(AUTH_INFO, PAGEABLE);
        assertThat(notifications.getNotifications()).isEmpty();
    }

    @DisplayName("게시글에 해당하는 알림들을 삭제한다.")
    @Test
    void deletePostNotification() {
        Comment comment3 = Comment.builder()
                .post(post)
                .member(member2)
                .nickname("닉네임")
                .message("내용")
                .build();
        commentRepository.save(comment3);
        Notification notification1 = Notification.newComment(member.getId(), post.getId());
        Notification notification2 = Notification.newComment(member.getId(), post.getId());
        notificationRepository.save(notification1);
        notificationRepository.save(notification2);

        em.clear();

        notificationService.deletePostNotification(post.getId());

        NotificationsResponse notifications = notificationService.findNotifications(AUTH_INFO, PAGEABLE);
        assertThat(notifications.getNotifications()).isEmpty();
    }

    @DisplayName("알림을 삭제한다.")
    @Test
    void deleteNotification() {
        Notification notification = Notification.postHotBoard(member.getId(), post.getId());
        notificationRepository.save(notification);

        notificationService.deleteNotification(AUTH_INFO, notification.getId());

        List<Notification> notifications = notificationRepository
                .findNotificationsByMemberId(member.getId(), PAGEABLE)
                .getContent();
        assertThat(notifications).isEmpty();
    }
}
