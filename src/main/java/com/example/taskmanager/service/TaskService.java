package com.example.taskmanager.service;

import com.example.taskmanager.data.Task;
import com.example.taskmanager.data.TaskRepository;
import com.example.taskmanager.data.TaskState;
import com.example.taskmanager.dto.CreateTaskRequest;
import com.example.taskmanager.dto.UpdateTaskRequest;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


/**
 * Service layer responsible for managing tasks.
 */
@RequiredArgsConstructor
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
        return taskRepository.findById(taskId);
    }

    /**
     * Retrieves all tasks in the system.
     *
     * @return List of all tasks.
     */
    @Nonnull
    public List<Task> getAllTasks() {
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
        Task task = new Task();
        task.setDescription(request.description());
        task.setDueDate(request.dueDate());
        task.setAssignedUser(request.assignedUser());
        task.setState(request.state());
        taskRepository.save(task);
        return task;
    }

    /**
     * Updates an existing task in the system.
     *
     * @param id      The unique identifier of the task to update.
     * @param request The request containing updated task details.
     * @return The updated task.
     */
    @Nonnull
    public Optional<Task> updateTask(@Nonnull Integer id, @Nonnull UpdateTaskRequest request) {
        Optional<Task> task = taskRepository.findById(id);
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
        }
        return task;
    }

    /**
     * Deletes a task by its unique identifier.
     *
     * @param taskId The unique identifier of the task to delete.
     */
    public void deleteTask(@Nonnull Integer taskId) {
        taskRepository.deleteById(taskId);
    }

    /**
     * Retrieves tasks with a specific state.
     *
     * @param state The state to filter tasks.
     * @return List of tasks with the specified state.
     */
    @Nonnull
    public List<Task> getTasksByState(@Nonnull String state) {
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
        return taskRepository.findByDueDate(dueDate);
    }

    public void updateTaskStateForOverdueTasks() {
        List<Task> overdueTasks = taskRepository.findByDueDate(LocalDate.now().minusDays(1));

        for (Task task : overdueTasks) {
            if (task.getState().equals(TaskState.TODO.name())) {
                task.setState(TaskState.DELAYED.name());
                taskRepository.save(task);
            }
        }
    }

}
