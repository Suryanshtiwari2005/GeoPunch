package com.GeoPunch.backend.security;

import com.GeoPunch.backend.FirebaseAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class securityConfig {

    @Autowired
    private FirebaseAuthFilter firebaseAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())   // disable CSRF
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/auth/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(firebaseAuthFilter , UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
