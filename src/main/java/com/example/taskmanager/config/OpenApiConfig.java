package com.example.taskmanager.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                description = "OpenApi documentation for TaskManager",
                title = "OpenApi specification"
        ),
        servers = @Server( description = "Local ENV",
        url = "http://localhost:8080")
)
public class OpenApiConfig {
}
