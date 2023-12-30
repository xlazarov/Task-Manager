package com.example.taskmanager.web;

import com.example.taskmanager.data.Task;
import com.example.taskmanager.data.TaskMapper;
import com.example.taskmanager.dto.CreateTaskRequest;
import com.example.taskmanager.dto.TaskResponse;
import com.example.taskmanager.dto.UpdateTaskRequest;
import com.example.taskmanager.service.TaskService;
import com.example.taskmanager.validation.ValidateTaskState;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Controller layer for handling HTTP requests related to task management.
 */
@RestController
@RequestMapping("/api/task")
@RequiredArgsConstructor
@Validated
public class TaskController {

    private final TaskService taskService;
    private final TaskMapper taskMapper;

    /**
     * Maps a single Task entity to a TaskResponse DTO.
     *
     * @param task The Task entity to map.
     * @return The corresponding TaskResponse DTO.
     */
    public TaskResponse mapTaskToResponse(Task task) {
        return taskMapper.taskToTaskResponse(task);
    }

    /**
     * Maps a list of Task entities to a list of TaskResponse DTOs.
     *
     * @param tasks The list of Task entities to map.
     * @return The corresponding list of TaskResponse DTOs.
     */
    public List<TaskResponse> mapTaskToResponse(List<Task> tasks) {
        return tasks.stream()
                .map(taskMapper::taskToTaskResponse)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all tasks.
     *
     * @return List of tasks and HTTP status OK.
     */
    @Operation(summary = "Get all tasks", responses = {
            @ApiResponse(responseCode = "200", description = "List of tasks")
    })
    @GetMapping
    public ResponseEntity<List<TaskResponse>> getAllTasks() {
        List<Task> tasks = taskService.getAllTasks();
        List<TaskResponse> responses = mapTaskToResponse(tasks);
        return ResponseEntity.ok(responses);
    }

    /**
     * Retrieves a specific task by its ID.
     *
     * @param taskId The ID of the task to retrieve.
     * @return Task and HTTP status OK if found, or NOT_FOUND if not found.
     */
    @Operation(summary = "Get a task by ID", responses = {
            @ApiResponse(responseCode = "200", description = "Task found"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @GetMapping("/{taskId}")
    public ResponseEntity<TaskResponse> getTask(@PathVariable Integer taskId) {
        Optional<Task> task = taskService.getTaskById(taskId);
        if (task.isPresent()) {
            TaskResponse response = mapTaskToResponse(task.get());
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Adds a new task.
     *
     * @param request The request body containing task details.
     * @return Created task and HTTP status CREATED if successful, or BAD_REQUEST if validation fails.
     */
    @Operation(summary = "Add a new task", responses = {
            @ApiResponse(responseCode = "201", description = "Task created"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PostMapping
    public ResponseEntity<TaskResponse> addTask(@Valid @RequestBody CreateTaskRequest request) {
        Task createdTask = taskService.addTask(request);
        TaskResponse response = mapTaskToResponse(createdTask);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Updates an existing task.
     *
     * @param taskId  The ID of the task to update.
     * @param request The request body containing updated task details.
     * @return Appropriate HTTP status.
     */
    @Operation(summary = "Update an existing task", responses = {
            @ApiResponse(responseCode = "204", description = "Task updated"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @PutMapping("/{taskId}")
    public ResponseEntity<Void> updateTask(@PathVariable Integer taskId,
                                                   @Valid @RequestBody UpdateTaskRequest request) {
        Optional<Task> updatedTask = taskService.updateTask(taskId, request);
        if (updatedTask.isPresent()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Deletes a task by ID.
     *
     * @param taskId The ID of the task to delete.
     * @return No Content success HTTP status.
     */
    @Operation(summary = "Delete a task by ID", responses = {
            @ApiResponse(responseCode = "204", description = "Task deleted")
    })
    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Integer taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Retrieves tasks assigned to a user.
     *
     * @param userId The ID of the user.
     * @return List of tasks and HTTP status OK if user exists, NOT_FOUND otherwise.
     */
    @Operation(summary = "Get tasks by assigned user ID", responses = {
            @ApiResponse(responseCode = "200", description = "List of tasks by assigned user"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TaskResponse>> getTasksForUser(@PathVariable Integer userId) {
        Optional<List<Task>> userTasks = taskService.getTasksForUser(userId);
        if (userTasks.isPresent()) {
            List<TaskResponse> responses = mapTaskToResponse(userTasks.get());
            return ResponseEntity.ok(responses);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Retrieves tasks based on their state.
     *
     * @param state The state of the tasks to retrieve.
     * @return List of tasks and HTTP status OK.
     */
    @Operation(summary = "Get tasks by state", responses = {
            @ApiResponse(responseCode = "200", description = "List of tasks by state")
    })
    @GetMapping("/state/{state}")
    public ResponseEntity<List<TaskResponse>> getTasksByState(@PathVariable @ValidateTaskState String state) {
        List<Task> tasksByState = taskService.getTasksByState(state);
        List<TaskResponse> responses = mapTaskToResponse(tasksByState);
        return ResponseEntity.ok(responses);
    }

    /**
     * Retrieves tasks based on their due date.
     *
     * @param dueDate The due date for tasks to retrieve (must be in the future).
     * @return List of tasks and HTTP status OK.
     */
    @Operation(summary = "Get tasks by due date", responses = {
            @ApiResponse(responseCode = "200", description = "List of tasks by due date")
    })
    @GetMapping("/date/{dueDate}")
    public ResponseEntity<List<TaskResponse>> getTasksByDueDate(@PathVariable @Future LocalDate dueDate) {
        List<Task> tasksByDueDate = taskService.getTasksByDueDate(dueDate);
        List<TaskResponse> responses = mapTaskToResponse(tasksByDueDate);
        return ResponseEntity.ok(responses);
    }

}
