package com.wooteco.sokdak.notification.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.wooteco.sokdak.notification.domain.NewNotificationExistence;
import com.wooteco.sokdak.util.RepositoryTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class NewNotificationExistenceRepositoryTest extends RepositoryTest {

    @Autowired
    private NewNotificationExistenceRepository newNotificationExistenceRepository;

    @DisplayName("멤버에 대한 새 알림이 존재하는지 반환한다.")
    @Test
    void existsByMemberIdAndExistenceIsTrue() {
        newNotificationExistenceRepository.save(new NewNotificationExistence(1L, true));

        assertThat(newNotificationExistenceRepository.existsByMemberIdAndExistenceIsTrue(1L)).isTrue();
    }

    @DisplayName("멤버에 대한 새 알림 상태를 변경한다.")
    @Test
    void update() {
        newNotificationExistenceRepository.save(new NewNotificationExistence(1L, false));

        newNotificationExistenceRepository.update(1L, true);

        assertThat(newNotificationExistenceRepository.existsByMemberIdAndExistenceIsTrue(1L)).isTrue();
    }
}
