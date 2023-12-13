package com.example.taskmanager.data;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Integer> {
    List<Task> findByDueDate(LocalDate dueDate);
    List<Task> findByAssignedUserId(Integer userId);
    List<Task> findByState(TaskState state);
}
