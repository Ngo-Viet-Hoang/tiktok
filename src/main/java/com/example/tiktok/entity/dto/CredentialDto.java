package com.example.tiktok.entity.dto;

import com.example.tiktok.entity.Account;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CredentialDto {
    private String accessToken;
    private String refreshToken;
    private long expiredAt;
    private String scope;
    private Long accountId;
    private String  accountUsername;
}
