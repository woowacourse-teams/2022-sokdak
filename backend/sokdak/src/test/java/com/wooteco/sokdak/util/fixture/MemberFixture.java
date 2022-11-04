package com.wooteco.sokdak.util.fixture;

import com.wooteco.sokdak.auth.domain.encryptor.EncryptorFactory;
import com.wooteco.sokdak.auth.domain.encryptor.EncryptorI;
import com.wooteco.sokdak.auth.dto.LoginRequest;
import com.wooteco.sokdak.member.domain.Member;
import com.wooteco.sokdak.member.domain.Nickname;
import com.wooteco.sokdak.member.domain.Password;
import com.wooteco.sokdak.member.domain.Username;
import java.util.List;

public class MemberFixture {

    public static final EncryptorI ENCRYPTOR = new EncryptorFactory().getEncryptor();
    public static final String APPLICANT_USERNAME = "applicant";

    public static final String VALID_USERNAME_TEXT = "chris";
    public static final Username VALID_USERNAME = Username.of(ENCRYPTOR, VALID_USERNAME_TEXT);

    public static final String VALID_PASSWORD_TEXT = "Abcd123!@";
    public static final Password VALID_PASSWORD = Password.of(ENCRYPTOR, VALID_PASSWORD_TEXT);

    public static final String VALID_NICKNAME_TEXT = "testNickname";
    public static final Nickname VALID_NICKNAME = new Nickname("testNickname");

    public static final LoginRequest VALID_LOGIN_REQUEST = new LoginRequest(VALID_USERNAME_TEXT, VALID_PASSWORD_TEXT);
    public static final LoginRequest APPLICANT_LOGIN_REQUEST = new LoginRequest(APPLICANT_USERNAME, VALID_PASSWORD_TEXT);
    public static final LoginRequest INVALID_LOGIN_REQUEST = new LoginRequest("invalidUsername", "invalidPassword1!");

    public static final Long CHRIS_ID = 3L;

    public static List<Member> getMembersForReport() {

        return List.of(
                Member.builder().id(1L).username(Username.of(ENCRYPTOR, "chris"))
                        .password(VALID_PASSWORD).nickname(new Nickname("chrisNickname")).build(),
                Member.builder().id(2L).username(Username.of(ENCRYPTOR, "josh"))
                        .password(VALID_PASSWORD).nickname(new Nickname("joshNickname")).build(),
                Member.builder().id(3L).username(Username.of(ENCRYPTOR, "thor"))
                        .password(VALID_PASSWORD).nickname(new Nickname("thorNickname")).build(),
                Member.builder().id(4L).username(Username.of(ENCRYPTOR, "hunch"))
                        .password(VALID_PASSWORD).nickname(new Nickname("hunchNickname")).build(),
                Member.builder().id(5L).username(Username.of(ENCRYPTOR, "east"))
                        .password(VALID_PASSWORD).nickname(new Nickname("eastNickname")).build()
        );
    }
}
