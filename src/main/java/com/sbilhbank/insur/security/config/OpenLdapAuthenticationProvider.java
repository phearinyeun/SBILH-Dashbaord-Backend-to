package com.sbilhbank.insur.security.config;

import com.sbilhbank.insur.repository.primary.AuthorityRepository;
import com.sbilhbank.insur.repository.primary.RoleRepository;
import com.sbilhbank.insur.repository.primary.UserRepository;
import com.sbilhbank.insur.service.user.security.SecuredUserDetails;
import com.sbilhbank.insur.entity.primary.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.NamingException;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class OpenLdapAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private LdapContextSource contextSource;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private LdapTemplate ldapTemplate;

//    @PostConstruct
//    private void initContext() throws Exception {
//        LdapQuery query = query()
//                .searchScope(SearchScope.SUBTREE)
////                .timeLimit(3)
////                .countLimit(10)
//                .attributes("mail")
//                .base(LdapUtils.emptyLdapName())
//                .where("objectclass").is("user");
//         ldapTemplate.search(query,ctx -> {
//             System.err.println(ctx);
//         });
//    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        try{
            AndFilter filter = new AndFilter();
            filter.and(new EqualsFilter("sAMAccountName", authentication.getName()));
            filter.and(new EqualsFilter("objectclass", "user"));
            filter.and(new EqualsFilter("objectcategory","person"));
//            filter.and(new LikeFilter("sAMAccountName", "*"+authentication.getName()));
//            LdapQuery ldapQuery = LdapQueryBuilder.query()
//                    .where("objectclass").is("user")
//                    .and("objectcategory").is("person")
//                    .and("cn").like("*");
//            ldapTemplate.search(ldapQuery, attributes -> {
//                System.err.println(attributes.getName());
//            });
            Boolean authenticate = ldapTemplate.authenticate(LdapUtils.emptyLdapName(), filter.encode(),
                    authentication.getCredentials().toString());
            if (authenticate) {
//                Optional<Authority> authority = authorityRepository.findByName(Authority.DEFAULT_AUTHORITY);
//                if(authority.isEmpty()){
//                   authority = Optional.of(authorityRepository.save(
//                      Authority
//                              .builder()
//                              .name(Authority.DEFAULT_AUTHORITY)
//                              .build()
//                   ));
//                }
//                //check role exist or not
//                //if not do create
//                //else do nothing
//                Optional<Role> role = roleRepository.findByName(Role.DEFAULT_ROLE);
//                if(role.isEmpty()){
//                    role = Optional.of(
//                            roleRepository.save(Role
//                                    .builder()
//                                    .name(Role.DEFAULT_ROLE)
//                                            .authorities(Set.of(authority.isEmpty() ? null : authority.get()))
//                                    .build()
//                            )
//                    );
//                }
                //check user exist or not
                //if not do create
                //else do nothing
                Optional<User> userOptional = userRepository.findByUsername(authentication.getName());
                if(userOptional.isEmpty()){
//                    userOptional = Optional.of(
//                            userRepository.save(com.sbilhbank.com.kh.holdingbalance.entity.User
//                                    .builder()
//                                        .username(authentication.getName())
//                                        .roles(Set.of(role.isEmpty()?null:role.get()))
//                                        .accountExpired(false)
//                                        .accountLocked(false)
//                                        .credentialsExpired(false)
//                                        .enabled(true)
//                                    .build())
//                    );
                    throw new BadCredentialsException("User doesn't exist in this application!");
                }
//                if(userOptional.isEmpty()) throw new NotFoundEntityException("User doesn't exist in this application!");
                if (!userOptional.get().isEnabled()) throw new DisabledException("User is disabled.");
                if (userOptional.get().isAccountLocked()) throw new LockedException("User is locked.");
                if (userOptional.get().isCredentialsExpired()) throw new CredentialsExpiredException("Credentials Expired on the user.");
                if (userOptional.get().isAccountExpired()) throw new AccountExpiredException("User is expired.");
//                List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
//                grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
//                UserDetails userDetails = new User(authentication.getName() ,authentication.getCredentials().toString()
//                        ,grantedAuthorities);
                UserDetails userDetails = new SecuredUserDetails(userOptional.get());
                Authentication auth = new UsernamePasswordAuthenticationToken(userDetails.getUsername(),
                        authentication.getCredentials().toString() , userDetails.getAuthorities());
                return auth;

            } else {
                throw new BadCredentialsException("Bad credentials");
            }
        }catch (NamingException ex){
            System.err.println(ex.getMessage());
            throw new RuntimeException(ex.getMessage());
        }
    }

//    @Override
//    public boolean supports(Class<?> authentication) {
//        return authentication.equals(UsernamePasswordAuthenticationToken.class);
//    }
    @Override
    public boolean supports(Class<?> authentication){
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}

