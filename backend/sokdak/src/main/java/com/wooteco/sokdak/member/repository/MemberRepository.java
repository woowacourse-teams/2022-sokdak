package com.wooteco.sokdak.member.repository;

import com.wooteco.sokdak.member.domain.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsMemberByUsernameValue(String username);

    boolean existsMemberByNicknameValue(String nickname);

    Optional<Member> findByUsernameValueAndPassword(String username, String password);
}
