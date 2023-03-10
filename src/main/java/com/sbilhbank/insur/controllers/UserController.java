package com.sbilhbank.insur.controllers;

import com.sbilhbank.insur.dto.Create;
import com.sbilhbank.insur.dto.Update;
import com.sbilhbank.insur.dto.UserDto;
import com.sbilhbank.insur.extras.request.Pagination;
import com.sbilhbank.insur.extras.request.UserFilter;
import com.sbilhbank.insur.extras.response.Response;
import com.sbilhbank.insur.service.ldap.UserLdapService;
import com.sbilhbank.insur.service.user.UserService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final UserLdapService userLdapService;
    public UserController(UserService userService, UserLdapService userLdapService) {
        this.userService = userService;
        this.userLdapService = userLdapService;
    }
    @GetMapping
    public Response getAllUserContainCreatedBy(Pagination pagination, UserFilter userFilter) {
        Pageable paging = PageRequest.of(pagination.getPage(), pagination.getSize(), Sort.by("id").ascending());
        return userService.getAllUserContainCreatedBy(userFilter, paging);
    }
    @GetMapping("/detail/{username}")
    public Response getDetailByUsername(@PathVariable String username){
        return userService.getByUsername(username);
    }
    @GetMapping("/{username}")
    public Response getLdapDetailByUsername(@PathVariable String username){
        return userLdapService.getByUsernames(username);
    }
    @PostMapping
    public Response createUser(@Validated({Create.class}) @RequestBody UserDto userDto){
        return userService.createUser(userDto);
    }
    @PostMapping("/{username}")
    public Response updateUser(@PathVariable String username, @Validated({Update.class}) @RequestBody UserDto userDto){
        return userService.updateUser(username, userDto);
    }
}
