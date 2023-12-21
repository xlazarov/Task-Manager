package com.example.taskmanager.service;

import com.example.taskmanager.data.Task;
import com.example.taskmanager.data.TaskMapper;
import com.example.taskmanager.data.TaskRepository;
import com.example.taskmanager.data.TaskState;
import com.example.taskmanager.dto.CreateTaskRequest;
import com.example.taskmanager.dto.TaskResponse;
import com.example.taskmanager.dto.UpdateTaskRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public TaskResponse mapTaskToResponse(Task task) {
        return taskMapper.taskToTaskResponse(task);
    }

    public List<TaskResponse> mapTaskToResponse(List<Task> tasks) {
        return tasks.stream()
                .map(taskMapper::taskToTaskResponse)
                .collect(Collectors.toList());
    }

    public TaskResponse getTaskById(Integer taskId) {
        Task task = taskRepository.findById(taskId).orElseThrow(() ->
                new ResponseStatusException(NOT_FOUND, String.format("No task found for id (%s)", taskId)));
        return mapTaskToResponse(task);
    }

    public List<TaskResponse> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        return mapTaskToResponse(tasks);
    }

    public TaskResponse addTask(CreateTaskRequest request) {
        Task task = new Task();
        task.setDescription(request.description());
        task.setDueDate(request.dueDate());
        task.setAssignedUser(request.assignedUser());
        task.setState(request.state());
        taskRepository.save(task);
        return mapTaskToResponse(task);
    }

    public TaskResponse updateTask(Integer id, UpdateTaskRequest request) {
        Task task = taskRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(NOT_FOUND, String.format("No task found for id (%s)", id)));

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
        taskRepository.save(task);
        return mapTaskToResponse(task);
    }

    public void deleteTask(Integer taskId) {
        if (!taskRepository.existsById(taskId)) {
            throw new ResponseStatusException(NOT_FOUND, String.format("No task found for id (%s)", taskId));
        }
        taskRepository.deleteById(taskId);
    }

    public List<TaskResponse> getTasksByState(TaskState state) {
        List<Task> tasks = taskRepository.findByState(state);
        return mapTaskToResponse(tasks);
    }

    public List<TaskResponse> getTasksForUser(Integer userId) {
        if (!taskRepository.existsByAssignedUserId(userId)) {
            throw new ResponseStatusException(NOT_FOUND, String.format("No user found for id (%s)", userId));
        }
        List<Task> tasks = taskRepository.findByAssignedUserId(userId);
        return mapTaskToResponse(tasks);
    }

    public List<TaskResponse> getTasksByDueDate(LocalDate dueDate) {
        if (dueDate.isBefore(LocalDate.now())) {
            throw new ConstraintViolationException("Due date must be in the future", null);
        }
        List<Task> tasks = taskRepository.findByDueDate(dueDate);
        return mapTaskToResponse(tasks);
    }

}

