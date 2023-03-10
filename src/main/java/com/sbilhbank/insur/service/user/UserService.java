package com.sbilhbank.insur.service.user;

import com.sbilhbank.insur.dto.UserDto;
import com.sbilhbank.insur.entity.primary.User;
import com.sbilhbank.insur.extras.request.UserFilter;
import com.sbilhbank.insur.extras.response.Response;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserService {
    Response getAllUser(Pageable pageable);
    Response getAllUserContainCreatedBy(UserFilter userFilter, Pageable pageable);
    Response createUser(UserDto userDto);
    Response updateUser(String username, UserDto userDto);
    Optional<User> findByUsername(String username);
    Response getByUsername(String username);

}
