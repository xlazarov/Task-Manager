package com.example.taskmanager.data;

import com.example.taskmanager.dto.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "username", target = "username")
    UserResponse userToUserResponse(AppUser user);
}
