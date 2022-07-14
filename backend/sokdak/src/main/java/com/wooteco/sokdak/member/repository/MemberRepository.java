package com.wooteco.sokdak.member.repository;

import com.wooteco.sokdak.member.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByUsername(String username);
}
