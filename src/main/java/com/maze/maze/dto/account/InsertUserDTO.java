package com.maze.maze.dto.account;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class InsertUserDTO {
    private String fullname;
    private String password;
    private String phoneNumber;
    private String email;
    Date birthDate;
    private boolean gender;
    private String role;
    private String avatarUrl;
}
