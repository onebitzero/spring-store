package com.yatc.helloworld.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.yatc.helloworld.dtos.CreateUserRequest;
import com.yatc.helloworld.dtos.UpdateUserRequest;
import com.yatc.helloworld.dtos.UserDto;
import com.yatc.helloworld.entities.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(CreateUserRequest request);

    UserDto toDto(User user);

    void update(UpdateUserRequest request, @MappingTarget User user);
}
