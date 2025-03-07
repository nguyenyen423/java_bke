package com.example.mobilestore.repository;

import com.example.mobilestore.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    public boolean existsByUsername(String username);
    Optional<User> findByUsername(String username);
}
