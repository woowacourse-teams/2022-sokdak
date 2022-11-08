package com.wooteco.sokdak.notification.repository;

import com.wooteco.sokdak.notification.domain.NewNotificationExistence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface NewNotificationExistenceRepository extends JpaRepository<NewNotificationExistence, Long> {

    boolean existsByMemberIdAndExistenceIsTrue(Long memberId);

    @Modifying
    @Query(value = "UPDATE NewNotificationExistence nne SET nne.existence = :existence WHERE nne.memberId = :memberId")
    void update(Long memberId, boolean existence);
}
