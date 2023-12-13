package com.example.taskmanager.service;

import com.example.taskmanager.data.AppUser;
import com.example.taskmanager.data.UserRepository;
import com.example.taskmanager.dto.CreateUserRequest;
import com.example.taskmanager.dto.UpdateUserRequest;

import java.util.List;
import java.util.Optional;

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<AppUser> getAllUsers() {
        return userRepository.findAll();
    }

    public AppUser getUserById(Integer userId) {
        Optional<AppUser> user = userRepository.findById(userId);
        return user.orElse(null);
    }

    public AppUser addUser(CreateUserRequest request) {
        AppUser user = new AppUser();
        user.setUsername(request.username());
        return userRepository.save(user);
    }

    public AppUser updateUser(Integer userId, UpdateUserRequest request){
        AppUser user = userRepository.getReferenceById(userId);
        user.setUsername(request.username());
        return userRepository.save(user);
    }

    public void deleteUser(Integer userId) {
        userRepository.deleteById(userId);
    }
}
