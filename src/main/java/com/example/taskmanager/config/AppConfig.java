package com.example.taskmanager.config;

import com.example.taskmanager.data.TaskRepository;
import com.example.taskmanager.data.UserRepository;
import com.example.taskmanager.service.TaskService;
import com.example.taskmanager.service.UserService;
import com.example.taskmanager.web.TaskController;
import com.example.taskmanager.web.UserController;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.context.annotation.Scope;

@Configuration
@EnableMBeanExport // Enable JMX
public class AppConfig {

    @Bean
    @Scope("singleton") // by default though
    public TaskService taskService(TaskRepository taskRepository) {
        return new TaskService(taskRepository);
    }

    @Bean
    public UserService userService(UserRepository userRepository) {
        return new UserService(userRepository);
    }

    @Bean
    public TaskController taskController(TaskService taskService) {
        return new TaskController(taskService);
    }

    @Bean
    public UserController userController(UserService userService) {
        return new UserController(userService);
    }

    @Bean
    @ConfigurationProperties(prefix = "custom")
    public CustomProperties customProperties() {
        return new CustomProperties();
    }
}
