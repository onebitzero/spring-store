package com.yatc.helloworld.dtos;

import com.yatc.helloworld.validation.LowerCase;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateUserRequest {
    @NotBlank(message = "Enter an email")
    @Email(message = "Enter a valid email")
    @LowerCase(message = "Email must be in lower case")
    private String email;

    @NotBlank(message = "Enter a name")
    private String name;

    @NotBlank(message = "Enter a password")
    private String password;
}
