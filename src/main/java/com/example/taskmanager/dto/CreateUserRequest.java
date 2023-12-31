package com.example.taskmanager.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateUserRequest(
        @NotBlank(message = "Username must not be blank")
        String username) {
}

