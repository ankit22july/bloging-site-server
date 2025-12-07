package com.example.bloging.security;


public class CustomPrincipal {
    
    // Fields to hold the essential user information
    private final String role;
    private final Long id;

    /**
     * Constructor to initialize the principal with username and user ID.
     * @param username The subject (sub) from the JWT, typically the user's email or login name.
     * @param userId The custom 'userId' claim extracted from the JWT.
     */
    public CustomPrincipal(String role, Long id) {
        this.role = role;
        this.id = id;
    }

    // --- Getters ---

    /**
     * Retrieves the username (typically the 'sub' claim).
     * @return The username.
     */
    public String getRole() {
        return role;
    }

    /**
     * Retrieves the custom user ID.
     * @return The user ID (Long).
     */
    public Long getId() {
        return id;
    }
    
    // Optional: You may want to override toString() for easier logging
    @Override
    public String toString() {
        return "CustomPrincipal{" +
               "role='" + role + '\'' +
               ", id=" + id +
               '}';
    }
}