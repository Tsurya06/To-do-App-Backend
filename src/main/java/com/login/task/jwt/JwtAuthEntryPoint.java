package com.login.task.jwt;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        Map<String, Object> data = new HashMap<>();
        data.put("success", false);

        String requestURL = request.getRequestURL().toString();
        if (requestURL.contains("/login")) {
            data.put("message", "Invalid login credentials!");
        } else if (requestURL.contains("/signup")) {
            data.put("message", "Signup failed!");
        } else {
            data.put("message", "Something went wrong!");
        }

        ObjectMapper mapper = new ObjectMapper();
        response.getOutputStream().println(mapper.writeValueAsString(data));
    }

}
