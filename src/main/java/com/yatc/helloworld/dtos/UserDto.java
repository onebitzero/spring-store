package com.yatc.helloworld.dtos;

import lombok.Data;

@Data
public class UserDto {
    private long id;

    private String email;

    private String name;
}
