package com.example.tiktok.config;

import com.example.tiktok.service.AccountService;
import com.example.tiktok.util.Enums;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@Slf4j
@RequiredArgsConstructor
@EnableWebSecurity
public class MySecurityConfig extends WebSecurityConfigurerAdapter {
    final AccountService accountService;
    final PasswordEncoder passwordEncoder;

    private static final String[] IGNORE_PATHS = {
            "/api/v1/accounts/*"
    };
    private static final String[] USER_PATHS = {"/api/v1/posts/**"};

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.headers().frameOptions().disable();
        http.cors()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .csrf().disable();
        http.authorizeRequests()
                .antMatchers(IGNORE_PATHS).permitAll()
                .antMatchers(USER_PATHS).hasAnyAuthority(Enums.AccountStatus.USER.name(), Enums.AccountStatus.ADMIN.name());
        http.addFilterBefore(
                new MyAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

}

//        MyAuthenticationFilter authenticationFilter = new MyAuthenticationFilter(authenticationManagerBean());
//        authenticationFilter.setFilterProcessesUrl("/api/v1/accounts/login");
//        http.cors().and().csrf().disable();
//        http.authorizeRequests().antMatchers("/api/v1/accounts/*").permitAll();
//        http.authorizeRequests().antMatchers("/api/v1/posts/**").permitAll();
//        http.authorizeRequests().and().exceptionHandling().accessDeniedPage("/403");
//
//        http.addFilterBefore(
//                new MyAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);


