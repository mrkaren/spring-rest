package com.example.springrest.dto;

import com.example.springrest.model.UserType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserSaveDto {

    private String name;
    private String surname;
    private String email;
    private String password;
    private UserType userType;

}
