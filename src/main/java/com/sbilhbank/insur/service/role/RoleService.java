package com.sbilhbank.insur.service.role;

import com.sbilhbank.insur.extras.response.Response;

public interface RoleService {
    Response getAllRoleName();
    boolean isRoleExist(String name);
}
