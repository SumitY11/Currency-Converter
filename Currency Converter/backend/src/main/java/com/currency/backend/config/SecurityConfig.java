package com.currency.backend.config;

import com.currency.backend.security.ApiKeyFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final ApiKeyFilter apiKeyFilter;

    public SecurityConfig(ApiKeyFilter apiKeyFilter) {
        this.apiKeyFilter = apiKeyFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            // disable default Spring Security login form & CSRF
            .csrf(csrf -> csrf.disable())
            .httpBasic(httpBasic -> httpBasic.disable())
            .formLogin(form -> form.disable())

            // allow public endpoints
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/",                    // your test route
                    "/h2-console/**",
                    "/swagger-ui/**",
                    "/v3/api-docs/**",
                    "/health"
                ).permitAll()
                // all others require API key
                .anyRequest().authenticated()
            )

            // allow H2 console frames
            .headers(headers -> headers.frameOptions(frame -> frame.disable()))

            // add API key filter BEFORE Spring Security's UsernamePasswordAuthenticationFilter
            .addFilterBefore(apiKeyFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
