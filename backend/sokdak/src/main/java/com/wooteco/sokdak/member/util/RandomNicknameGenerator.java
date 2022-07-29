package com.wooteco.sokdak.member.util;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class RandomNicknameGenerator {

    private static final Random RANDOM = new Random();
    private static final String[] ADJECTIVES = {"행복한", "즐거운", "슬픈", "화난", "기쁜", "울적한", "신기한", "재밌는", "속상한", "기쁜", "배고픈",
            "걱정많은"};
    private static final String[] NOUNS = {"404", "200", "자스", "타스", "리액트", "스프링", "자바", "파이썬", "노드", "AWS", "맥북",
            "윈도우", "마우스", "키보드", "노드", "도커"};
    public static final Set<String> RANDOM_NICKNAMES = new HashSet<>();

    static {
        for (String adjective : ADJECTIVES) {
            for (String noun : NOUNS) {
                RANDOM_NICKNAMES.add(adjective + " " + noun);
            }
        }
    }

    public static String generate(Set<String> usedNicknames) {
        List<String> usableNicknames = RANDOM_NICKNAMES.stream()
                .filter(nickname -> !usedNicknames.contains(nickname))
                .collect(Collectors.toList());

        if (usableNicknames.isEmpty()) {
            return "크루";
        }
        return usableNicknames.get(RANDOM.nextInt(usableNicknames.size()));
    }
}
