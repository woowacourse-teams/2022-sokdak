package com.wooteco.sokdak.notification.repository;

import static com.wooteco.sokdak.notification.domain.NotificationType.NEW_COMMENT;
import static com.wooteco.sokdak.notification.domain.NotificationType.POST_REPORT;
import static com.wooteco.sokdak.notification.fixture.NotificationFixture.ZERO_PAGE_TWO_SIZE_CREATE_AT_DESCENDING_PAGEABLE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.wooteco.sokdak.notification.domain.Notification;
import com.wooteco.sokdak.util.RepositoryTest;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Slice;

class NotificationRepositoryTest extends RepositoryTest {

    private static final Long MEMBER_ID = 1L;
    private static final Long POST_ID = 1L;
    private static final Long NULL_COMMENT_ID = null;

    @Autowired
    private NotificationRepository notificationRepository;

    @PersistenceContext
    private EntityManager em;

    @DisplayName("알림들의 id를 받아 조회 여부를 true로 변경한다.")
    @Test
    void inquireNotificationByIds() {
        Notification notification = new Notification(NEW_COMMENT, MEMBER_ID, POST_ID, NULL_COMMENT_ID);
        Notification notification2 = new Notification(NEW_COMMENT, MEMBER_ID, POST_ID, NULL_COMMENT_ID);
        notificationRepository.saveAll(List.of(notification, notification2));
        em.clear();

        notificationRepository.inquireNotificationByIds(List.of(notification.getId(), notification2.getId()));

        List<Notification> notifications = notificationRepository
                .findNotificationsByMemberId(MEMBER_ID, ZERO_PAGE_TWO_SIZE_CREATE_AT_DESCENDING_PAGEABLE)
                .getContent();
        boolean actual = notifications.stream()
                .allMatch(Notification::isInquired);
        assertThat(actual).isTrue();
    }

    @DisplayName("회원이 조회하지 않은 알림이 존재하는지 반환")
    @Test
    void existsByMemberIdAndInquired() {
        Notification notification = new Notification(NEW_COMMENT, MEMBER_ID, POST_ID, NULL_COMMENT_ID);
        notificationRepository.save(notification);

        boolean actual = notificationRepository.existsByMemberIdAndInquiredIsFalse(MEMBER_ID);

        assertThat(actual).isTrue();
    }

    @DisplayName("회원에 따른 알림을 반환한다.")
    @Test
    void findNotificationsByMemberId() {
        Long anotherPostId = 2L;
        Notification notification2 = new Notification(NEW_COMMENT, MEMBER_ID, POST_ID, NULL_COMMENT_ID);
        notificationRepository.save(notification2);
        Notification notification3 = new Notification(POST_REPORT, MEMBER_ID, anotherPostId, NULL_COMMENT_ID);
        notificationRepository.save(notification3);

        Slice<Notification> notifications = notificationRepository
                .findNotificationsByMemberId(MEMBER_ID, ZERO_PAGE_TWO_SIZE_CREATE_AT_DESCENDING_PAGEABLE);

        assertAll(
                () -> assertThat(notifications.isLast()).isTrue(),
                () -> assertThat(notifications.getContent().get(0).getNotificationType()).isEqualTo(POST_REPORT),
                () -> assertThat(notifications.getContent().get(0).getPostId()).isEqualTo(anotherPostId),
                () -> assertThat(notifications.getContent().get(1).getNotificationType()).isEqualTo(NEW_COMMENT),
                () -> assertThat(notifications.getContent().get(1).getPostId()).isEqualTo(POST_ID)
        );
    }
}
