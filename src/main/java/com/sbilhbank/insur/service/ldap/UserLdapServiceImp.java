package com.sbilhbank.insur.service.ldap;

import com.sbilhbank.insur.dto.UserDto;
import com.sbilhbank.insur.entity.primary.UserLdap;
import com.sbilhbank.insur.extras.response.Response;
import com.sbilhbank.insur.repository.primary.UserLdapRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.ldap.query.LdapQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UserLdapServiceImp implements UserLdapService{
    @Autowired
    private UserLdapRepository userLdapRepository;
    @Override
    public Response getByUsernames(String username) {
        LdapQuery ldapQuery = LdapQueryBuilder.query()
                .countLimit(10)
                .where("objectclass").is("user")
                .and("objectcategory").is("person")
                .and("sAMAccountName").like(username + "*");
        Iterable<UserLdap> userLdaps = userLdapRepository.findAll(ldapQuery);
        return Response.ok(
                StreamSupport.stream(userLdaps.spliterator(), false)
                        .collect(Collectors.toList()).stream().map(s -> {
                            return UserDto
                                    .builder()
                                    .username(s.getUsername())
                                    .roleNames(null)
                                    .accountLocked(false)
                                    .enabled(true)
                                    .department(s.getDepartment())
                                    .fullName(s.getFullName())
                                    .memberOf(s.getMemberOf())
                                    .lastName(s.getLastName())
                                    .givenName(s.getGivenName())
                                    .mail(s.getMail())
                                    .build();
                        })
        );
    }
}
