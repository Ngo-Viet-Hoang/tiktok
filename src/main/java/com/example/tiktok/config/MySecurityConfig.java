package com.example.tiktok.config;

import com.example.tiktok.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@Slf4j
@RequiredArgsConstructor
@EnableWebSecurity
public class MySecurityConfig extends WebSecurityConfigurerAdapter {
    final AccountService accountService;
    final PasswordEncoder passwordEncoder;

    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManagerBean();
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        MyAuthenticationFilter authenticationFilter = new MyAuthenticationFilter(authenticationManagerBean());
        authenticationFilter.setFilterProcessesUrl("/api/v1/accounts/login");
        http.cors().and().csrf().disable();
        http.authorizeRequests().antMatchers("/api/v1/accounts/*").permitAll();
        http.authorizeRequests().antMatchers("/api/v1/posts/*").hasAnyAuthority("USER","ADMIN");

        http.addFilterBefore(
                new MyAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

}
