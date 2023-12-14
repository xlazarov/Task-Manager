package com.example.taskmanager.service;

import com.example.taskmanager.data.*;
import com.example.taskmanager.dto.CreateTaskRequest;
import com.example.taskmanager.dto.UpdateTaskRequest;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    public Task getTaskById(Integer taskId) {
        Optional<Task> task = taskRepository.findById(taskId);
        return task.orElse(null);
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task addTask(CreateTaskRequest request) {
        Task task = new Task();
        task.setDescription(request.description());
        task.setDueDate(request.dueDate());
        task.setAssignedUser(request.assignedUser());
        task.setState(request.state());
        return taskRepository.save(task);
    }

    public Task updateTask(Integer id, UpdateTaskRequest request) {
        Task task = taskRepository.getReferenceById(id);

        if (Objects.nonNull(request.description())) {
            task.setDescription(request.description());
        }
        if (Objects.nonNull(request.dueDate())) {
            task.setDueDate(request.dueDate());
        }
        if (Objects.nonNull(request.assignedUser())) {
            task.setAssignedUser(request.assignedUser());
        }
        if (Objects.nonNull(request.state())) {
            task.setState(request.state());
        }
        return taskRepository.save(task);
    }

    public void deleteTask(Integer taskId) {
        taskRepository.deleteById(taskId);
    }

    public List<Task> getTasksByState(TaskState state) {
        return taskRepository.findByState(state);
    }

    public List<Task> getTasksForUser(Integer userId) {
        return taskRepository.findByAssignedUserId(userId);
    }

    public List<Task> getTasksByDueDate(LocalDate dueDate) {
        return taskRepository.findByDueDate(dueDate);
    }

}

