package com.wooteco.sokdak.notification.repository;

import com.wooteco.sokdak.notification.domain.Notification;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query(value = "SELECT exists (SELECT * from Notification where member_id = :memberId and inquired = false)",
            nativeQuery = true)
    boolean existsByMemberIdAndInquiredIsFalse(Long memberId);

    @Modifying
    @Query(value = "DELETE from Notification n WHERE n.id in :ids")
    void deleteAllById(List<Long> ids);

    @Query(value = "SELECT n.id from Notification n where n.comment.id = :commentId")
    List<Long> findIdsByCommentId(Long commentId);

    @Query(value = "SELECT n.id from Notification n where n.post.id = :postId")
    List<Long> findIdsByPostId(Long postId);

    @Query(value = "SELECT n from Notification n where n.member.id = :memberId")
    Slice<Notification> findNotificationsByMemberId(Long memberId, Pageable pageable);

    @Modifying
    @Query(value = "UPDATE Notification n SET n.inquired = true WHERE n.id in :ids")
    void inquireNotificationByIds(List<Long> ids);
}
