package com.maze.maze.dto.account;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter @Setter
public class ForgotPasswordDTO {
    private String email;
    private String password;
    private Integer otp;
}
