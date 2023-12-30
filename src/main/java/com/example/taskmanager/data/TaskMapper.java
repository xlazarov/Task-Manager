package com.example.taskmanager.data;

import com.example.taskmanager.dto.TaskResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface TaskMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "dueDate", target = "dueDate", dateFormat = "dd-MM-yyyy")
    @Mapping(source = "assignedUser", target = "assignedUser")
    @Mapping(source = "state", target = "state")
    TaskResponse taskToTaskResponse(Task task);
}
