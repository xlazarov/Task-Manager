package com.example.taskmanager.web;

import com.example.taskmanager.data.AppUser;
import com.example.taskmanager.data.UserMapper;
import com.example.taskmanager.dto.CreateUserRequest;
import com.example.taskmanager.dto.UpdateUserRequest;
import com.example.taskmanager.dto.UserResponse;
import com.example.taskmanager.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Controller layer for handling HTTP requests related to user management.
 */
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Validated
@Slf4j
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;


    /**
     * Maps a single User entity to a UserResponse DTO.
     *
     * @param user The User entity to map.
     * @return The corresponding UserResponse DTO.
     */
    public UserResponse mapUserToResponse(AppUser user) {
        return userMapper.userToUserResponse(user);
    }

    /**
     * Maps a list of User entities to a list of UserResponse DTOs.
     *
     * @param users The list of User entities to map.
     * @return The corresponding list of UserResponse DTOs.
     */
    public List<UserResponse> mapUserToResponse(List<AppUser> users) {
        return users.stream()
                .map(this::mapUserToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all users.
     *
     * @return List of users and HTTP status OK.
     */
    @Operation(summary = "Get all users", responses = {
            @ApiResponse(responseCode = "200", description = "List of users")
    })
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        log.info("Endpoint /api/user called: getAllUsers");
        List<AppUser> users = userService.getAllUsers();
        List<UserResponse> userResponses = mapUserToResponse(users);
        return ResponseEntity.ok(userResponses);
    }

    /**
     * Retrieves a specific user by its ID.
     *
     * @param userId The ID of the user to retrieve.
     * @return User and HTTP status OK if found, or NOT_FOUND if not found.
     */
    @Operation(summary = "Get a user by ID", responses = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Integer userId) {
        log.info("Endpoint /api/user called: getUser");
        Optional<AppUser> user = userService.getUserById(userId);
        if (user.isPresent()) {
            UserResponse userResponse = mapUserToResponse(user.get());
            return ResponseEntity.ok(userResponse);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Adds a new user.
     *
     * @param request The request body containing user details.
     * @return Created user and HTTP status CREATED if successful, or BAD_REQUEST if validation fails.
     */
    @Operation(summary = "Add a new user", responses = {
            @ApiResponse(responseCode = "201", description = "User created"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PostMapping
    public ResponseEntity<UserResponse> addUser(@Valid @RequestBody CreateUserRequest request) {
        log.info("Endpoint /api/user called: addUser");
        AppUser createdUser = userService.addUser(request);
        UserResponse userResponse = mapUserToResponse(createdUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }

    /**
     * Updates an existing user.
     *
     * @param userId  The ID of the user to update.
     * @param request The request body containing updated user details.
     * @return Appropriate HTTP status.
     */
    @Operation(summary = "Update an existing user", responses = {
            @ApiResponse(responseCode = "204", description = "User updated"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PutMapping("/{userId}")
    public ResponseEntity<Void> updateUser(@PathVariable Integer userId,
                                                   @Valid @RequestBody UpdateUserRequest request) {
        log.info("Endpoint /api/user called: updateUser");
        Optional<AppUser> updatedUser = userService.updateUser(userId, request);
        if (updatedUser.isPresent()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Deletes a user by ID.
     *
     * @param userId The ID of the user to delete.
     * @return No Content success HTTP status.
     */
    @Operation(summary = "Delete a user by ID", responses = {
            @ApiResponse(responseCode = "204", description = "User deleted")
    })
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer userId) {
        log.info("Endpoint /api/user called: deleteUser");
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}
