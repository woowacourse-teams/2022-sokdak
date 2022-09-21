package com.wooteco.sokdak.notification.repository;

import com.wooteco.sokdak.notification.domain.Notification;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    boolean existsByMemberIdAndInquiredIsFalse(Long memberId);

    void deleteAllByPostId(Long postId);

    void deleteAllByCommentId(Long commentId);

    Slice<Notification> findNotificationsByMemberId(Long memberId, Pageable pageable);
}
