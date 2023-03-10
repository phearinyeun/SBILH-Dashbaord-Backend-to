package com.sbilhbank.insur.entity.primary;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
//@Data
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class Role {
    public static final String DEFAULT_ROLE = "ROLE_USER";
    public static final String ADMIN_ROLE = "ROLE_ADMIN";
    public static final String CHECKER_ROLE = "ROLE_INSUR_CHECKER";
    public static final String MAKER_ROLE = "ROLE_INSUR_MAKER";
    public static final String INSUR_ROLE = "ROLE_INSUR";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "roles")
    private Set<User> users = new HashSet<>();
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "roles_authorities",
            joinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "authority_id", referencedColumnName = "id"))
    private Set<Authority> authorities = new HashSet<>();
}
