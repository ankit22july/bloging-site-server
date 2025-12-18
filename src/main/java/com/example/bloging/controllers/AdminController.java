package com.example.bloging.controllers;

import com.example.bloging.entities.User;
import com.example.bloging.services.UserService;

import com.example.bloging.exceptions.ResourceNotFoundException;

import java.nio.file.AccessDeniedException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * Controller dedicated to administrative tasks, requiring elevated privileges.
 * Endpoint: /api/admin/users/{userId}
 */
@Tag(name = "Admin API", description = "Administrative operations (ADMIN only)")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/admin/users")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Endpoint to permanently delete a user account by their ID.
     * Requires ROLE_ADMIN.
     * 
     * @throws AccessDeniedException
     */
    @Operation(summary = "Delete user account", description = "Permanently delete a user account by ID (ADMIN only)")
    @ApiResponse(responseCode = "200", description = "User deleted successfully")
    @ApiResponse(responseCode = "404", description = "User not found")
    @ApiResponse(responseCode = "400", description = "Admin cannot delete own account")
    @DeleteMapping("/{userId}")

    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteUserAccount(@PathVariable Long userId) throws AccessDeniedException {

        // Get the currently authenticated Admin user performing the action
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        try {
            // Call the service method to perform hard deletion
            userService.deleteUserByAdmin(userId, currentUser);

            String successMessage = "User with ID " + userId + " deleted successfully.";
            return new ResponseEntity<>(successMessage, HttpStatus.OK);

        } catch (ResourceNotFoundException e) {
            // The user ID provided was not found
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 Not Found

        } catch (IllegalStateException e) {
            // Caught if the admin tries to delete their own account
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
    }

    @Operation(summary = "Set user role to MAINTAINER", description = "Update a user's role to MAINTAINER (ADMIN only)")
    @ApiResponse(responseCode = "200", description = "Role updated successfully")
    @ApiResponse(responseCode = "404", description = "User not found")
    @PutMapping("/{userId}/role/maintainer")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> setRoleToMaintainer(@PathVariable Long userId) {

        try {

            userService.updateUserRole(userId, "MAINTAINER");

            String successMessage = "User with ID " + userId + " role successfully updated to MAINTAINER.";
            return new ResponseEntity<>(successMessage, HttpStatus.OK);

        } catch (ResourceNotFoundException e) {

            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {

            return new ResponseEntity<>("Error setting role: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}