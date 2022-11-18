package com.wooteco.sokdak.notification.repository;

import static com.wooteco.sokdak.notification.domain.NotificationType.NEW_COMMENT;
import static com.wooteco.sokdak.notification.domain.NotificationType.POST_REPORT;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;

class NotificationRepositoryTest extends RepositoryTest {

    private static final Long MEMBER_ID = 1L;
    private static final Long POST_ID = 1L;

    @Autowired
    private NotificationRepository notificationRepository;

    @PersistenceContext
    private EntityManager em;

    @DisplayName("알림들의 id를 받아 조회 여부를 true로 변경한다.")
    @Test
    void inquireNotificationByIds() {
        Notification notification = Notification.newComment(MEMBER_ID, POST_ID);
        Notification notification2 = Notification.newComment(MEMBER_ID, POST_ID);
        notificationRepository.saveAll(List.of(notification, notification2));
        em.clear();

        notificationRepository.inquireNotificationByIds(List.of(notification.getId(), notification2.getId()));

        List<Notification> notifications = notificationRepository
                .findNotificationsByMemberId(MEMBER_ID, Pageable.ofSize(3))
                .getContent();
        boolean actual = notifications.stream()
                .allMatch(Notification::isInquired);
        assertThat(actual).isTrue();
    }

    @DisplayName("회원이 조회하지 않은 알림이 존재하는지 반환")
    @Test
    void existsByMemberIdAndInquired() {
        Notification notification = Notification.newComment(MEMBER_ID, POST_ID);
        notificationRepository.save(notification);

        boolean actual = notificationRepository.existsByMemberIdAndInquiredIsFalse(MEMBER_ID);

        assertThat(actual).isTrue();
    }

    @DisplayName("회원에 따른 알림을 반환한다.")
    @Test
    void findNotificationsByMemberId() {
        Notification notification = Notification.newComment(MEMBER_ID, POST_ID);
        notificationRepository.save(notification);
        Notification notification2 = Notification.newComment(MEMBER_ID, POST_ID);
        notificationRepository.save(notification2);
        Notification notification3 = Notification.postReport(MEMBER_ID, 2L);
        notificationRepository.save(notification3);
        PageRequest pageRequest = PageRequest.of(0, 2, Sort.by("createdAt").descending());

        Slice<Notification> notifications = notificationRepository
                .findNotificationsByMemberId(MEMBER_ID, pageRequest);

        assertAll(
                () -> assertThat(notifications.isLast()).isFalse(),
                () -> assertThat(notifications.getContent().get(0).getNotificationType()).isEqualTo(POST_REPORT),
                () -> assertThat(notifications.getContent().get(0).getPostId()).isEqualTo(2L),
                () -> assertThat(notifications.getContent().get(1).getNotificationType()).isEqualTo(NEW_COMMENT),
                () -> assertThat(notifications.getContent().get(1).getPostId()).isEqualTo(1L)
        );
    }
}
