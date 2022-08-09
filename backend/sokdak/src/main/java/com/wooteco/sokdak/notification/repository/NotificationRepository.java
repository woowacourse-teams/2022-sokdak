package com.wooteco.sokdak.notification.repository;

import com.wooteco.sokdak.notification.domain.Notification;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByMemberId(Long memberId);

    boolean existsByMemberIdAndInquiredIsFalse(Long memberId);
}
