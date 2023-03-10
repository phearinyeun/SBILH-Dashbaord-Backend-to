package com.sbilhbank.insur.entity.primary;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "authorities")
@Data
@SuperBuilder
@NoArgsConstructor
public class Authority {
    public static final String DEFAULT_AUTHORITY = "FULL_INSUR";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "authorities")
    private Set<Role> roles;
}
