package com.example.taskmanager.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "task-scheduler")
@Getter
@Setter
public class TaskSchedulerProperties {
    private String cronExpression;
}
