package com.example.taskmanager.dto;

import com.example.taskmanager.data.AppUser;
import com.example.taskmanager.data.TaskState;

import java.time.LocalDate;

public record UpdateTaskRequest(String description, LocalDate dueDate, AppUser assignedUser, TaskState state) {
}
