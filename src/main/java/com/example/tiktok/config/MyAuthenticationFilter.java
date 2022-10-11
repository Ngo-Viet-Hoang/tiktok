package com.example.tiktok.config;

import com.example.tiktok.entity.Account;
import com.example.tiktok.entity.dto.AccountLoginDto;
import com.example.tiktok.entity.dto.CredentialDto;
import com.example.tiktok.repository.AccountRepository;
import com.example.tiktok.service.AccountService;
import com.example.tiktok.util.JwtUtil;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class MyAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    final AuthenticationManager authenticationManager;
    @Autowired
    AccountRepository accountRepository;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            String jsonData = request.getReader().lines().collect(Collectors.joining());
            Gson gson = new Gson();
            AccountLoginDto accountLoginDto = gson.fromJson(jsonData, AccountLoginDto.class);
            UsernamePasswordAuthenticationToken userToken = new UsernamePasswordAuthenticationToken(
                    accountLoginDto.getEmail(), accountLoginDto.getPassword());
            return authenticationManager.authenticate(userToken);
        }catch (IOException exception){
            return  null;
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        User user = (User) authResult.getPrincipal();
        Optional<Account> accountOptional = accountRepository.findAccountByUsername(user.getUsername());
        if (!accountOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Account not found");
        }
        Account account = accountOptional.get();
        String accessToken = JwtUtil.generateToken(user.getUsername(),
                user.getAuthorities().iterator().next().getAuthority(),
                request.getRequestURL().toString(),
                JwtUtil.ONE_DAY *7);
        String refreshToken = JwtUtil.generateToken(user.getUsername(),
                user.getAuthorities().iterator().next().getAuthority(),
                request.getRequestURL().toString(),
                JwtUtil.ONE_DAY *14);
        CredentialDto credential = new CredentialDto(accessToken, refreshToken,
                JwtUtil.ONE_DAY * 7 ,"basic_information", account.getId(), account.getUsername());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        Gson gson = new Gson();
        response.getWriter().println(gson.toJson(credential));
        System.out.println("SUccesc login");
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        HashMap<String,String> errors = new HashMap<>();
        errors.put("message","Invalid information");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        Gson gson = new Gson();
        response.getWriter().println(gson.toJson(errors));
    }
    }

