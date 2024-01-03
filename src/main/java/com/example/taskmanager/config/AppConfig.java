package com.example.taskmanager.config;

import com.example.taskmanager.data.TaskMapper;
import com.example.taskmanager.data.TaskRepository;
import com.example.taskmanager.data.UserMapper;
import com.example.taskmanager.data.UserRepository;
import com.example.taskmanager.service.TaskService;
import com.example.taskmanager.service.UserService;
import org.mapstruct.factory.Mappers;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@EnableMBeanExport
@EnableConfigurationProperties(TaskSchedulerProperties.class)
public class AppConfig {

    @Bean
    public TaskService taskService(TaskRepository taskRepository) {
        return new TaskService(taskRepository);
    }

    @Bean
    public UserService userService(UserRepository userRepository) {
        return new UserService(userRepository);
    }

    @Bean
    public TaskMapper taskMapper() {
        return Mappers.getMapper(TaskMapper.class);
    }

    @Bean
    public UserMapper userMapper() {
        return Mappers.getMapper(UserMapper.class);
    }

    @Bean
    public TaskSchedulerConfig taskSchedulerConfig(TaskService taskService, TaskSchedulerProperties schedulerProperties) {
        return new TaskSchedulerConfig(taskService, schedulerProperties);
    }
}
