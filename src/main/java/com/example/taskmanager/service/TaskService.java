package com.example.taskmanager.service;

import com.example.taskmanager.data.Task;
import com.example.taskmanager.data.TaskMapper;
import com.example.taskmanager.data.TaskRepository;
import com.example.taskmanager.dto.CreateTaskRequest;
import com.example.taskmanager.dto.TaskResponse;
import com.example.taskmanager.dto.UpdateTaskRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * Service layer responsible for managing tasks.
 */
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
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
     * Retrieves a task by its unique identifier.
     *
     * @param taskId The unique identifier of the task.
     * @return The task with the specified identifier.
     * @throws ResponseStatusException If no task is found for the given identifier.
     */
    public TaskResponse getTaskById(Integer taskId) {
        Task task = taskRepository.findById(taskId).orElseThrow(() ->
                new ResponseStatusException(NOT_FOUND, String.format("No task found for id (%s)", taskId)));
        return mapTaskToResponse(task);
    }


    /**
     * Retrieves all tasks in the system.
     *
     * @return List of all tasks.
     */
    public List<TaskResponse> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        return mapTaskToResponse(tasks);
    }

    /**
     * Adds a new task to the system.
     *
     * @param request The request containing task details.
     * @return The newly created task.
     */
    public TaskResponse addTask(CreateTaskRequest request) {
        Task task = new Task();
        task.setDescription(request.description());
        task.setDueDate(request.dueDate());
        task.setAssignedUser(request.assignedUser());
        task.setState(request.state());
        taskRepository.save(task);
        return mapTaskToResponse(task);
    }

    /**
     * Updates an existing task in the system.
     *
     * @param id      The unique identifier of the task to update.
     * @param request The request containing updated task details.
     * @return The updated task.
     * @throws ResponseStatusException If no task is found for the given identifier.
     */
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

    /**
     * Deletes a task by its unique identifier.
     *
     * @param taskId The unique identifier of the task to delete.
     * @throws ResponseStatusException If no task is found for the given identifier.
     */
    public void deleteTask(Integer taskId) {
        if (!taskRepository.existsById(taskId)) {
            throw new ResponseStatusException(NOT_FOUND, String.format("No task found for id (%s)", taskId));
        }
        taskRepository.deleteById(taskId);
    }

    /**
     * Retrieves tasks with a specific state.
     *
     * @param state The state to filter tasks.
     * @return List of tasks with the specified state.
     */
    public List<TaskResponse> getTasksByState(String state) {
        List<Task> tasks = taskRepository.findByState(state);
        return mapTaskToResponse(tasks);
    }

    /**
     * Retrieves tasks with a specific user assigned.
     *
     * @param userId The users unique identifier to filter tasks.
     * @return List of tasks with the specified user.
     */
    public List<TaskResponse> getTasksForUser(Integer userId) {
        if (!taskRepository.existsByAssignedUserId(userId)) {
            throw new ResponseStatusException(NOT_FOUND, String.format("No user found for id (%s)", userId));
        }
        List<Task> tasks = taskRepository.findByAssignedUserId(userId);
        return mapTaskToResponse(tasks);
    }

    /**
     * Retrieves tasks with a specific due date.
     *
     * @param dueDate The date to filter tasks.
     * @return List of tasks with the specified due date.
     */
    public List<TaskResponse> getTasksByDueDate(LocalDate dueDate) {
        List<Task> tasks = taskRepository.findByDueDate(dueDate);
        return mapTaskToResponse(tasks);
    }
}
