package com.example.tiktok.entity;

import com.example.tiktok.entity.basic.BaseEntity;
import com.example.tiktok.entity.dto.AccountRegisterDto;
import com.example.tiktok.util.Enums;
import lombok.*;
import org.springframework.beans.BeanUtils;

import javax.persistence.*;
import javax.validation.constraints.Email;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "accounts")
public class Account extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String passwordHash;
    private String email;
    private String image;
    @Enumerated(EnumType.STRING)
    private Enums.AccountSRole role;
    @Enumerated(EnumType.STRING)
    private Enums.AccountStatus status = Enums.AccountStatus.active;


    public Account(AccountRegisterDto accountRegisterDto){
        BeanUtils.copyProperties(accountRegisterDto,this);
    }



}
