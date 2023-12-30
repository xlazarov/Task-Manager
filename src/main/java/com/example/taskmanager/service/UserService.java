package com.example.taskmanager.service;

import com.example.taskmanager.data.AppUser;
import com.example.taskmanager.data.UserRepository;
import com.example.taskmanager.dto.CreateUserRequest;
import com.example.taskmanager.dto.UpdateUserRequest;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;


/**
 * Service layer responsible for managing users.
 */
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;


    /**
     * Retrieves all users in the system.
     *
     * @return List of all users.
     */
    @Nonnull
    public List<AppUser> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Retrieves a user by its unique identifier.
     *
     * @param userId The unique identifier of the user.
     * @return The user with the specified identifier.
     */
    @Nonnull
    public Optional<AppUser> getUserById(@Nonnull Integer userId) {
        return userRepository.findById(userId);
    }

    /**
     * Adds a new user entity.
     *
     * @param request The request containing user details.
     * @return The newly created user.
     */
    @Nonnull
    public AppUser addUser(@Nonnull CreateUserRequest request) {
        AppUser user = new AppUser();
        user.setUsername(request.username());
        userRepository.save(user);
        return user;
    }

    /**
     * Updates an existing user.
     *
     * @param userId  The unique identifier of the user to update.
     * @param request The request containing updated user details.
     * @return The updated user.
     */
    @Nonnull
    public Optional<AppUser> updateUser(@Nonnull Integer userId, @Nonnull UpdateUserRequest request) {
        Optional<AppUser> user = userRepository.findById(userId);
        if (user.isPresent()) {
            user.get().setUsername(request.username());
            userRepository.save(user.get());
        }
        return user;
    }

    /**
     * Deletes a user by its unique identifier.
     *
     * @param userId The unique identifier of the user to delete.
     */
    public void deleteUser(@Nonnull Integer userId) {
        userRepository.deleteById(userId);
    }
}
