package com.maze.maze.dto.account;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter @Setter
public class EditProfileDTO {
    private String fullname;
    private String phoneNumber;
    private String email;
    private Boolean gender;
    private String birthDate;
    private String avatarUrl;
}
