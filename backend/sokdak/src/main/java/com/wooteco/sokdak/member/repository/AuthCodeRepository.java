package com.wooteco.sokdak.member.repository;

import com.wooteco.sokdak.member.domain.auth.AuthCode;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthCodeRepository extends JpaRepository<AuthCode, Long> {
    Optional<AuthCode> findBySerialNumber(String serialNumber);

    void deleteAllBySerialNumber(String serialNumber);
}
