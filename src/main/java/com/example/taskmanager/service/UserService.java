package com.example.taskmanager.service;

import com.example.taskmanager.data.AppUser;
import com.example.taskmanager.data.UserMapper;
import com.example.taskmanager.data.UserRepository;
import com.example.taskmanager.dto.CreateUserRequest;
import com.example.taskmanager.dto.UpdateUserRequest;
import com.example.taskmanager.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * Service layer responsible for managing users.
 */
@RequiredArgsConstructor
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
        List<AppUser> users = userRepository.findAll();
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
        AppUser user = userRepository.findById(userId).orElseThrow(() ->
                new ResponseStatusException(NOT_FOUND, String.format("No user found for id (%s)", userId)));
        return mapUserToResponse(user);
    }

    /**
     * Adds a new user entity.
     *
     * @param request The request containing user details.
     * @return The newly created user.
     */
    public UserResponse addUser(CreateUserRequest request) {
        AppUser user = new AppUser();
        user.setUsername(request.username());
        userRepository.save(user);
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
        AppUser user = userRepository.findById(userId).orElseThrow(() ->
                new ResponseStatusException(NOT_FOUND, String.format("No user found for id (%s)", userId)));

        user.setUsername(request.username());
        userRepository.save(user);
        return mapUserToResponse(user);
    }

    /**
     * Deletes a user by its unique identifier.
     *
     * @param userId The unique identifier of the user to delete.
     * @throws ResponseStatusException If no user is found for the given identifier.
     */
    public void deleteUser(Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResponseStatusException(NOT_FOUND, String.format("No task found for id (%s)", userId));
        }
        userRepository.deleteById(userId);
    }
}
