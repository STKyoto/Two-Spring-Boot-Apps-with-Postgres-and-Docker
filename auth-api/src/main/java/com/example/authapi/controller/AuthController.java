package com.example.authapi.controller;

import com.example.authapi.model.User;
import com.example.authapi.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/auth/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        String response = authService.register(user);
        return ResponseEntity.status(response.equals("User registered successfully") ? 201 : 400).body(response);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        String response = authService.login(user.getEmail(), user.getPasswordHash());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/process")
    public ResponseEntity<Map<String, String>> processText(
            Authentication authentication,
            @RequestBody Map<String, String> payload) {

        String userEmail = authentication.getName();
        String inputText = payload.get("text");

        String transformedText = authService.processAndSaveText(userEmail, inputText);

        return ResponseEntity.ok(Map.of("result", transformedText));
    }
}
