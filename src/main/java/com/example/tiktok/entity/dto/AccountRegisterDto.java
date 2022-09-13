package com.example.tiktok.entity.dto;

import lombok.*;

import javax.validation.constraints.Email;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AccountRegisterDto {
    private Long id;
    private String username;
    private String password;
    @Email(message = "invalid email address")
    private String email;
    private int role;
}
