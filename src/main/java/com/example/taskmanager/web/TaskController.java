package com.example.taskmanager.web;

import com.example.taskmanager.dto.CreateTaskRequest;
import com.example.taskmanager.dto.TaskResponse;
import com.example.taskmanager.dto.UpdateTaskRequest;
import com.example.taskmanager.service.TaskService;
import com.example.taskmanager.validation.ValidateTaskState;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
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

/**
 * Controller layer for handling HTTP requests related to task management.
 */
@RestController
@RequestMapping("/api/task")
@RequiredArgsConstructor
@Validated
@Slf4j
public class TaskController {
    private final TaskService taskService;

    /**
     * Retrieves all tasks.
     *
     * @return List of tasks and HTTP status OK.
     */
    @GetMapping
    public ResponseEntity<List<TaskResponse>> getAllTasks() {
        log.info("Endpoint /api/task called: getAllTasks");
        List<TaskResponse> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(tasks);
    }

    /**
     * Retrieves a specific task by its ID.
     *
     * @param taskId The ID of the task to retrieve.
     * @return Task and HTTP status OK if found, or NOT_FOUND if not found.
     */
    @GetMapping("/{taskId}")
    public ResponseEntity<TaskResponse> getTask(@PathVariable Integer taskId) {
        log.info("Endpoint /api/task called: getAllTasks");
        TaskResponse task = taskService.getTaskById(taskId);
        return (task != null) ? ResponseEntity.ok(task) : ResponseEntity.notFound().build();
    }

    /**
     * Adds a new task.
     * @param request The request body containing task details.
     * @param bindingResult The result of the validation.
     * @return Created task and HTTP status CREATED if successful, or BAD_REQUEST if validation fails.
     * @throws ConstraintViolationException if there are validation errors.
     */
    @PostMapping
    public ResponseEntity<TaskResponse> addTask(@Valid @RequestBody CreateTaskRequest request,
                                                BindingResult bindingResult) {
        log.info("Endpoint /api/task called: addTask");
        if (bindingResult.hasErrors()) {
            log.error("Validation error occurred while adding a task. Details: {}", bindingResult);
            throw new ConstraintViolationException(bindingResult.toString(), null);
        }
        TaskResponse createdTask = taskService.addTask(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }

    /**
     * Updates an existing task.
     * @param taskId The ID of the task to update.
     * @param request The request body containing updated task details.
     * @param bindingResult The result of the validation.
     * @return Updated task and HTTP status OK if successful, or NOT_FOUND if the task is not found.
     * @throws ConstraintViolationException if there are validation errors.
     */
    @PutMapping("/{taskId}")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable Integer taskId,
                                                   @Valid @RequestBody UpdateTaskRequest request,
                                                   BindingResult bindingResult) {
        log.info("Endpoint /api/task called: updateTask");
        if (bindingResult.hasErrors()) {
            log.error("Validation error occurred while updating a task. Details: {}", bindingResult);
            throw new ConstraintViolationException(bindingResult.toString(), null);
        }
        TaskResponse updatedTask = taskService.updateTask(taskId, request);
        return (updatedTask != null) ? ResponseEntity.ok(updatedTask) : ResponseEntity.notFound().build();
    }

    /**
     * Deletes a task by ID.
     * @param taskId The ID of the task to delete.
     * @return Success message and HTTP status OK.
     */
    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Integer taskId) {
        log.info("Endpoint /api/task called: deleteTask");
        taskService.deleteTask(taskId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Retrieves tasks assigned to a user.
     * @param userId The ID of the user.
     * @return List of tasks and HTTP status OK.
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TaskResponse>> getTasksForUser(@PathVariable Integer userId) {
        log.info("Endpoint /api/task/user called: getTasksForUser");
        List<TaskResponse> userTasks = taskService.getTasksForUser(userId);
        return ResponseEntity.ok(userTasks);
    }

    /**
     * Retrieves tasks based on their state.
     * @param state The state of the tasks to retrieve.
     * @return List of tasks and HTTP status OK.
     */
    @GetMapping("/state/{state}")
    public ResponseEntity<List<TaskResponse>> getTasksByState(@PathVariable @ValidateTaskState String state) {
        log.info("Endpoint /api/task/state called: getTasksByState");
        List<TaskResponse> tasksByState = taskService.getTasksByState(state);
        return ResponseEntity.ok(tasksByState);
    }

    /**
     * Retrieves tasks based on their due date.
     * @param dueDate The due date for tasks to retrieve (must be in the future).
     * @return List of tasks and HTTP status OK.
     */
    @GetMapping("/date/{dueDate}")
    public ResponseEntity<List<TaskResponse>> getTasksByDueDate(@PathVariable @FutureOrPresent LocalDate dueDate) {
        log.info("Endpoint /api/task/date called: getTasksByDueDate");
        List<TaskResponse> tasksByDueDate = taskService.getTasksByDueDate(dueDate);
        return ResponseEntity.ok(tasksByDueDate);
    }

}
