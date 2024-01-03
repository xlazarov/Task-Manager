package com.example.taskmanager.service;

import com.example.taskmanager.data.Task;
import com.example.taskmanager.data.TaskRepository;
import com.example.taskmanager.data.TaskState;
import com.example.taskmanager.dto.CreateTaskRequest;
import com.example.taskmanager.dto.UpdateTaskRequest;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


/**
 * Service layer responsible for managing tasks.
 */
@RequiredArgsConstructor
@Slf4j
public class TaskService {

    private final TaskRepository taskRepository;

    /**
     * Retrieves a task by its unique identifier.
     *
     * @param taskId The unique identifier of the task.
     * @return The task with the specified identifier.
     */
    @Nonnull
    public Optional<Task> getTaskById(@Nonnull Integer taskId) {
        log.info("Fetching task with id: {}", taskId);
        return taskRepository.findById(taskId);
    }

    /**
     * Retrieves all tasks in the system.
     *
     * @return List of all tasks.
     */
    @Nonnull
    public List<Task> getAllTasks() {
        log.info("Fetching all tasks");
        return taskRepository.findAll();
    }

    /**
     * Adds a new task to the system.
     *
     * @param request The request containing task details.
     * @return The newly created task.
     */
    @Nonnull
    public Task addTask(@Nonnull CreateTaskRequest request) {
        log.info("Adding a new task: {}", request);
        Task task = new Task();
        task.setDescription(request.description());
        task.setDueDate(request.dueDate());
        task.setAssignedUser(request.assignedUser());
        task.setState(request.state());
        taskRepository.save(task);
        log.info("Added a new task: {}", task);
        return task;
    }

    /**
     * Updates an existing task in the system.
     *
     * @param taskId      The unique identifier of the task to update.
     * @param request The request containing updated task details.
     * @return The updated task.
     */
    @Nonnull
    public Optional<Task> updateTask(@Nonnull Integer taskId, @Nonnull UpdateTaskRequest request) {
        log.info("Updating task with id {}: {}", taskId, request);
        Optional<Task> task = taskRepository.findById(taskId);
        if (task.isPresent()) {
            if (Objects.nonNull(request.description())) {
                task.get().setDescription(request.description());
            }
            if (Objects.nonNull(request.dueDate())) {
                task.get().setDueDate(request.dueDate());
            }
            if (Objects.nonNull(request.assignedUser())) {
                task.get().setAssignedUser(request.assignedUser());
            }
            if (Objects.nonNull(request.state())) {
                task.get().setState(request.state());
            }
            taskRepository.save(task.get());
            log.info("Updated task: {}", task.get());
        }
        return task;
    }

    /**
     * Deletes a task by its unique identifier.
     *
     * @param taskId The unique identifier of the task to delete.
     */
    public void deleteTask(@Nonnull Integer taskId) {
        log.info("Deleting task with id: {}", taskId);
        taskRepository.deleteById(taskId);
    }

    /**
     * Retrieves tasks with a specific state.
     *
     * @param state The state to filter tasks.
     * @return List of tasks with the specified state.
     */
    @Nonnull
    public List<Task> getTasksByState(@Nonnull TaskState state) {
        log.info("Fetching tasks by state: {}", state);
        return taskRepository.findByState(state);
    }

    /**
     * Retrieves tasks with a specific user assigned.
     *
     * @param userId The users unique identifier to filter tasks.
     * @return List of tasks with the specified user.
     */
    @Nonnull
    public List<Task> getTasksForUser(@Nonnull Integer userId) {
        log.info("Fetching tasks for user with id: {}", userId);
        return taskRepository.findByAssignedUserId(userId);
    }

    /**
     * Retrieves tasks with a specific due date.
     *
     * @param dueDate The date to filter tasks.
     * @return List of tasks with the specified due date.
     */
    @Nonnull
    public List<Task> getTasksByDueDate(@Nonnull LocalDate dueDate) {
        log.info("Fetching tasks by due date: {}", dueDate);
        return taskRepository.findByDueDate(dueDate);
    }

    /**
     * Updates the state for overdue tasks.
     * Fetches tasks with a due date equal to the current date and updates the state to DELAYED
     * for tasks that are currently in TO-DO state.
     */
    public void updateTaskStateForOverdueTasks() {
        log.info("Updating state for overdue tasks");
        List<Task> overdueTasks = taskRepository.findByDueDateAndState(LocalDate.now(), TaskState.TODO);

        for (Task task : overdueTasks) {
            task.setState(TaskState.DELAYED);
            taskRepository.save(task);
            log.info("Updated state for task: {}", task);
        }
        log.info("Updated state for {} overdue tasks", overdueTasks.size());
    }
}
