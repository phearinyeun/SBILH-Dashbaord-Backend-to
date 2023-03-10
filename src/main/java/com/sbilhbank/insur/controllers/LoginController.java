package com.sbilhbank.insur.controllers;

import com.sbilhbank.insur.extras.request.JwtRequest;
import com.sbilhbank.insur.extras.request.TokenRequest;
import com.sbilhbank.insur.extras.response.Response;
import com.sbilhbank.insur.service.login.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/authenticate")
public class LoginController {
    @Autowired
    private AccountService accountService;
    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@Valid @RequestBody JwtRequest authenticationRequest) {
        return ResponseEntity.ok(accountService.authenticate(authenticationRequest));
    }
    @PostMapping("/logout")
    public Response logoutAuthenticationToken(@Valid @RequestBody TokenRequest token) {
        return accountService.logout(token);
    }
    @PostMapping("/refresh")
    public  ResponseEntity<?> refreshToken(@Valid @RequestBody TokenRequest tokenRequest){
        return ResponseEntity.ok(accountService.refreshToken(tokenRequest));
    }
    @GetMapping("/me")
    public Response me(HttpServletRequest httpServletRequest) {
        String test = SecurityContextHolder.getContext().getAuthentication().getName();
        String token = httpServletRequest.getHeader("Authorization");
        if(token != null){
            token = token.replaceAll("Bearer ","");
        }
        return accountService.me(token);
    }
}
