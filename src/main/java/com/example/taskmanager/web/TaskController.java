package com.example.taskmanager.web;

import com.example.taskmanager.data.TaskState;
import com.example.taskmanager.dto.CreateTaskRequest;
import com.example.taskmanager.dto.TaskResponse;
import com.example.taskmanager.dto.UpdateTaskRequest;
import com.example.taskmanager.service.TaskService;
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

@RestController
@RequestMapping("/api/task")
@RequiredArgsConstructor
@Validated
public class TaskController {
    private final TaskService taskService;

    @GetMapping("/")
    public ResponseEntity<List<TaskResponse>> getAllTasks() {
        List<TaskResponse> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskResponse> getTask(@PathVariable Integer taskId) {
        TaskResponse task = taskService.getTaskById(taskId);
        return (task != null) ? ResponseEntity.ok(task) : ResponseEntity.notFound().build();
    }

    @PostMapping("/")
    public ResponseEntity<TaskResponse> addTask(@RequestBody CreateTaskRequest request) {
        TaskResponse createdTask = taskService.addTask(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable Integer taskId,
                                           @RequestBody UpdateTaskRequest request) {
        TaskResponse updatedTask = taskService.updateTask(taskId, request);
        return (updatedTask != null) ? ResponseEntity.ok(updatedTask) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<?> deleteTask(@PathVariable Integer taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.ok("Task deleted successfully.");
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TaskResponse>> getTasksForUser(@PathVariable Integer userId) {
        List<TaskResponse> userTasks = taskService.getTasksForUser(userId);
        return ResponseEntity.ok(userTasks);
    }

    @GetMapping("/state/{state}")
    public ResponseEntity<List<TaskResponse>> getTasksByState(@PathVariable TaskState state) {
        List<TaskResponse> tasksByState = taskService.getTasksByState(state);
        return ResponseEntity.ok(tasksByState);
    }

    @GetMapping("/date/{dueDate}")
    public ResponseEntity<List<TaskResponse>> getTasksByDueDate(@PathVariable LocalDate dueDate) {
        List<TaskResponse> tasksByDueDate = taskService.getTasksByDueDate(dueDate);
        return ResponseEntity.ok(tasksByDueDate);
    }

}
