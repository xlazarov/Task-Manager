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

@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserResponse mapUserToResponse(AppUser user) {
        return userMapper.userToUserResponse(user);
    }

    public List<UserResponse> getAllUsers() {
        List<AppUser> users = userRepository.findAll();
        return users.stream()
                .map(this::mapUserToResponse)
                .collect(Collectors.toList());
    }

    public UserResponse getUserById(Integer userId) {
        AppUser user = userRepository.findById(userId).orElseThrow(() ->
                new ResponseStatusException(NOT_FOUND, String.format("No user found for id (%s)", userId)));
        return mapUserToResponse(user);
    }

    public UserResponse addUser(CreateUserRequest request) {
        AppUser user = new AppUser();
        user.setUsername(request.username());
        userRepository.save(user);
        return mapUserToResponse(user);
    }

    public UserResponse updateUser(Integer userId, UpdateUserRequest request) {
        AppUser user = userRepository.findById(userId).orElseThrow(() ->
                new ResponseStatusException(NOT_FOUND, String.format("No user found for id (%s)", userId)));

        user.setUsername(request.username());
        userRepository.save(user);
        return mapUserToResponse(user);
    }

    public void deleteUser(Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResponseStatusException(NOT_FOUND, String.format("No task found for id (%s)", userId));
        }
        userRepository.deleteById(userId);
    }
}
