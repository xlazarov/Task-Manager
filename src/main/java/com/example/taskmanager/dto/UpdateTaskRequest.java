package com.example.taskmanager.dto;

import com.example.taskmanager.data.AppUser;
import com.example.taskmanager.validation.ExistsInDb;
import com.example.taskmanager.validation.ValidateTaskState;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

public record UpdateTaskRequest(
        @Pattern(regexp = "^(?!\\s*$).+", message = "Description must not be blank")
        String description,
        @Future(message = "Due date must be in the future")
        LocalDate dueDate,
        @Valid
        @ExistsInDb
        AppUser assignedUser,
        @ValidateTaskState
        String state) {
}
