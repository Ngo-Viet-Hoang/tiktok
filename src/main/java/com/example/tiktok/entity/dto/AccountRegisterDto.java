package com.example.tiktok.entity.dto;

import com.example.tiktok.entity.Account;
import com.example.tiktok.util.Enums;
import lombok.*;
import org.springframework.beans.BeanUtils;

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
    private String email;
    private Enums.AccountStatus status;

    public AccountRegisterDto(Account account){
        BeanUtils.copyProperties(account,this);
    }
}
