package com.example.tiktok.service;

import com.example.tiktok.entity.Account;
import com.example.tiktok.entity.Credential;
import com.example.tiktok.entity.dto.AccountLoginDto;
import com.example.tiktok.entity.dto.AccountRegisterDto;
import com.example.tiktok.repository.AccountRepository;
import com.example.tiktok.util.Enums;
import com.example.tiktok.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService implements UserDetailsService {
    final AccountRepository accountRepository;
    final PasswordEncoder passwordEncoder;

    public Account register(AccountRegisterDto accountRegisterDto) {
        Optional<Account> optionalAccount =
                accountRepository.findAccountsByEmail(accountRegisterDto.getEmail());
        if (optionalAccount.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Email is exitst");
        }
        if(accountRegisterDto.getUsername() == null || accountRegisterDto.getUsername().equals("")){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name is not null");
        }
        if(accountRegisterDto.getPassword() == null || accountRegisterDto.getUsername().equals("")){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password is not null");
        }
        Account account = Account.builder()
                .username((accountRegisterDto.getUsername()))
                .passwordHash(passwordEncoder.encode( accountRegisterDto.getPassword()))
                .email(accountRegisterDto.getEmail())
                .role(Enums.AccountSRole.USER)
                .status(Enums.AccountStatus.active)
                .build();
         return accountRepository.save(account);


    }

    public Credential login(AccountLoginDto accountLoginDto) {
        Optional<Account> optionalAccount
                = accountRepository.findAccountsByEmail(accountLoginDto.getEmail());
        if (!optionalAccount.isPresent()) {
            throw new UsernameNotFoundException("Email is not found");
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
            credential.setAccountId(account.getId());
            credential.setAccountUsername(account.getUsername());
            return credential;
        } else {
            throw new UsernameNotFoundException("Password is not match");

        }

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
                new SimpleGrantedAuthority(account.getRole() == Enums.AccountSRole.ADMIN ? "ADMIN" : "USER");
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
    public void deletedById(Long id) {
        accountRepository.deleteById(id);
    }

    public List<Account> findAllByUsername(String username){
        return accountRepository.findAllByUsername(username);
    }
    public Page<Account> search(String keyword,  Pageable pageable) {
        return accountRepository.filter(keyword, pageable);
    }
}
