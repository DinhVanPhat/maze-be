package com.maze.maze.dto.account;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter @Setter
public class RegisterDTO {
    private String fullname;
    private String email;
    private String password; 
    private String phoneNumber;
}
