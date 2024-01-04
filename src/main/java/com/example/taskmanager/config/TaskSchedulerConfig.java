package com.example.taskmanager.config;

import com.example.taskmanager.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

@RequiredArgsConstructor
public class TaskSchedulerConfig implements SchedulingConfigurer {

    private final TaskService taskService;
    private final TaskSchedulerProperties schedulerProperties;

    /**
     * Configures a cron task to update overdue tasks based on the specified cron expression.
     * The cron expression is retrieved from the {@link TaskSchedulerProperties} instance.
     *
     * @param taskRegistrar The registrar for configuring scheduled tasks.
     */
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.addCronTask(taskService::updateTaskStateForOverdueTasks, schedulerProperties.getCronExpression());
    }
}
