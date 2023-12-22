package com.example.taskmanager.dto;

import com.example.taskmanager.data.AppUser;
import com.example.taskmanager.data.TaskState;
import com.example.taskmanager.validation.ExistsInDb;
import com.example.taskmanager.validation.ValidateTaskState;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CreateTaskRequest(
        @NotNull(message = "Description must not be null")
        @NotBlank(message = "Description must not be blank")
        String description,
        @Future(message = "Due date must be in the future")
        LocalDate dueDate,
        @Valid
        @ExistsInDb
        AppUser assignedUser,
        @ValidateTaskState(
                enumClass = TaskState.class,
                message = "Must be TODO, IN_PROGRESS or COMPLETED")
        String state) {
    public CreateTaskRequest {
        state = (state != null) ? state : "TODO";
    }
}