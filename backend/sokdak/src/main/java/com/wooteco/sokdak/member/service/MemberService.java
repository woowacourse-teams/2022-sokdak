package com.wooteco.sokdak.member.service;

import com.wooteco.sokdak.auth.dto.AuthInfo;
import com.wooteco.sokdak.auth.service.AuthService;
import com.wooteco.sokdak.auth.domain.encryptor.Encryptor;
import com.wooteco.sokdak.member.domain.Member;
import com.wooteco.sokdak.member.domain.Nickname;
import com.wooteco.sokdak.member.dto.SignupRequest;
import com.wooteco.sokdak.member.dto.UniqueResponse;
import com.wooteco.sokdak.member.dto.VerificationRequest;
import com.wooteco.sokdak.member.exception.InvalidSignupFlowException;
import com.wooteco.sokdak.member.exception.MemberNotFoundException;
import com.wooteco.sokdak.member.exception.PasswordConfirmationException;
import com.wooteco.sokdak.member.repository.MemberRepository;
import com.wooteco.sokdak.member.dto.NicknameResponse;
import com.wooteco.sokdak.member.dto.NicknameUpdateRequest;
import com.wooteco.sokdak.member.exception.DuplicateNicknameException;
import com.wooteco.sokdak.ticket.service.RegisterService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final EmailService emailService;
    private final AuthService authService;
    private final RegisterService registerService;
    private final Encryptor encryptor;

    public MemberService(MemberRepository memberRepository, EmailService emailService,
                         AuthService authService, RegisterService registerService,
                         Encryptor encryptor) {
        this.memberRepository = memberRepository;
        this.emailService = emailService;
        this.authService = authService;
        this.registerService = registerService;
        this.encryptor = encryptor;
    }

    public UniqueResponse checkUniqueUsername(String username) {
        String hashedUsername = encryptor.encode(username);
        boolean unique = !memberRepository.existsMemberByUsernameValue(hashedUsername);
        return new UniqueResponse(unique);
    }

    public UniqueResponse checkUniqueNickname(String nickname) {
        boolean unique = !memberRepository.existsMemberByNicknameValue(nickname);
        return new UniqueResponse(unique);
    }

    @Transactional
    public void signUp(SignupRequest signupRequest) {
        validate(signupRequest);

        Member member = Member.builder()
                .username(signupRequest.getUsername())
                .password(signupRequest.getPassword())
                .nickname(signupRequest.getNickname())
                .build();
        memberRepository.save(member);
        registerService.useTicket(signupRequest.getEmail());
    }

    @Transactional
    public void signUpAsApplicant(SignupRequest signupRequest) {
        validateForApplicant(signupRequest);

        Member member = Member.applicant(signupRequest.getUsername(), signupRequest.getPassword(),
                signupRequest.getNickname());
        memberRepository.save(member);
    }

    private void validate(SignupRequest signupRequest) {
        validateSerialNumber(signupRequest);
        validateAuthCode(signupRequest);
        confirmPassword(signupRequest.getPassword(), signupRequest.getPasswordConfirmation());
        validateUniqueNickname(signupRequest);
        validateUniqueUsername(signupRequest);
    }

    private void validateForApplicant(SignupRequest signupRequest) {
        confirmPassword(signupRequest.getPassword(), signupRequest.getPasswordConfirmation());
        validateUniqueNickname(signupRequest);
        validateUniqueUsername(signupRequest);
    }

    private void validateUniqueUsername(SignupRequest signupRequest) {
        boolean isDuplicatedUsername = memberRepository
                .existsMemberByUsernameValue(encryptor.encode(signupRequest.getUsername()));
        if (isDuplicatedUsername) {
            throw new InvalidSignupFlowException();
        }
    }

    private void validateUniqueNickname(SignupRequest signupRequest) {
        boolean isDuplicatedNickname = memberRepository
                .existsMemberByNicknameValue(signupRequest.getNickname());
        if (isDuplicatedNickname) {
            throw new InvalidSignupFlowException();
        }
    }

    private void validateAuthCode(SignupRequest signupRequest) {
        VerificationRequest verificationRequest = new VerificationRequest(
                signupRequest.getEmail(), signupRequest.getCode());
        registerService.verifyAuthCode(verificationRequest);
    }

    private void validateSerialNumber(SignupRequest signupRequest) {
        String serialNumber = encryptor.encode(signupRequest.getEmail());
        registerService.validateSignUpMember(serialNumber);
    }

    private void confirmPassword(String password, String passwordConfirmation) {
        if (!password.equals(passwordConfirmation)) {
            throw new PasswordConfirmationException();
        }
    }

    @Transactional
    public void editNickname(NicknameUpdateRequest nicknameUpdateRequest, AuthInfo authInfo) {
        Member member = memberRepository.findById(authInfo.getId())
                .orElseThrow(MemberNotFoundException::new);

        Nickname validNickname = new Nickname(nicknameUpdateRequest.getNickname());
        validateUniqueNickname(validNickname);
        member.updateNickname(validNickname);
    }

    private void validateUniqueNickname(Nickname validNickname) {
        if (memberRepository.existsMemberByNickname(validNickname)) {
            throw new DuplicateNicknameException();
        }
    }

    public NicknameResponse findNickname(AuthInfo authInfo) {
        Member member = memberRepository.findById(authInfo.getId())
                .orElseThrow(MemberNotFoundException::new);
        return NicknameResponse.of(member);
    }
}
