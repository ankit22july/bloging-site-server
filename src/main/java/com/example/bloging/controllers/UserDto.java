package com.example.bloging.controllers;

import com.example.bloging.entities.Role;
import com.example.bloging.entities.User;

public class UserDto {
    private Long id;
    private String username;
    private String email;
    private Role role;

    public UserDto(Long id, String username, String email, Role role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
    }

    public static UserDto fromUser(User user) {
        return new UserDto(user.getId(), user.getUsername(), user.getEmail(), user.getRole());
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public Role getRole() {
        return role;
    }
}
