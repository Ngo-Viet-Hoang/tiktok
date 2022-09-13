package com.example.tiktok.entity.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AccountRegisterDto {
    private Long id;
    private String username;
    private String password;
    private int role;
}
