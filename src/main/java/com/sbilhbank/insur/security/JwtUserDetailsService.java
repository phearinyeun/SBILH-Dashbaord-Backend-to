package com.sbilhbank.insur.security;

import com.sbilhbank.insur.entity.primary.User;
import com.sbilhbank.insur.repository.primary.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    public User loadUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new BadCredentialsException("Username doesn't exist: " + username));
    }
}
