package com.wooteco.sokdak.member.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.wooteco.sokdak.auth.service.Encryptor;
import com.wooteco.sokdak.member.dto.UniqueResponse;
import com.wooteco.sokdak.util.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

class MemberServiceTest extends IntegrationTest {

    @Autowired
    private MemberService memberService;

    @DisplayName("이미 존재하는 username이면 uniqueResponse의 unique를 false, 이외는 true를 반환한다.")
    @ParameterizedTest
    @CsvSource({"josh, false", "hahahoho, true"})
    void checkUniqueUsername(String username, boolean expected) {
        UniqueResponse uniqueResponse = memberService.checkUniqueUsername(username);

        assertThat(uniqueResponse.isUnique()).isEqualTo(expected);
    }
}
