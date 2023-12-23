package com.example.taskmanager.config;

import com.example.taskmanager.service.TaskService;
import lombok.Data;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalTime;

@Data
@Configuration
@EnableScheduling
public class TaskSchedulerConfig {

    private LocalTime executionTime;
    private final TaskService taskService;

    @Scheduled(cron = "${task-scheduler.cron-expression}")
    public void executeScheduledTask() {
        taskService.updateTaskStateForOverdueTasks();
    }
}
