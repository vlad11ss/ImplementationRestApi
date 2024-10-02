package com.example.RestApiExercise1.configuration;

import com.example.RestApiExercise1.services.CustomUserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailService;

    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Отключаем CSRF для /api/**
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/**","/h2-console/**")

                )
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/registration", "/api/**").permitAll() // Открыть доступ к регистрации и API
                        .requestMatchers("/booking", "/booking/success")
                        .hasAnyAuthority("ROLE_ADMIN", "ROLE_USER") // Разрешить доступ к домашней и записи
                        .anyRequest().authenticated() // Все остальные запросы требуют авторизации
                )
                .formLogin((form) -> form
                        .loginPage("/login").defaultSuccessUrl("/home", true)
                        .permitAll()
                )
                .logout(LogoutConfigurer::permitAll)
                // Добавляем обработку исключений для API
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                )
                .headers(headers -> headers.frameOptions().disable());


        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder auth =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        auth.userDetailsService(userDetailService)
                .passwordEncoder(passwordEncoder());
        return auth.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(8);
    }
}