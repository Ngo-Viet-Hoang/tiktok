package com.example.tiktok.entity.dto;

import com.example.tiktok.util.Enums;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {
    private Long id;
    private String username;
    private String email;
    private String password;
    private Enums.AccountStatus status;
}
