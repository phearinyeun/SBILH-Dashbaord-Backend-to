package com.sbilhbank.insur.entity.primary;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
@Data
@SuperBuilder
@NoArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    @OneToOne(mappedBy = "user")
    private Token token;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;
    private String password;
    private boolean accountExpired;
    private boolean accountLocked;
    private boolean credentialsExpired;
    private boolean enabled;
    private boolean ad;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (roles == null)
            return null;
        List<GrantedAuthority> role_grant = roles.stream().map(s -> new SimpleGrantedAuthority(s.getName())).collect(Collectors.toList());
        List<GrantedAuthority> authorities = new ArrayList<>();
        roles.forEach(s -> {
            s.getAuthorities().forEach(item -> {
                authorities.add(new SimpleGrantedAuthority(item.getName()));
            });
        });
        role_grant.addAll(authorities);
        return role_grant;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !accountExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !accountLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !credentialsExpired;
    }
}
