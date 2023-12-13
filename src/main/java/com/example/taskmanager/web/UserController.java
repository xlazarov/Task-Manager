package com.example.taskmanager.web;

import com.example.taskmanager.data.AppUser;
import com.example.taskmanager.dto.CreateUserRequest;
import com.example.taskmanager.dto.UpdateUserRequest;
import com.example.taskmanager.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public ResponseEntity<List<AppUser>> getAllUsers() {
        List<AppUser> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<AppUser> getUser(@PathVariable("userId") Integer userId) {
        AppUser user = userService.getUserById(userId);
        return (user != null) ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    @PostMapping("/")
    public ResponseEntity<AppUser> addUser(@RequestBody CreateUserRequest request) {
        AppUser createdUser = userService.addUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<AppUser> updateUser(@PathVariable("userId") Integer id,
                                              @RequestBody UpdateUserRequest request) {
        AppUser updatedUser = userService.updateUser(id, request);
        return (updatedUser != null) ? ResponseEntity.ok(updatedUser) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable("userId") Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("Task deleted successfully.");
    }
}
