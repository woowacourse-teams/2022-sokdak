package com.wooteco.sokdak.member.repository;

import com.wooteco.sokdak.member.domain.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsMemberByUsernameValue(String username);

    boolean existsMemberByNicknameValue(String nickname);

    Optional<Member> findByUsernameValueAndPassword(String username, String password);

    Optional<Member> findById(Long id);

    @Query(value = "SELECT m.nickname.value FROM Member m WHERE m.id = :id")
    Optional<String> findNicknameValueById(@Param("id") Long id);
}
