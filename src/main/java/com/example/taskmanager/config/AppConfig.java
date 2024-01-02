package com.example.taskmanager.config;

import com.example.taskmanager.data.TaskMapper;
import com.example.taskmanager.data.TaskRepository;
import com.example.taskmanager.data.UserMapper;
import com.example.taskmanager.data.UserRepository;
import com.example.taskmanager.service.TaskService;
import com.example.taskmanager.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableMBeanExport;

@Configuration
@EnableMBeanExport
@Slf4j
public class AppConfig {

    @Bean
    public TaskService taskService(TaskRepository taskRepository) {
        log.info("Creating TaskService bean");
        return new TaskService(taskRepository);
    }

    @Bean
    public UserService userService(UserRepository userRepository) {
        log.info("Creating UserService bean");
        return new UserService(userRepository);
    }

    @Bean
    public TaskMapper taskMapper(){
        log.info("Creating TaskMapper bean");
        return Mappers.getMapper(TaskMapper.class);
    }

    @Bean
    public UserMapper userMapper(){
        log.info("Creating UserMapper bean");
        return Mappers.getMapper(UserMapper.class);
    }

    @Bean
    @ConfigurationProperties(prefix = "task-scheduler")
    public TaskSchedulerConfig taskSchedulerConfig(TaskService taskService) {
        log.info("Creating TaskSchedulerConfig bean");
        return new TaskSchedulerConfig(taskService);
    }
}
