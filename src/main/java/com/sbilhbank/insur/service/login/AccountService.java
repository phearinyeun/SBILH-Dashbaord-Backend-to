package com.sbilhbank.insur.service.login;

import com.sbilhbank.insur.entity.primary.Token;
import com.sbilhbank.insur.entity.primary.User;
import com.sbilhbank.insur.exception.NotFoundEntityException;
import com.sbilhbank.insur.extras.request.JwtRequest;
import com.sbilhbank.insur.extras.request.TokenRequest;
import com.sbilhbank.insur.extras.response.JwtResponse;
import com.sbilhbank.insur.extras.response.Response;
import com.sbilhbank.insur.mapper.UserMapper;
import com.sbilhbank.insur.repository.primary.TokenRepository;
import com.sbilhbank.insur.security.JwtTokenUtil;
import com.sbilhbank.insur.service.user.security.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.Optional;

@Service
public class AccountService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserService userService;
    @Autowired
    TokenRepository tokenRepository;
    @Autowired
    private UserMapper userMapper;
    public JwtResponse authenticate(JwtRequest authenticationRequest){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
        final UserDetails userDetails = userService.loadUserByUsername(authenticationRequest.getUsername());
        final JwtResponse token = jwtTokenUtil.generateToken(userDetails);
        User user = userService.findByUsername(userDetails.getUsername());
        tokenRepository.save(
                Token
                        .builder()
                        .accessToken(token.getAccessToken())
                        .refreshToken(token.getRefreshToken())
                        .accessTokenIssueAt(token.getAccessTokenIssueAt())
                        .accessTokenExpireAt(token.getAccessTokenExpireAt())
                        .refreshTokenIssueAt(token.getRefreshTokenIssueAt())
                        .refreshTokenExpireAt(token.getRefreshTokenExpireAt())
                        .user(user)
                        .isRevoke(false)
                        .build()
        );
        return token;
    }
    @Transactional
    public JwtResponse refreshToken(@Valid TokenRequest tokenRequest) {
        try{
            if(!jwtTokenUtil.validateRefreshToken(tokenRequest.getToken())) throw new BadCredentialsException(HttpStatus.UNAUTHORIZED.name());
            Optional<Token> tokenOptional = tokenRepository.findByRefreshTokenAndIsRevoke(tokenRequest.getToken(),false);
            if(tokenOptional.isEmpty()) throw new BadCredentialsException(HttpStatus.UNAUTHORIZED.name());
            Token oldToken = tokenOptional.get();
            User userFromToken = oldToken.getUser();
            if(userFromToken == null) throw new BadCredentialsException(HttpStatus.UNAUTHORIZED.name());
            final UserDetails userDetails = userService.loadUserByUsername(userFromToken.getUsername());
            final JwtResponse token = jwtTokenUtil.generateToken(userDetails);
            User user = userService.findByUsername(userDetails.getUsername());
            tokenRepository.save(
                    Token
                            .builder()
                            .accessToken(token.getAccessToken())
                            .refreshToken(token.getRefreshToken())
                            .accessTokenIssueAt(token.getAccessTokenIssueAt())
                            .accessTokenExpireAt(token.getAccessTokenExpireAt())
                            .refreshTokenIssueAt(token.getRefreshTokenIssueAt())
                            .refreshTokenExpireAt(token.getRefreshTokenExpireAt())
                            .user(user)
                            .isRevoke(false)
                            .build()
            );
            oldToken.setRevoke(true);
            tokenRepository.save(oldToken);
            return token;
        } catch (Exception ex){
            throw new BadCredentialsException(HttpStatus.UNAUTHORIZED.name());
        }
    }
    public Response logout(@Valid TokenRequest tokenRequest){
        Optional<Token> token = tokenRepository.findByAccessToken(tokenRequest.getToken());
        if(token.isEmpty()){
            Response.failed(new NotFoundEntityException("fail logout: can not find toke: "+ tokenRequest.getToken()));
        }
        token.get().setRevoke(true);
        tokenRepository.save(token.get());
        SecurityContextHolder.clearContext();
        return Response.ok("Logout successfully");
    }
    public Response me(String token){
        String username = jwtTokenUtil.getUsernameFromToken(token);
        User me = userService.findByUsername(username);
//        me.setToken(Token.builder()
//                .accessToken(token)
//                .refreshToken(me.getToken()!=null?me.getToken().getRefreshToken():null)
//                .build());
        me.setToken(null);
        me.getRoles().forEach(s -> {
            s.setUsers(null);
            s.getAuthorities().forEach(a -> a.setRoles(null));
        });
        return Response.ok(userMapper.userToUserDto(me));
    }
}
