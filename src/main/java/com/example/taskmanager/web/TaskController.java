package com.example.taskmanager.web;

import com.example.taskmanager.data.Task;
import com.example.taskmanager.data.TaskState;
import com.example.taskmanager.dto.CreateTaskRequest;
import com.example.taskmanager.dto.UpdateTaskRequest;
import com.example.taskmanager.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/task")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/")
    public ResponseEntity<List<Task>> getAllTasks() {
        List<Task> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<Task> getTask(@PathVariable Integer taskId) {
        Task task = taskService.getTaskById(taskId);
        return (task != null) ? ResponseEntity.ok(task) : ResponseEntity.notFound().build();
    }

    @PostMapping("/")
    public ResponseEntity<Task> addTask(@RequestBody CreateTaskRequest request) {
        Task createdTask = taskService.addTask(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<Task> updateTask(@PathVariable Integer taskId,
                                           @RequestBody UpdateTaskRequest request) {
        Task updatedTask = taskService.updateTask(taskId, request);
        return (updatedTask != null) ? ResponseEntity.ok(updatedTask) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<?> deleteTask(@PathVariable Integer taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.ok("Task deleted successfully.");
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Task>> getTasksForUser(@PathVariable Integer userId) {
        List<Task> userTasks = taskService.getTasksForUser(userId);
        return ResponseEntity.ok(userTasks);
    }

    @GetMapping("/state/{state}")
    public ResponseEntity<List<Task>> getTasksByState(@PathVariable TaskState state) {
        List<Task> tasksByState = taskService.getTasksByState(state);
        return ResponseEntity.ok(tasksByState);
    }

    @GetMapping("/date/{dueDate}")
    public ResponseEntity<List<Task>> getTasksByDueDate(@PathVariable LocalDate dueDate) {
        List<Task> tasksByDueDate = taskService.getTasksByDueDate(dueDate);
        return ResponseEntity.ok(tasksByDueDate);
    }

}
