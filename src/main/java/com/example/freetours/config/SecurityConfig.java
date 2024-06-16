package com.example.freetours.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/api/users/**").hasRole("ADMIN")  // Solo los ADMIN pueden acceder a rutas de usuarios
                                .requestMatchers("/api/tours/**", "/api/reservations/**").authenticated()  // Requiere autenticación
                                .anyRequest().permitAll()  // Permitir todas las demás solicitudes
                )
                .formLogin(formLogin ->
                        formLogin.defaultSuccessUrl("/").permitAll()
                )  // Permitir formulario de login
                .logout(logout -> logout.permitAll())  // Permitir logout
                .csrf(csrf -> csrf.disable());  // Deshabilitar CSRF para simplificar
        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
