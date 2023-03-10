package com.sbilhbank.insur.security;

import com.sbilhbank.insur.entity.primary.Token;
import com.sbilhbank.insur.entity.primary.User;
import com.sbilhbank.insur.extras.response.JwtResponse;
import com.sbilhbank.insur.repository.primary.TokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Component
public class JwtTokenUtil implements Serializable {
    @Value("${jwt.access_token_expire}")
    public long JWT_TOKEN_VALIDITY = 1800000;
    @Value("${jwt.refresh_token_expire}")
    public long JWT_REFRESH_TOKEN_VALIDITY = 3600000;

    @Value("${jwt.secret}")
    private String secret;
    @Autowired
    private TokenRepository tokenRepository;

    // retrieve username from jwt token
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    // retrieve expiration date from jwt token
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    // for retrieveing any information from token we will need the secret key
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret)
                .parseClaimsJws(token).getBody();
    }

    // check if the token has expired
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    // generate token for user
    public JwtResponse generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        String username = userDetails.getUsername();
        claims.put("UserInfo", userDetails);
        return doGenerateToken(claims, username);
    }

    // while creating the token -
    // 1. Define claims of the token, like Issuer, Expiration, Subject, and the ID
    // 2. Sign the JWT using the HS512 algorithm and secret key.
    // 3. According to JWS Compact
    // compaction of the JWT to a URL-safe string
    private JwtResponse doGenerateToken(Map<String, Object> claims, String subject) {
        Date issueDate = new Date(System.currentTimeMillis());
        Date expireDate = new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY);
        Date expireRefreshDate = new Date(System.currentTimeMillis() + JWT_REFRESH_TOKEN_VALIDITY);
        return new JwtResponse(
                Jwts.builder().setClaims(claims).setSubject(subject)
                    .setIssuedAt(issueDate)
                    .setExpiration(expireDate)
                    .signWith(SignatureAlgorithm.HS512, secret).compact(),
                issueDate,
                expireDate,
                Jwts.builder().setSubject(subject)
                        .setIssuedAt(issueDate)
                        .setExpiration(expireRefreshDate)
                        .signWith(SignatureAlgorithm.HS512, secret).compact(),
                issueDate,
                expireRefreshDate
                );
    }

    // validate token
    public Boolean validateToken(String token, User userDetails) {
        final String username = getUsernameFromToken(token);
        Optional<Token> tokenOptional = tokenRepository.findByAccessTokenAndIsRevoke(token, false);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token)) && tokenOptional.isPresent();
    }
    public Boolean validateRefreshToken(String refreshToken) {
        Optional<Token> tokenOptional = tokenRepository.findByRefreshTokenAndIsRevoke(refreshToken, false);
        return !isTokenExpired(refreshToken) && tokenOptional.isPresent();
    }
}