package com.wooteco.sokdak.member.service;

import static com.wooteco.sokdak.member.util.Encryptor.encrypt;

import com.wooteco.sokdak.member.domain.member.Member;
import com.wooteco.sokdak.member.domain.member.Username;
import com.wooteco.sokdak.member.dto.SignupRequest;
import com.wooteco.sokdak.member.dto.UsernameUniqueResponse;
import com.wooteco.sokdak.member.dto.VerificationRequest;
import com.wooteco.sokdak.member.exception.PasswordConfirmationException;
import com.wooteco.sokdak.member.repository.MemberRepository;
import com.wooteco.sokdak.member.util.RandomNicknameGenerator;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final EmailService emailService;

    public MemberService(MemberRepository memberRepository, EmailService emailService) {
        this.memberRepository = memberRepository;
        this.emailService = emailService;
    }

    public UsernameUniqueResponse checkUnique(String username) {
        boolean unique = !memberRepository.existsMemberByUsername(new Username(username));
        return new UsernameUniqueResponse(unique);
    }

    public void signUp(SignupRequest signupRequest) {
        validate(signupRequest);

        Member member = Member.builder()
                .username(signupRequest.getUsername())
                .password(encrypt(signupRequest.getPassword()))
                .nickname(RandomNicknameGenerator.generate())
                .build();
        memberRepository.save(member);
    }

    private void validate(SignupRequest signupRequest) {
        String serialNumber = encrypt(signupRequest.getEmail());
        emailService.validate(serialNumber);

        VerificationRequest verificationRequest = new VerificationRequest(
                signupRequest.getEmail(), signupRequest.getCode());
        emailService.verifyAuthCode(verificationRequest);

        confirmPassword(signupRequest.getPassword(), signupRequest.getPasswordConfirmation());
    }

    private void confirmPassword(String password, String passwordConfirmation) {
        if (!password.equals(passwordConfirmation)) {
            throw new PasswordConfirmationException();
        }
    }
}
