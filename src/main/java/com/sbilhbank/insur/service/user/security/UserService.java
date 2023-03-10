package com.sbilhbank.insur.service.user.security;


import com.sbilhbank.insur.entity.primary.User;
import com.sbilhbank.insur.repository.primary.UserRepository;
import com.sbilhbank.insur.security.JwtUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtUserDetailsService userDetailsService;
    public User findByUsername(String username){
        Optional<User> user = userRepository.findByUsername(username);
        if(user.isPresent()){
            return user.get();
        }
//        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//        if (Optional.ofNullable(userDetails).isPresent()) {
//            User userDetail = new User();
//            userDetail.setUsername(userDetails.getUsername());
//            return userDetail;
//        }
        throw new UsernameNotFoundException("User doesn't exist: " + username);
    }
    // we can omit this part
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String name) {
        Optional<User> user = userRepository.findByUsername(name);

        if(user.isPresent()){
            return new SecuredUserDetails(user.get());
        }
//        UserDetails userDetails = userDetailsService.loadUserByUsername(name);
//        if (Optional.ofNullable(userDetails).isPresent()) {
//            return userDetails;
//        }
        throw new UsernameNotFoundException("User doesn't exist: " + name);
    }
}
