package com.wooteco.sokdak.notification.repository;

import com.wooteco.sokdak.notification.domain.Notification;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    boolean existsByMemberIdAndInquiredIsFalse(Long memberId);

    @Query(value = "SELECT n FROM Notification n WHERE n.memberId = :memberId")
    Slice<Notification> findNotificationsByMemberId(Long memberId, Pageable pageable);

    @Modifying
    @Query(value = "UPDATE Notification n SET n.inquired = true WHERE n.id IN :ids")
    void inquireNotificationByIds(List<Long> ids);

    @Modifying
    @Query(value = "DELETE FROM Notification n WHERE n.postId = :postId")
    void deleteAllByPostId(Long postId);

    @Modifying
    @Query(value = "DELETE FROM Notification n WHERE n.commentId = :commentId")
    void deleteAllByCommentId(Long commentId);
}
