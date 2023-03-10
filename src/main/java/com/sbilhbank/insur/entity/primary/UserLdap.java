package com.sbilhbank.insur.entity.primary;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;
import org.springframework.ldap.odm.annotations.Transient;

import javax.naming.Name;
import java.util.Set;

@Entry(
        base = "OU=HQ,OU=SBILH OU,DC=sbilhbank,DC=com,DC=kh",
        objectClasses = { "person", "user" })
@Data
@SuperBuilder
@NoArgsConstructor
public class UserLdap {
    @Id
    private Name naming;
    @Transient
    private Long id;
    private @Attribute(name = "department") String department;
    private @Attribute(name = "sAMAccountName") String username;
    private @Attribute(name = "cn") String fullName;
    private @Attribute(name = "memberOf") String memberOf;
    private @Attribute(name = "sn") String lastName;
    private @Attribute(name = "givenname") String givenName;
    private @Attribute(name = "mail") String mail;
    private String manager;
    @Transient
    private Set<Role> roles;
    @Transient
    private boolean enabled;
    private boolean accountLocked;
}