package com.example.taskmanager.dto;

import com.example.taskmanager.data.AppUser;
import com.example.taskmanager.data.TaskState;
import com.example.taskmanager.validation.ExistsInDb;
import com.example.taskmanager.validation.ValidateTaskState;
import jakarta.validation.Valid;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record CreateTaskRequest(
        @NotBlank(message = "Description must not be blank")
        String description,
        @FutureOrPresent(message = "Due date must be in the future")
        LocalDate dueDate,
        @Valid
        @ExistsInDb
        AppUser assignedUser,
        @ValidateTaskState
        TaskState state) {
    public CreateTaskRequest {
        state = (state != null) ? state : TaskState.TODO;
    }
}