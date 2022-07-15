package com.wooteco.sokdak.member.repository;

import com.wooteco.sokdak.member.domain.member.Member;
import com.wooteco.sokdak.member.domain.member.Nickname;
import com.wooteco.sokdak.member.domain.member.Username;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsMemberByUsername(Username username);

    boolean existsMemberByNickname(Nickname nickname);

    Optional<Member> findByUsernameAndPassword(Username username, String password);
}