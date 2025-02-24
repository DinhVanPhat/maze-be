package com.maze.maze.dto.account;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter @Setter
public class ChangePasswordDTO {
    private String phoneNumber;
    private String password; 
    private String changePassword;

}
