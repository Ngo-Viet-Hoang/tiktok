package com.example.tiktok.service;

import com.example.tiktok.entity.Account;
import com.example.tiktok.entity.Credential;
import com.example.tiktok.entity.dto.AccountLoginDto;
import com.example.tiktok.entity.dto.AccountRegisterDto;
import com.example.tiktok.repository.AccountRepository;
import com.example.tiktok.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService implements UserDetailsService {
    final AccountRepository accountRepository;
    final PasswordEncoder passwordEncoder;

    public AccountRegisterDto register(AccountRegisterDto accoutRegisterDto) {
        Optional<Account> optionalAccount =
                accountRepository.findAccountByUsername(accoutRegisterDto.getUsername());
        if (optionalAccount.isPresent()) {
            return null;
        }
        Account account = Account.builder()
                .username((accoutRegisterDto.getUsername()))
                .passwordHash(passwordEncoder.encode( accoutRegisterDto.getPassword()))
                .email(accoutRegisterDto.getEmail())
                .role(accoutRegisterDto.getRole())
                .build();
        accountRepository.save(account);
        accoutRegisterDto.setId(account.getId());
        return  accoutRegisterDto;


    }

    public Credential login(AccountLoginDto accountLoginDto) {
        Optional<Account> optionalAccount
                = accountRepository.findAccountByUsername(accountLoginDto.getUsername());
        if (!optionalAccount.isPresent()) {
            throw new UsernameNotFoundException("User is not found");
        }
        Account account = optionalAccount.get();
        boolean isMatch = passwordEncoder.matches(accountLoginDto.getPassword(), account.getPasswordHash());
        if (isMatch) {
            int expiredAfterDay = 7;
            String accessToken =
                    JwtUtil.generateTokenByAccount(account, expiredAfterDay = 24 * 60 * 60 * 1000);
            String refreshToken =
                    JwtUtil.generateTokenByAccount(account, 14 * 24 * 60 * 60 * 1000);
            Credential credential = new Credential();
            credential.setAccessToken(accessToken);
            credential.setRefreshToken(refreshToken);
            credential.setExpiredAt(expiredAfterDay);
            credential.setScope("basic_information");
            return credential;
        } else {
            throw new UsernameNotFoundException("Password is not match");

        }

    }

    public void getInformation() {

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Account> optionalAccount = accountRepository.findAccountByUsername(username);
        if (!optionalAccount.isPresent()) {
            throw new UsernameNotFoundException("Username is not found");
        }
        Account account = optionalAccount.get();
        List<GrantedAuthority> authorities = new ArrayList<>();
        SimpleGrantedAuthority simpleGrantedAuthority =
                new SimpleGrantedAuthority(account.getRole() == 1 ? "ADMIN" : "USER");
        authorities.add(simpleGrantedAuthority);
        return new User(account.getUsername(), account.getPasswordHash(), authorities);
    }

    public List<Account> findAll() {
        return accountRepository.findAll();
    }
    public Optional<Account> findById(Long id) {
        return accountRepository.findById(id);
    }

    public Account save(Account account) {
        return accountRepository.save(account);
    }

}
