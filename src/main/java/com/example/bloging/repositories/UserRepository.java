package com.example.bloging.repositories;

import com.example.bloging.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Finds a user by their username. Returns an Optional to handle cases where the user is not found.
    Optional<User> findByUsername(String username);

    // Finds a user by their email. Returns an Optional to handle cases where the user is not found.
    Optional<User> findByEmail(String email);

    
}