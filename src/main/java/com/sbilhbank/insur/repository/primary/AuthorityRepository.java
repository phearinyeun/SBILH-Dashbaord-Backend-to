package com.sbilhbank.insur.repository.primary;

import com.sbilhbank.insur.entity.primary.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorityRepository extends JpaRepository<Authority,Long> {
    Optional<Authority> findByName(String name);
}
