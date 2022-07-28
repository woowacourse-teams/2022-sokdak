package com.wooteco.sokdak.auth.service;

import com.wooteco.sokdak.auth.domain.RefreshToken;
import com.wooteco.sokdak.auth.repository.RefreshTokenRepository;
import com.wooteco.sokdak.support.token.InvalidRefreshTokenException;
import com.wooteco.sokdak.support.token.TokenManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenManager tokenManager;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository,
                               TokenManager tokenManager) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.tokenManager = tokenManager;
    }

    @Transactional
    public void saveToken(String token, Long userId) {
        final RefreshToken refreshToken = new RefreshToken(userId, token);
        refreshTokenRepository.save(refreshToken);
    }

    @Transactional
    public void matches(String refreshToken, Long memberId) {
        final RefreshToken savedToken = refreshTokenRepository.findByMemberId(memberId)
                .orElseThrow();//Todo: 예외 메시지 정하기, db에 해당 멤버 id로 저장된 토큰이 없을 경우

        if (!tokenManager.isValid(savedToken.getToken())) {
            refreshTokenRepository.delete(savedToken);
            throw new InvalidRefreshTokenException();
        }
        savedToken.validateSameToken(refreshToken);
    }

    @Transactional
    public void deleteToken(Long memberId) {
        refreshTokenRepository.deleteAllByMemberId(memberId);
    }
}
