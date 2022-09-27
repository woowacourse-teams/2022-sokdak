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

    void deleteAllByPostId(Long postId);

    void deleteAllByCommentId(Long commentId);

    @Modifying
    @Query(value = "DELETE from Notification n WHERE n.id in :ids")
    void deleteAllById(List<Long> ids);

    @Query(value = "SELECT n.id from Notification n where n.comment.id = :commentId")
    List<Long> findIdsByCommentId(Long commentId);

    Slice<Notification> findNotificationsByMemberId(Long memberId, Pageable pageable);

    @Modifying
    @Query(value = "UPDATE Notification n SET n.inquired = true WHERE n.id in :ids")
    void inquireNotificationByIds(List<Long> ids);
}
