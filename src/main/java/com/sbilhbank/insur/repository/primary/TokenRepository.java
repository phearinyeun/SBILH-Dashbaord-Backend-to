package com.sbilhbank.insur.repository.primary;

import com.sbilhbank.insur.entity.primary.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByAccessToken(String token);
    Optional<Token> findByAccessTokenAndIsRevoke(String accessToken, boolean isRevoke);
    Optional<Token> findByRefreshTokenAndIsRevoke(String refreshToken, boolean isRevoke);
}
