package com.sbilhbank.insur.controllers;

import com.sbilhbank.insur.extras.response.Response;
import com.sbilhbank.insur.service.role.RoleService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/role")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }
    @GetMapping
    public Response getAllRoleName(){
        return roleService.getAllRoleName();
    }
}
