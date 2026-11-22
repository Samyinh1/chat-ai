package com.chatapp.backend.identity;

import java.io.IOException;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class UserIdentityFilter extends OncePerRequestFilter {

    public static final String HEADER = "X-Owner-Id";
    public static final String ATTR = "ownerId";

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // CORS preflights carry no custom headers; only guard the API.
        return "OPTIONS".equalsIgnoreCase(request.getMethod())
                || !request.getRequestURI().startsWith("/api/");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        UUID ownerId = parse(request.getHeader(HEADER));
        if (ownerId == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json");
            response.getWriter().write("{\"status\":400,\"error\":\"Bad Request\","
                    + "\"message\":\"Missing or invalid " + HEADER + " header\"}");
            return;
        }
        request.setAttribute(ATTR, ownerId);
        chain.doFilter(request, response);
    }

    private static UUID parse(String raw) {
        if (raw == null) {
            return null;
        }
        try {
            return UUID.fromString(raw.trim());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}