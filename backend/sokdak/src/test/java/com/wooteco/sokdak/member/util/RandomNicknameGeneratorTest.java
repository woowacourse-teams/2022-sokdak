package com.wooteco.sokdak.member.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RandomNicknameGeneratorTest {

    private static final String[] ADJECTIVES = {"행복한", "즐거운", "슬픈", "화난", "기쁜", "울적한", "신기한", "재밌는", "속상한", "기쁜", "배고픈",
            "걱정많은"};
    private static final String[] NOUNS = {"404", "200", "자스", "타스", "리액트", "스프링", "자바", "파이썬", "노드", "AWS", "맥북",
            "윈도우", "마우스", "키보드", "노드", "도커"};

    public static final Set<String> RANDOM_NICKNAMES = new HashSet<>();

    @BeforeAll
    static void setUp() {
        for (String adjective : ADJECTIVES) {
            for (String noun : NOUNS) {
                RANDOM_NICKNAMES.add(adjective + " " + noun);
            }
        }
    }

    @DisplayName("인자로 받은 닉네임과 중복되지 않는 닉네임을 반환한다.")
    @Test
    void createUniqueRandomNickname() {
        String nickname = RandomNicknameGenerator.generate(new HashSet<>());
        assertThat(RANDOM_NICKNAMES).contains(nickname);
    }

    @DisplayName("중복되지 않는 닉네임이 없다면 디폴트 닉네임을 반환한다.")
    @Test
    void createDefaultNickname() {
        String nickname = RandomNicknameGenerator.generate(RANDOM_NICKNAMES);
        assertThat(nickname).isEqualTo("크루");
    }
}
