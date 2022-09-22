package com.example.tiktok.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Credential {
    private String accessToken;
    private String refreshToken;
    private long expiredAt;
    private String scope;
//    private Long accountId;
//    private String accountUsername;

}
