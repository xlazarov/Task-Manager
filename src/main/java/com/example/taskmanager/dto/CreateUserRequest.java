package com.example.taskmanager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateUserRequest(
        @NotNull(message = "Username must not be null")
        @NotBlank(message = "Username must not be blank")
        String username) {
}

