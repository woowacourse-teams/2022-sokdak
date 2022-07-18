package com.wooteco.sokdak.member.service;

import com.wooteco.sokdak.auth.service.AuthService;
import com.wooteco.sokdak.auth.service.Encryptor;
import com.wooteco.sokdak.member.domain.Member;
import com.wooteco.sokdak.member.domain.Nickname;
import com.wooteco.sokdak.member.domain.Username;
import com.wooteco.sokdak.member.dto.SignupRequest;
import com.wooteco.sokdak.member.dto.UniqueResponse;
import com.wooteco.sokdak.member.dto.VerificationRequest;
import com.wooteco.sokdak.member.exception.InvalidSignupFlowException;
import com.wooteco.sokdak.member.exception.PasswordConfirmationException;
import com.wooteco.sokdak.member.repository.MemberRepository;
import com.wooteco.sokdak.member.util.RandomNicknameGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final EmailService emailService;
    private final AuthService authService;
    private final Encryptor encryptor;

    public MemberService(MemberRepository memberRepository, EmailService emailService,
                         AuthService authService, Encryptor encryptor) {
        this.memberRepository = memberRepository;
        this.emailService = emailService;
        this.authService = authService;
        this.encryptor = encryptor;
    }

    public UniqueResponse checkUniqueUsername(String username) {
        boolean unique = !memberRepository.existsMemberByUsername(new Username(username));
        return new UniqueResponse(unique);
    }

    public UniqueResponse checkUniqueNickname(String nickname) {
        boolean unique = !memberRepository.existsMemberByNickname(new Nickname(nickname));
        return new UniqueResponse(unique);
    }

    @Transactional
    public void signUp(SignupRequest signupRequest) {
        validate(signupRequest);

        Member member = Member.builder()
                .username(signupRequest.getUsername())
                .password(encryptor.encrypt(signupRequest.getPassword()))
                .nickname(RandomNicknameGenerator.generate())
                .build();
        memberRepository.save(member);
        authService.useTicket(signupRequest.getEmail());
    }

    private void validate(SignupRequest signupRequest) {
        validateSerialNumber(signupRequest);
        validateAuthCode(signupRequest);
        confirmPassword(signupRequest.getPassword(), signupRequest.getPasswordConfirmation());
        validateUniqueNickname(signupRequest);
        validateUniqueUsername(signupRequest);
    }

    private void validateUniqueUsername(SignupRequest signupRequest) {
        boolean isDuplicatedUsername = memberRepository
                .existsMemberByUsername(new Username(signupRequest.getUsername()));
        if (isDuplicatedUsername) {
            throw new InvalidSignupFlowException();
        }
    }

    private void validateUniqueNickname(SignupRequest signupRequest) {
        boolean isDuplicatedNickname = memberRepository
                .existsMemberByNickname(new Nickname(signupRequest.getNickname()));
        if (isDuplicatedNickname) {
            throw new InvalidSignupFlowException();
        }
    }

    private void validateAuthCode(SignupRequest signupRequest) {
        VerificationRequest verificationRequest = new VerificationRequest(
                signupRequest.getEmail(), signupRequest.getCode());
        emailService.verifyAuthCode(verificationRequest);
    }

    private void validateSerialNumber(SignupRequest signupRequest) {
        String serialNumber = encryptor.encrypt(signupRequest.getEmail());
        authService.validateSignUpMember(serialNumber);
    }

    private void confirmPassword(String password, String passwordConfirmation) {
        if (!password.equals(passwordConfirmation)) {
            throw new PasswordConfirmationException();
        }
    }
}
