package com.sbilhbank.insur.service.user;

import com.sbilhbank.insur.dto.UserDto;
import com.sbilhbank.insur.entity.primary.Role;
import com.sbilhbank.insur.entity.primary.User;
import com.sbilhbank.insur.entity.primary.UserLdap;
import com.sbilhbank.insur.exception.NotFoundEntityException;
import com.sbilhbank.insur.extras.request.UserFilter;
import com.sbilhbank.insur.extras.response.Response;
import com.sbilhbank.insur.mapper.UserMapper;
import com.sbilhbank.insur.repository.primary.RoleRepository;
import com.sbilhbank.insur.repository.primary.UserLdapRepository;
import com.sbilhbank.insur.repository.primary.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.ldap.query.LdapQueryBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImp implements UserService{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserLdapRepository userLdapRepository;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public Response getAllUser(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);;
        List<UserDto> userDtoList = users.map(s -> {
            LdapQuery ldapQuery = LdapQueryBuilder.query()
                    .countLimit(1)
                    .where("objectclass").is("user")
                    .and("objectcategory").is("person")
                    .and("sAMAccountName").is(s.getUsername());
            Optional<UserLdap> userLdapOptional = userLdapRepository.findOne(ldapQuery);
            if(userLdapOptional.isPresent()){
                UserLdap userLdap = userLdapOptional.get();
                userLdap.setId(s.getId());
                userLdap.setRoles(s.getRoles());
                userLdap.setEnabled(s.isEnabled());
                userLdap.setAccountLocked(s.isAccountLocked());
                return userMapper.userLdapToUserDto(userLdap);
            }
            return userMapper.userToUserDto(s);
        }).stream().collect(Collectors.toList());
        final int start = (int)pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), userDtoList.size());
        return Response.ok(new PageImpl<>(userDtoList.subList(start, end),pageable, users.getTotalElements()));
    }

    @Override
    public Response getAllUserContainCreatedBy(UserFilter userFilter, Pageable pageable) {
        Page<User> users = userRepository.findAllByUsername(userFilter.getUsername(), pageable);;
        List<UserDto> userDtoList = users.map(s -> {
            LdapQuery ldapQuery = LdapQueryBuilder.query()
                    .countLimit(1)
                    .where("objectclass").is("user")
                    .and("objectcategory").is("person")
                    .and("sAMAccountName").is(s.getUsername());
            Optional<UserLdap> userLdapOptional = userLdapRepository.findOne(ldapQuery);
            if(userLdapOptional.isPresent()){
                UserLdap userLdap = userLdapOptional.get();
                userLdap.setId(s.getId());
                userLdap.setRoles(s.getRoles());
                userLdap.setEnabled(s.isEnabled());
                userLdap.setAccountLocked(s.isAccountLocked());
                return userMapper.userLdapToUserDto(userLdap);
            }
            return userMapper.userToUserDto(s);
        }).stream().collect(Collectors.toList());
        final int start = (int)pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), userDtoList.size());
        return Response.ok(new PageImpl<>(userDtoList.subList(start, end),pageable, users.getTotalElements()));
    }

    @Override
    public Response createUser(UserDto userDto) {
        Set<Role> roles =
                userDto.getRoleNames().stream().map(s -> {
                    Optional<Role> roleOptional = roleRepository.findByName(s);
                    return roleOptional.orElse(null);
                }).filter(s -> s != null).collect(Collectors.toSet());
        User user = userMapper.userDtoToUser(userDto);
        user.setRoles(roles);
        user.setCredentialsExpired(false);
        user.setAccountExpired(false);
        if (!userDto.isAd()){
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }else user.setPassword(null);
        user = userRepository.save(user);
        LdapQuery ldapQuery = LdapQueryBuilder.query()
                .countLimit(1)
                .where("objectclass").is("user")
                .and("objectcategory").is("person")
                .and("sAMAccountName").is(user.getUsername());
        Optional<UserLdap> userLdapOptional = userLdapRepository.findOne(ldapQuery);
        UserDto userDtoRes = userMapper.userToUserDto(user);
        if(userLdapOptional.isPresent()){
            UserLdap userLdap = userLdapOptional.get();
            userLdap.setRoles(user.getRoles());
            userLdap.setEnabled(user.isEnabled());
            userLdap.setAccountLocked(user.isAccountLocked());
            return Response.ok(userMapper.userLdapToUserDto(userLdap));
        }
        return Response.ok(userDtoRes);
    }

    @Override
    public Response updateUser(String username, UserDto userDto) {
        Set<Role> roles =
                userDto.getRoleNames().stream().map(s -> {
                    Optional<Role> roleOptional = roleRepository.findByName(s);
                    return roleOptional.orElse(null);
                }).filter(s -> s != null).collect(Collectors.toSet());
        Optional<User> userOptional = userRepository.findByUsername(username);
        if(userOptional.isEmpty()) throw new NotFoundEntityException("User doesn't exist for update!");
        User user = userOptional.get();
        user.setAccountLocked(userDto.isAccountLocked());
        user.setEnabled(userDto.isEnabled());
        user.setRoles(roles);
        if (!userDto.isAd()){
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }else {
            user.setPassword(null);
        }
        user.setUsername(userDto.getUsername());
        user = userRepository.save(user);
        LdapQuery ldapQuery = LdapQueryBuilder.query()
                .countLimit(1)
                .where("objectclass").is("user")
                .and("objectcategory").is("person")
                .and("sAMAccountName").is(user.getUsername());
        Optional<UserLdap> userLdapOptional = userLdapRepository.findOne(ldapQuery);
        UserDto userDtoRes = userMapper.userToUserDto(user);
        if(userLdapOptional.isPresent()){
            UserLdap userLdap = userLdapOptional.get();
            userLdap.setRoles(user.getRoles());
            userLdap.setEnabled(user.isEnabled());
            userLdap.setAccountLocked(user.isAccountLocked());
            return Response.ok(userMapper.userLdapToUserDto(userLdap));
        }
        return Response.ok(userDtoRes);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Response getByUsername(String username) {
        Optional<User> userOptional = findByUsername(username);
        if(userOptional.isEmpty()) throw new NotFoundEntityException("User doesn't exist.");
        User user = userOptional.get();
        LdapQuery ldapQuery = LdapQueryBuilder.query()
                .countLimit(1)
                .where("objectclass").is("user")
                .and("objectcategory").is("person")
                .and("sAMAccountName").is(user.getUsername());
        Optional<UserLdap> userLdapOptional = userLdapRepository.findOne(ldapQuery);
        UserDto userDtoRes = userMapper.userToUserDto(user);
        if(userLdapOptional.isPresent()){
            UserLdap userLdap = userLdapOptional.get();
            userLdap.setId(user.getId());
            userLdap.setRoles(user.getRoles());
            userLdap.setEnabled(user.isEnabled());
            userLdap.setAccountLocked(user.isAccountLocked());
            return Response.ok(userMapper.userLdapToUserDto(userLdap));
        }
        return Response.ok(userDtoRes);
    }
}
