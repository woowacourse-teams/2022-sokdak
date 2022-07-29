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
    public void saveToken(String token, Long memberId) {
        deleteToken(memberId);
        RefreshToken refreshToken = new RefreshToken(memberId, token);
        refreshTokenRepository.save(refreshToken);
    }

    @Transactional
    public void matches(String refreshToken, Long memberId) {
        RefreshToken savedToken = refreshTokenRepository.findByMemberId(memberId)
                .orElseThrow(InvalidRefreshTokenException::new);

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
