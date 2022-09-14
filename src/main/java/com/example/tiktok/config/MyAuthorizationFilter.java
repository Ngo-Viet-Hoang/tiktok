package com.example.tiktok.config;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.tiktok.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

@Slf4j
public class MyAuthorizationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            log.info("Call filter...");
            String fullToken = request.getHeader("Authorization");
            String originalToken = fullToken.replace("Bearer", "").trim();
            DecodedJWT decodedJWT = JwtUtil.getDecodedJwt(originalToken);
            String accountId = decodedJWT.getSubject();
            String username = decodedJWT.getClaim("username").asString();
            String role = decodedJWT.getClaim("role").asString();
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(role));
            log.info("Role: " + role);
            UsernamePasswordAuthenticationToken usernameToken
                    = new UsernamePasswordAuthenticationToken(accountId,null,authorities);
            SecurityContextHolder.getContext().setAuthentication(usernameToken);

        }catch (Exception ex){
            ex.printStackTrace();
        }
        filterChain.doFilter(request,response);


    }

    }

