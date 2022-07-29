package com.wooteco.sokdak.auth.repository;

import com.wooteco.sokdak.auth.domain.RefreshToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByMemberId(Long memberId);

    void deleteAllByMemberId(Long memberId);
}
