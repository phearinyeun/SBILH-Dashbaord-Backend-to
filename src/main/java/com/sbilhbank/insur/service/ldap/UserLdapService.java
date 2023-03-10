package com.sbilhbank.insur.service.ldap;

import com.sbilhbank.insur.extras.response.Response;

public interface UserLdapService {
    Response getByUsernames(String username);
}
