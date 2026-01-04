package com.yatc.helloworld.dtos;

import lombok.Data;

@Data
public class ChangePasswordRequest {
    private String oldPassword;

    private String newPassword;
}
