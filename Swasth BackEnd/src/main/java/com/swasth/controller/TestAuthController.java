package com.swasth.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.swasth.security.JwtUtil;

@RestController
@RequestMapping("/api/test-auth")
public class TestAuthController {
    
    private final JwtUtil jwtUtil;
    
    public TestAuthController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }
    
    @GetMapping("/generate-token")
    public Map<String, String> generateTestToken(@RequestParam String role , @RequestParam int userId) {
        // Generate token for test user (ID: 1)
        String token = jwtUtil.generateToken(userId, role.toUpperCase());
        
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        response.put("type", "Bearer");
        
        return response;
    }
}