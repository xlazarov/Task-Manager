package com.example.taskmanager.service;

import com.example.taskmanager.data.AppUser;
import com.example.taskmanager.data.UserMapper;
import com.example.taskmanager.data.UserRepository;
import com.example.taskmanager.dto.CreateUserRequest;
import com.example.taskmanager.dto.UpdateUserRequest;
import com.example.taskmanager.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * Service layer responsible for managing users.
 */
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
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
     * Retrieves all users in the system.
     *
     * @return List of all users.
     */
    public List<UserResponse> getAllUsers() {
        log.info("Fetching all users");
        List<AppUser> users = userRepository.findAll();
        log.info("Fetched {} users", users.size());
        return users.stream()
                .map(this::mapUserToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a user by its unique identifier.
     *
     * @param userId The unique identifier of the user.
     * @return The user with the specified identifier.
     * @throws ResponseStatusException If no user is found for the given identifier.
     */
    public UserResponse getUserById(Integer userId) {
        log.info("Fetching user with id: {}", userId);
        try {
            AppUser user = userRepository.findById(userId).orElseThrow(() ->
                    new ResponseStatusException(NOT_FOUND, String.format("No user found for id (%s)", userId)));
            log.info("Fetched user: {}", user);
            return mapUserToResponse(user);
        } catch (Exception e) {
            log.error("Error fetching user with id {}: {}", userId, e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Adds a new user entity.
     *
     * @param request The request containing user details.
     * @return The newly created user.
     */
    public UserResponse addUser(CreateUserRequest request) {
        log.info("Adding a new user: {}", request);
        AppUser user = new AppUser();
        user.setUsername(request.username());
        userRepository.save(user);
        log.info("Added a new task: {}", user);
        return mapUserToResponse(user);
    }

    /**
     * Updates an existing user.
     *
     * @param userId  The unique identifier of the user to update.
     * @param request The request containing updated user details.
     * @return The updated user.
     * @throws ResponseStatusException If no user is found for the given identifier.
     */
    public UserResponse updateUser(Integer userId, UpdateUserRequest request) {
        log.info("Updating user with id {}: {}", userId, request);
        try {
            AppUser user = userRepository.findById(userId).orElseThrow(() ->
                    new ResponseStatusException(NOT_FOUND, String.format("No user found for id (%s)", userId)));

            user.setUsername(request.username());
            userRepository.save(user);
            return mapUserToResponse(user);
        } catch (Exception e) {
            log.error("Error updating user with id {}: {}", userId, e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Deletes a user by its unique identifier.
     *
     * @param userId The unique identifier of the user to delete.
     * @throws ResponseStatusException If no user is found for the given identifier.
     */
    public void deleteUser(Integer userId) {
        log.info("Deleting user with id: {}", userId);
        try {
            if (!userRepository.existsById(userId)) {
                throw new ResponseStatusException(NOT_FOUND, String.format("No task found for id (%s)", userId));
            }
            userRepository.deleteById(userId);
            log.info("Deleted user with id: {}", userId);
        } catch (Exception e) {
            log.error("Error deleting user with id {}: {}", userId, e.getMessage(), e);
            throw e;
        }
    }
}
