package com.example.bloging.controllers;

import com.example.bloging.dto.UserDto;
import com.example.bloging.entities.User;
import com.example.bloging.services.UserService; // Corrected package name
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Schema;

@Tag(name = "User API", description = "User authentication and profile APIs")
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "User signup", description = "Register a new user")
    @PostMapping("/signup")
    public ResponseEntity<UserDto> signUp(@Valid @RequestBody SignUpRequest request) {
        User newUser = userService.signUp(request.getUsername(), request.getEmail(), request.getPassword());
        return new ResponseEntity<>(UserDto.fromUser(newUser), HttpStatus.CREATED);
    }

    @Operation(summary = "User login", description = "Login and get JWT token")
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginRequest request) {
        String token = userService.login(request.getUsername(), request.getPassword());
        return ResponseEntity.ok(new JwtResponse(token));
    }

    @Operation(summary = "Get user by username")
    @GetMapping("/{username}")
    public ResponseEntity<UserDto> getUserByUsername(@PathVariable String username) {
        User user = userService.findByUsername(username);
        return ResponseEntity.ok(UserDto.fromUser(user));
    }

    // Inner class for the request payload
    @Schema(description = "Signup request payload")
    public static class SignUpRequest {

        @NotBlank(message = "Username cannot be empty")
        @Pattern(regexp = "^[a-zA-Z0-9_.]+$", message = "Username can only contain letters, numbers, dots, and underscores")
        @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
        private String username;

        @NotBlank(message = "Email cannot be blank")
        @Email(message = "Email should be a valid email address")
        private String email;

        @NotBlank(message = "Password cannot be blank")
        @Pattern(regexp = "^(?=.*[A-Z])(?=.*[!@#$%^&*()]).{8,}$", message = "Password must be at least 8 characters long, with one uppercase letter and one special character.")
        private String password;

        // Getters and Setters
        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    // Inner class for the login request payload
    @Schema(description = "Login request payload")
    public static class LoginRequest {
        @NotBlank(message = "Username or email cannot be blank")
        private String username;

        @NotBlank(message = "Password cannot be blank")
        private String password;

        // Getters and Setters
        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    // Inner class for the JWT response payload
    @Schema(description = "JWT response payload")
    public static class JwtResponse {
        private String token;

        public JwtResponse(String token) {
            this.token = token;
        }

        public String getToken() {
            return token;
        }
    }
}