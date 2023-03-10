package com.sbilhbank.insur.config;

import com.sbilhbank.insur.entity.primary.Authority;
import com.sbilhbank.insur.entity.primary.Role;
import com.sbilhbank.insur.entity.primary.User;
import com.sbilhbank.insur.entity.primary.UserLdap;
import com.sbilhbank.insur.extras.constant.EnumConst;
import com.sbilhbank.insur.repository.primary.AuthorityRepository;
import com.sbilhbank.insur.repository.primary.RoleRepository;
import com.sbilhbank.insur.repository.primary.UserLdapRepository;
import com.sbilhbank.insur.repository.primary.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.ldap.query.LdapQueryBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@Configuration
@AllArgsConstructor
public class BootstrapDataConfig {
    private final RoleRepository roleRepository;
    private final AuthorityRepository authorityRepository;
    private final UserRepository userRepository;
    private final UserLdapRepository userLdapRepository;
    private final PasswordEncoder passwordEncoder;
    private final ListableBeanFactory listableBeanFactory;
    @Value("${user.admins}")
    private String[] userAdmins;
    @Value("${user.custom.admins}")
    private String[] userCustomAdmins;
    @EventListener(value = ApplicationReadyEvent.class)
    @Transactional
    public void init() {
        List<Authority> HBAuthorities= initHBAuthorities();
        List<Role> roles = initRoles(HBAuthorities);
        List<User> users = initUsers(roles);
        initUsersCustom(roles);
    }
    private List<String> getControllers(){
        Map<String, Object> controllers;
        controllers = listableBeanFactory.getBeansWithAnnotation(RestController.class);
        controllers.putAll(listableBeanFactory.getBeansWithAnnotation(Controller.class));
        controllers.replaceAll((s, o) -> {
            return o = s.toLowerCase().replaceAll("controller", "");
        });
        controllers.remove("basicErrorController");
        return controllers.values().stream().map(s -> s.toString()).collect(Collectors.toList());
    }
    private List<Authority> initHBAuthorities(){
        List<String> controllers = getControllers();
        controllers.forEach(s -> {
            Optional<Authority> controllerRead = authorityRepository
                    .findByName(s + "_" + EnumConst.Permission.READ.name());
            Optional<Authority> controllerWrite = authorityRepository
                    .findByName(s + "_" + EnumConst.Permission.WRITE.name());
            Optional<Authority> controllerUpdate = authorityRepository
                    .findByName(s + "_" + EnumConst.Permission.UPDATE.name());
            Optional<Authority> controllerDelete = authorityRepository
                    .findByName(s + "_" + EnumConst.Permission.DELETE.name());
            Optional<Authority> controllerChecker = authorityRepository
                    .findByName(s + "_" + EnumConst.Permission.CHECKER.name());
            Optional<Authority> controllerMaker = authorityRepository
                    .findByName(s + "_" + EnumConst.Permission.MAKER.name());
            Optional<Authority> controllerDownload = authorityRepository
                    .findByName(s + "_" + EnumConst.Permission.DOWNLOAD.name());
            Optional<Authority> controllerUpload = authorityRepository
                    .findByName(s + "_" + EnumConst.Permission.UPLOAD.name());
            if(controllerRead.isEmpty()){
                controllerRead = Optional.of(authorityRepository.save(
                        Authority
                                .builder()
                                .name(s + "_" + EnumConst.Permission.READ.name())
                                .build()
                ));
            }
            if(controllerWrite.isEmpty()){
                controllerWrite = Optional.of(authorityRepository.save(
                        Authority
                                .builder()
                                .name(s + "_" + EnumConst.Permission.WRITE.name())
                                .build()
                ));
            }
            if(controllerUpdate.isEmpty()){
                controllerUpdate = Optional.of(authorityRepository.save(
                        Authority
                                .builder()
                                .name(s + "_" + EnumConst.Permission.UPDATE.name())
                                .build()
                ));
            }
            if(controllerDelete.isEmpty()){
                controllerDelete = Optional.of(authorityRepository.save(
                        Authority
                                .builder()
                                .name(s + "_" + EnumConst.Permission.DELETE.name())
                                .build()
                ));
            }
            if(controllerChecker.isEmpty()){
                controllerChecker = Optional.of(authorityRepository.save(
                        Authority
                                .builder()
                                .name(s + "_" + EnumConst.Permission.CHECKER.name())
                                .build()
                ));
            }
            if(controllerMaker.isEmpty()){
                controllerMaker = Optional.of(authorityRepository.save(
                        Authority
                                .builder()
                                .name(s + "_" + EnumConst.Permission.MAKER.name())
                                .build()
                ));
            }
            if(controllerUpload.isEmpty()){
                controllerUpload = Optional.of(authorityRepository.save(
                        Authority
                                .builder()
                                .name(s + "_" + EnumConst.Permission.UPLOAD.name())
                                .build()
                ));
            }
            if(controllerDownload.isEmpty()){
                controllerDownload = Optional.of(authorityRepository.save(
                        Authority
                                .builder()
                                .name(s + "_" + EnumConst.Permission.DOWNLOAD.name())
                                .build()
                ));
            }
        });

        Optional<Authority> authorityHBRead = authorityRepository
                .findByName(EnumConst.PrefixPermission.DASHBOARD_.name() + EnumConst.Permission.READ.name());
        Optional<Authority> authorityHBWrite = authorityRepository
                .findByName(EnumConst.PrefixPermission.DASHBOARD_.name() + EnumConst.Permission.WRITE.name());
        Optional<Authority> authorityHBUpdate = authorityRepository
                .findByName(EnumConst.PrefixPermission.DASHBOARD_.name() + EnumConst.Permission.UPDATE.name());
        Optional<Authority> authorityHBDelete = authorityRepository
                .findByName(EnumConst.PrefixPermission.DASHBOARD_.name() + EnumConst.Permission.DELETE.name());
        Optional<Authority> authorityHBChecker = authorityRepository
                .findByName(EnumConst.PrefixPermission.DASHBOARD_.name() + EnumConst.Permission.CHECKER.name());
        Optional<Authority> authorityHBMaker = authorityRepository
                .findByName(EnumConst.PrefixPermission.DASHBOARD_.name() + EnumConst.Permission.MAKER.name());
        if(authorityHBRead.isEmpty()){
            authorityHBRead = Optional.of(authorityRepository.save(
                    Authority
                            .builder()
                            .name(EnumConst.PrefixPermission.DASHBOARD_.name() + EnumConst.Permission.READ.name())
                            .build()
            ));
        }
        if(authorityHBWrite.isEmpty()){
            authorityHBWrite = Optional.of(authorityRepository.save(
                    Authority
                            .builder()
                            .name(EnumConst.PrefixPermission.DASHBOARD_.name() + EnumConst.Permission.WRITE.name())
                            .build()
            ));
        }
        if(authorityHBUpdate.isEmpty()){
            authorityHBUpdate = Optional.of(authorityRepository.save(
                    Authority
                            .builder()
                            .name(EnumConst.PrefixPermission.DASHBOARD_.name() + EnumConst.Permission.UPDATE.name())
                            .build()
            ));
        }
        if(authorityHBDelete.isEmpty()){
            authorityHBDelete = Optional.of(authorityRepository.save(
                    Authority
                            .builder()
                            .name(EnumConst.PrefixPermission.DASHBOARD_.name() + EnumConst.Permission.DELETE.name())
                            .build()
            ));
        }
        if(authorityHBMaker.isEmpty()){
            authorityHBMaker = Optional.of(authorityRepository.save(
                    Authority
                            .builder()
                            .name(EnumConst.PrefixPermission.DASHBOARD_.name() + EnumConst.Permission.MAKER.name())
                            .build()
            ));
        }
        if(authorityHBChecker.isEmpty()){
            authorityHBChecker = Optional.of(authorityRepository.save(
                    Authority
                            .builder()
                            .name(EnumConst.PrefixPermission.DASHBOARD_.name() + EnumConst.Permission.CHECKER.name())
                            .build()
            ));
        }
        return List.of(
             authorityHBRead.get(),
             authorityHBWrite.get(),
             authorityHBUpdate.get(),
             authorityHBDelete.get(),
             authorityHBMaker.get(),
             authorityHBChecker.get()
        );
    }
    private List<Role> initRoles(List<Authority> HBAuthorities){
        Optional<Role> roleAdmin = roleRepository.findByName(Role.ADMIN_ROLE);
        Optional<Role> roleChecker = roleRepository.findByName(Role.CHECKER_ROLE);
        Optional<Role> roleMaker = roleRepository.findByName(Role.MAKER_ROLE);
        Optional<Role> roleHB = roleRepository.findByName(Role.INSUR_ROLE);
        if(roleAdmin.isEmpty()){
            roleAdmin = Optional.of(roleRepository.save(Role
                    .builder()
                            .name(Role.ADMIN_ROLE)
                            .authorities(null)
                    .build()));
        }
        if(roleMaker.isEmpty()){
            roleMaker = Optional.of(roleRepository.save(Role
                    .builder()
                    .name(Role.MAKER_ROLE)
                    .authorities(Set.of(
//                            HBAuthorities.get(0), HBAuthorities.get(1),
//                            HBAuthorities.get(2), HBAuthorities.get(3),
                            HBAuthorities.get(4)))
                    .build()));
        }
        if(roleChecker.isEmpty()){
            roleChecker = Optional.of(roleRepository.save(Role
                    .builder()
                    .name(Role.CHECKER_ROLE)
                    .authorities(Set.of(
//                                    HBAuthorities.get(0), HBAuthorities.get(1),
//                                    HBAuthorities.get(2), HBAuthorities.get(3),
                                    HBAuthorities.get(5)))
                    .build()));
        }
        if(roleHB.isEmpty()){
            roleHB = Optional.of(roleRepository.save(Role
                    .builder()
                    .name(Role.INSUR_ROLE)
                    .authorities(Set.of(
                                    HBAuthorities.get(0), HBAuthorities.get(1),
                                    HBAuthorities.get(2), HBAuthorities.get(3)))
                    .build()));
        }
        return List.of(roleAdmin.get(), roleMaker.get(), roleChecker.get(), roleHB.get());
    }
    private List<User> initUsers(List<Role> roles){
        if(userAdmins.length > 0){
            return Arrays.stream(userAdmins).toList().stream().map(s -> {
                LdapQuery ldapQuery = LdapQueryBuilder.query()
                        .countLimit(1)
                        .where("objectclass").is("user")
                        .and("objectcategory").is("person")
                        .and("sAMAccountName").is(s);
                Optional<UserLdap> userLdap = userLdapRepository.findOne(ldapQuery);
                if(userLdap.isPresent()){
                    Optional<User> user = userRepository.findByUsername(s);
                    if(user.isEmpty()){
                        return userRepository.save(
                                User
                                .builder()
                                .username(s)
                                .enabled(true)
                                .roles(Set.of(roles.get(0)))
                                .build());
                    }else
                        return user.get();
                }
                return null;
            }).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    private void initUsersCustom(List<Role> roles){
        if(userCustomAdmins.length > 0){
            Arrays.stream(userCustomAdmins).forEach(s -> {

                Optional<User> user = userRepository.findByUsername(s);
                if(user.isEmpty()){
                    userRepository.save(
                            User
                                    .builder()
                                    .username(s)
                                    .enabled(true)
                                    .password(passwordEncoder.encode("P@$$30rd"))
                                    .roles(Set.of(roles.get(0)))
                                    .build());
                }
            });
        }
    }
}
