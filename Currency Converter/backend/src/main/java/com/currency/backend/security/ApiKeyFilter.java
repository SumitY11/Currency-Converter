package com.currency.backend.security;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
public class ApiKeyFilter extends OncePerRequestFilter {

    @Value("${api.key:}")
    private String apiKey;

      @PostConstruct
    public void checkKey() {
        if (apiKey == null || apiKey.isBlank()) {
            System.err.println("⚠ API KEY NOT LOADED!");
        } else {
            System.out.println("✅ Loaded API KEY = " + apiKey);
        }
    }
    
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        // Skip public paths
        if (path.equals("/") || path.equals("/health") || path.startsWith("/h2-console") ||
            path.startsWith("/swagger-ui") || path.startsWith("/v3/api-docs")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Check API key for protected routes
        String requestApiKey = request.getHeader("X-API-KEY");
        if (requestApiKey == null || !requestApiKey.equals(apiKey)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid or missing API Key");
            return;
        }

        // ✅ Mark request as authenticated with authority to avoid 403
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(
                        "apiKeyUser",
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_API"))
                );
        SecurityContextHolder.getContext().setAuthentication(auth);

        filterChain.doFilter(request, response);
    }
}
