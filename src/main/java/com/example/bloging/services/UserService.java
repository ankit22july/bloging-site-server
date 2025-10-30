package com.example.bloging.services;

import com.example.bloging.entities.User; // import io.jsonwebtoken.security.Keys;Corrected import
import com.example.bloging.entities.Role;
import com.example.bloging.exceptions.InvalidCredentialsException;
import com.example.bloging.exceptions.UserAlreadyExistsException;
import com.example.bloging.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.example.bloging.exceptions.ResourceNotFoundException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final String privateKey;
    private final String jwtSecretKey;

    @Value("${bloging.security.jwt.expiration-ms}")
    private int jwtExpirationMs;


    public UserService(
            UserRepository userRepository,
            @Value("${bloging.security.private-key}") String privateKey,
            @Value("${bloging.security.jwt.secret-key}") String jwtSecretKey) {
        this.userRepository = userRepository;
        this.privateKey = privateKey;
        this.jwtSecretKey = jwtSecretKey;
    }

    public User signUp(String username, String email, String plainPassword) {
        // 1. Check if a user with the given username or email already exists
        if (userRepository.findByUsername(username).isPresent()) {
            throw new UserAlreadyExistsException("Username '" + username + "' is already taken.");
        }
        if (userRepository.findByEmail(email).isPresent()) {
            throw new UserAlreadyExistsException("Email '" + email + "' is already registered.");
        }

        // Create a new user entity
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setRole(Role.ROLE_USER);

        // Hash the password using MD5 and the private key (salt)
        String hashedPassword = hashPasswordWithMD5(plainPassword);
        newUser.setPasswordHash(hashedPassword);

        // Save the user to the database
        return userRepository.save(newUser);
    }

    public String login(String usernameOrEmail, String plainPassword) {
        // 1. Find the user by their username or email
        Optional<User> userOptional;
        if (usernameOrEmail.contains("@")) {
            userOptional = userRepository.findByEmail(usernameOrEmail);
        } else {
            userOptional = userRepository.findByUsername(usernameOrEmail);
        }

        User user = userOptional.orElseThrow(() -> new InvalidCredentialsException("Invalid username/email or password."));

        // 2. Hash the provided password using the same method as during sign-up
        String providedPasswordHash = hashPasswordWithMD5(plainPassword);

        // 3. Compare the hash of the provided password with the one stored in the database
        if (!providedPasswordHash.equals(user.getPasswordHash())) {
            throw new InvalidCredentialsException("Invalid username/email or password.");
        }

        // 4. If credentials are correct, generate and return a JWT token
        return generateJwtToken(user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
    }

    private String generateJwtToken(User user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .claim("role", user.getRole().name()) // Add role as a custom claim
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecretKey.getBytes(StandardCharsets.UTF_8))
                .compact();
    }

    public List<User> getAllUsers() {
    // Only admins should access this in future
    return userRepository.findAll();
}

    private String hashPasswordWithMD5(String password) {
        try {
            // WARNING: MD5 is not secure for password hashing. Use BCrypt in production.
            String saltedPassword = password + privateKey;
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(saltedPassword.getBytes(StandardCharsets.UTF_8));
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 algorithm not found", e);
        }
    }


    // For a secure alternative, you would add the spring-boot-starter-security dependency
    // and use BCryptPasswordEncoder:
    // private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    // String hashedPassword = passwordEncoder.encode(plainPassword);
}