package com.sbilhbank.insur.repository.primary;

import com.sbilhbank.insur.entity.primary.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    @Query("select distinct u from User u " +
            "where (:username is null or u.username like :username) ")
    Page<User> findAllByUsername(@Param("username") String username, Pageable pageable);
    Page<User> findAllByUsernameLike(String username, Pageable pageable);
}
