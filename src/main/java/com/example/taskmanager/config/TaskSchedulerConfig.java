package com.example.taskmanager.config;

import com.example.taskmanager.service.TaskService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalTime;

@Data
@Configuration
@EnableScheduling
@Slf4j
public class TaskSchedulerConfig {

    private LocalTime executionTime;
    private final TaskService taskService;

    @Scheduled(cron = "${task-scheduler.cron-expression}")
    public void executeScheduledTask() {
        log.info("Executing scheduled task at {}", LocalTime.now());
        taskService.updateTaskStateForOverdueTasks();
        log.info("Scheduled task executed successfully");
    }
}
