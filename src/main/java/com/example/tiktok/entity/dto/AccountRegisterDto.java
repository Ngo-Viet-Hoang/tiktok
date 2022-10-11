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
@Builder
public class AccountRegisterDto {
    private Long id;
    private String username;
    private String password;
    @Email(message = "Please enter the correct email form")
    private String email;
    private Enums.AccountSRole role = Enums.AccountSRole.USER;
    private Enums.AccountStatus status = Enums.AccountStatus.active;

    public AccountRegisterDto(Account account){
        BeanUtils.copyProperties(account,this);
    }
}
