package com.example.taskmanager.web;

import com.example.taskmanager.dto.CreateUserRequest;
import com.example.taskmanager.dto.UpdateUserRequest;
import com.example.taskmanager.dto.UserResponse;
import com.example.taskmanager.service.UserService;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
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

    /**
     * Retrieves all users.
     *
     * @return List of users and HTTP status OK.
     */
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        log.info("Endpoint /api/user called: getAllUsers");
        List<UserResponse> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * Retrieves a specific user by its ID.
     *
     * @param userId The ID of the user to retrieve.
     * @return User and HTTP status OK if found, or NOT_FOUND if not found.
     */
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Integer userId) {
        log.info("Endpoint /api/user called: getUser");
        UserResponse user = userService.getUserById(userId);
        return (user != null) ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    /**
     * Adds a new user.
     *
     * @param request       The request body containing user details.
     * @param bindingResult The result of the validation.
     * @return Created user and HTTP status CREATED if successful, or BAD_REQUEST if validation fails.
     * @throws ConstraintViolationException if there are validation errors.
     */
    @PostMapping("/")
    public ResponseEntity<UserResponse> addUser(@Valid @RequestBody CreateUserRequest request,
                                                BindingResult bindingResult) {
        log.info("Endpoint /api/user called: addUser");
        if (bindingResult.hasErrors()) {
            log.error("Validation error occurred while adding a user. Details: {}", bindingResult);
            throw new ConstraintViolationException(bindingResult.toString(), null);
        }
        UserResponse createdUser = userService.addUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    /**
     * Updates an existing user.
     *
     * @param userId        The ID of the user to update.
     * @param request       The request body containing updated user details.
     * @param bindingResult The result of the validation.
     * @return Updated user and HTTP status OK if successful, or NOT_FOUND if the task is not found.
     * @throws ConstraintViolationException if there are validation errors.
     */
    @PutMapping("/{userId}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Integer userId,
                                                   @Valid @RequestBody UpdateUserRequest request,
                                                   BindingResult bindingResult) {
        log.info("Endpoint /api/user called: updateUser");
        if (bindingResult.hasErrors()) {
            log.error("Validation error occurred while updating a user. Details: {}", bindingResult);
            throw new ConstraintViolationException(bindingResult.toString(), null);
        }
        UserResponse updatedUser = userService.updateUser(userId, request);
        return (updatedUser != null) ? ResponseEntity.ok(updatedUser) : ResponseEntity.notFound().build();
    }

    /**
     * Deletes a user by ID.
     *
     * @param userId The ID of the user to delete.
     * @return Success message and HTTP status OK.
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer userId) {
        log.info("Endpoint /api/user called: deleteUser");
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}
