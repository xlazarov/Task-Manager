package com.example.taskmanager.dto;

import com.example.taskmanager.data.AppUser;
import com.example.taskmanager.data.TaskState;

import java.time.LocalDate;

public record TaskResponse(Integer id, String description, LocalDate dueDate, AppUser assignedUser, String state) {
}
