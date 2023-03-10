package com.sbilhbank.insur.service.user.security;

import com.sbilhbank.insur.entity.primary.Role;
import com.sbilhbank.insur.entity.primary.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SecuredUserDetails implements UserDetails {

    private final User user;

    public SecuredUserDetails(User user) {
        this.user = user;
    }

    /**
     * Returns the authorities granted to the user. Cannot return <code>null</code>.
     *
     * @return the authorities, sorted by natural key (never <code>null</code>)
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<SimpleGrantedAuthority> authorities =
                Optional.ofNullable(user)
                .map(User::getRoles).stream()
                .flatMap(Collection::stream)
                .map(Role::getAuthorities)
                .flatMap(Collection::stream)
                .map(authority -> new SimpleGrantedAuthority(authority.getName()))
                .distinct()
                .collect(Collectors.toList());

        Optional.ofNullable(user)
                .map(User::getRoles).stream()
                .flatMap(Collection::stream)
                .map(r -> {
                    List<SimpleGrantedAuthority> authorities1 = new ArrayList<>();
                    authorities1.add(new SimpleGrantedAuthority(r.getName()));
                    return authorities1;
                })
                .distinct()
                .collect(Collectors.toList())
                        .forEach(s -> {
                            s.forEach(authorities::add);
                        });
        return authorities;
    }


    /**
     * Returns the password used to authenticate the user.
     *
     * @return the password
     */
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * Returns the username used to authenticate the user. Cannot return
     * <code>null</code>.
     *
     * @return the username (never <code>null</code>)
     */
    @Override
    public String getUsername() {
        return user.getUsername();
    }

    /**
     * Indicates whether the user's account has expired. An expired account cannot be
     * authenticated.
     *
     * @return <code>true</code> if the user's account is valid (ie non-expired),
     * <code>false</code> if no longer valid (ie expired)
     */
    @Override
    public boolean isAccountNonExpired() {
        return !user.isAccountExpired();
    }

    /**
     * Indicates whether the user is locked or unlocked. A locked user cannot be
     * authenticated.
     *
     * @return <code>true</code> if the user is not locked, <code>false</code> otherwise
     */
    @Override
    public boolean isAccountNonLocked() {
        return !user.isAccountLocked();
    }

    /**
     * Indicates whether the user's credentials (password) has expired. Expired
     * credentials prevent authentication.
     *
     * @return <code>true</code> if the user's credentials are valid (ie non-expired),
     * <code>false</code> if no longer valid (ie expired)
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return !user.isCredentialsExpired();
    }

    /**
     * Indicates whether the user is enabled or disabled. A disabled user cannot be
     * authenticated.
     *
     * @return <code>true</code> if the user is enabled, <code>false</code> otherwise
     */
    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }
}
