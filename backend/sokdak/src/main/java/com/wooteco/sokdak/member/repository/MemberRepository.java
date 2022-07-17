package com.wooteco.sokdak.member.repository;

import com.wooteco.sokdak.member.domain.Member;
import com.wooteco.sokdak.member.domain.Nickname;
import com.wooteco.sokdak.member.domain.Username;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsMemberByUsername(Username username);

    boolean existsMemberByNickname(Nickname nickname);

    Optional<Member> findByUsernameAndPassword(Username username, String password);
}
