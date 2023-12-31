package com.example.taskmanager.config;

import com.example.taskmanager.data.TaskMapper;
import com.example.taskmanager.data.TaskRepository;
import com.example.taskmanager.data.UserMapper;
import com.example.taskmanager.data.UserRepository;
import com.example.taskmanager.service.TaskService;
import com.example.taskmanager.service.UserService;
import org.mapstruct.factory.Mappers;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableMBeanExport;

@Configuration
@EnableMBeanExport
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
    @ConfigurationProperties(prefix = "task-scheduler")
    public TaskSchedulerConfig taskSchedulerConfig(TaskService taskService) {
        return new TaskSchedulerConfig(taskService);
    }
}
