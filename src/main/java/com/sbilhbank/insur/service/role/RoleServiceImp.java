package com.sbilhbank.insur.service.role;

import com.sbilhbank.insur.entity.primary.Role;
import com.sbilhbank.insur.extras.response.Response;
import com.sbilhbank.insur.repository.primary.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoleServiceImp implements RoleService{
    @Autowired
    private RoleRepository roleRepository;
    @Override
    public Response getAllRoleName() {
        List<Role> roles = roleRepository.findAll();
        return Response.ok(roles.stream().map(s -> s.getName()).collect(Collectors.toList()));
    }

    @Override
    public boolean isRoleExist(String name) {
        Optional<Role> role = roleRepository.findByName(name);
        return role.isPresent();
    }
}
