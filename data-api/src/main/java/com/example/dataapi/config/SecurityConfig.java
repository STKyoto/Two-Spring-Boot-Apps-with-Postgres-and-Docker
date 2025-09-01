package com.example.dataapi.config;

import com.example.dataapi.security.InternalTokenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final InternalTokenFilter internalTokenFilter;

    public SecurityConfig(InternalTokenFilter internalTokenFilter) {
        this.internalTokenFilter = internalTokenFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(internalTokenFilter, UsernamePasswordAuthenticationFilter.class);

        http.authorizeHttpRequests(authorizeRequests ->
                authorizeRequests
                        .requestMatchers("/api/transform").permitAll()
                        .anyRequest().authenticated()
        );

        return http.build();
    }
}
