package com.example.taskmanager.service;

import com.example.taskmanager.data.Task;
import com.example.taskmanager.data.TaskMapper;
import com.example.taskmanager.data.TaskRepository;
import com.example.taskmanager.data.TaskState;
import com.example.taskmanager.dto.CreateTaskRequest;
import com.example.taskmanager.dto.TaskResponse;
import com.example.taskmanager.dto.UpdateTaskRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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
        log.info("Fetching task with id: {}", taskId);
        try {
            Task task = taskRepository.findById(taskId).orElseThrow(() ->
                    new ResponseStatusException(NOT_FOUND, String.format("No task found for id (%s)", taskId)));
            log.info("Fetched task: {}", task);
            return mapTaskToResponse(task);
        } catch (Exception e) {
            log.error("Error fetching task with id {}: {}", taskId, e.getMessage(), e);
            throw e;
        }
    }


    /**
     * Retrieves all tasks in the system.
     *
     * @return List of all tasks.
     */
    public List<TaskResponse> getAllTasks() {
        log.info("Fetching all tasks");
        List<Task> tasks = taskRepository.findAll();
        log.info("Fetched {} tasks", tasks.size());
        return mapTaskToResponse(tasks);
    }

    /**
     * Adds a new task to the system.
     *
     * @param request The request containing task details.
     * @return The newly created task.
     */
    public TaskResponse addTask(CreateTaskRequest request) {
        log.info("Adding a new task: {}", request);
        Task task = new Task();
        task.setDescription(request.description());
        task.setDueDate(request.dueDate());
        task.setAssignedUser(request.assignedUser());
        task.setState(request.state());
        taskRepository.save(task);
        log.info("Added a new task: {}", task);
        return mapTaskToResponse(task);
    }

    /**
     * Updates an existing task in the system.
     *
     * @param taskId      The unique identifier of the task to update.
     * @param request The request containing updated task details.
     * @return The updated task.
     * @throws ResponseStatusException If no task is found for the given identifier.
     */
    public TaskResponse updateTask(Integer taskId, UpdateTaskRequest request) {
        log.info("Updating task with id {}: {}", taskId, request);
        try {
            Task task = taskRepository.findById(taskId).orElseThrow(() ->
                    new ResponseStatusException(NOT_FOUND, String.format("No task found for id (%s)", taskId)));

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
        } catch (Exception e) {
            log.error("Error updating task with id {}: {}", taskId, e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Deletes a task by its unique identifier.
     *
     * @param taskId The unique identifier of the task to delete.
     * @throws ResponseStatusException If no task is found for the given identifier.
     */
    public void deleteTask(Integer taskId) {
        log.info("Deleting task with id: {}", taskId);
        try {
            if (!taskRepository.existsById(taskId)) {
                throw new ResponseStatusException(NOT_FOUND, String.format("No task found for id (%s)", taskId));
            }
            taskRepository.deleteById(taskId);
            log.info("Deleted task with id: {}", taskId);
        } catch (Exception e) {
            log.error("Error deleting task with id {}: {}", taskId, e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Retrieves tasks with a specific state.
     *
     * @param state The state to filter tasks.
     * @return List of tasks with the specified state.
     */
    public List<TaskResponse> getTasksByState(String state) {
        log.info("Fetching tasks by state: {}", state);
        List<Task> tasks = taskRepository.findByState(state);
        log.info("Fetched {} tasks by state: {}", tasks.size(), state);
        return mapTaskToResponse(tasks);
    }

    /**
     * Retrieves tasks with a specific user assigned.
     *
     * @param userId The users unique identifier to filter tasks.
     * @return List of tasks with the specified user.
     */
    public List<TaskResponse> getTasksForUser(Integer userId) {
        log.info("Fetching tasks for user with id: {}", userId);
        try {
            if (!taskRepository.existsByAssignedUserId(userId)) {
                throw new ResponseStatusException(NOT_FOUND, String.format("No user found for id (%s)", userId));
            }
            List<Task> tasks = taskRepository.findByAssignedUserId(userId);
            log.info("Fetched {} tasks for user with id: {}", tasks.size(), userId);
            return mapTaskToResponse(tasks);
        } catch (Exception e) {
            log.error("Error fetching tasks for user with id {}: {}", userId, e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Retrieves tasks with a specific due date.
     *
     * @param dueDate The date to filter tasks.
     * @return List of tasks with the specified due date.
     */
    public List<TaskResponse> getTasksByDueDate(LocalDate dueDate) {
        log.info("Fetching tasks by due date: {}", dueDate);
        List<Task> tasks = taskRepository.findByDueDate(dueDate);
        log.info("Fetched {} tasks by due date: {}", tasks.size(), dueDate);
        return mapTaskToResponse(tasks);
    }

    /**
     * Updates the state for overdue tasks.
     * Fetches tasks with a due date equal to the current date and updates the state to DELAYED
     * for tasks that are currently in the TODO state.
     */
    public void updateTaskStateForOverdueTasks() {
        log.info("Updating state for overdue tasks");
        List<Task> overdueTasks = taskRepository.findByDueDate(LocalDate.now());

        for (Task task : overdueTasks) {
            if (task.getState().equals(TaskState.TODO.name())) {
                task.setState(TaskState.DELAYED.name());
                taskRepository.save(task);
                log.info("Updated state for task: {}", task);
            }
        }
        log.info("Updated state for {} overdue tasks", overdueTasks.size());
    }

}
