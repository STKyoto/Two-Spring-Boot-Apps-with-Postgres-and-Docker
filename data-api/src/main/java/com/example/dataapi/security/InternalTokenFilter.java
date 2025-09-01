package com.example.dataapi.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class InternalTokenFilter extends OncePerRequestFilter {

    private final String internalToken;

    public InternalTokenFilter(@Value("${internal.token}") String internalToken) {
        this.internalToken = internalToken;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = request.getHeader("X-Internal-Token");

        if (token == null || !token.equals(internalToken)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("Invalid internal token");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
