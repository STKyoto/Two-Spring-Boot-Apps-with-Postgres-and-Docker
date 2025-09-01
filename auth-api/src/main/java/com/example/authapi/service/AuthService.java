package com.example.authapi.service;

import com.example.authapi.dto.TransformResponse;
import com.example.authapi.model.ProcessingLog;
import com.example.authapi.model.User;
import com.example.authapi.repository.ProcessingLogRepository;
import com.example.authapi.repository.UserRepository;
import com.example.authapi.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RestTemplate restTemplate;
    private final ProcessingLogRepository processingLogRepository;

    @Value("${data.api.internal.token}")
    private String internalToken;

    @Value("${data.api.base.url}")
    private String dataApiBaseUrl;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       JwtTokenProvider jwtTokenProvider, RestTemplate restTemplate,
                       ProcessingLogRepository processingLogRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.restTemplate = restTemplate;
        this.processingLogRepository = processingLogRepository;
    }

    public String register(User user) {
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        userRepository.save(user);
        return "User registered successfully";
    }

    public String login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (passwordEncoder.matches(password, user.getPasswordHash())) {
            return jwtTokenProvider.generateToken(user.getEmail());
        } else {
            throw new RuntimeException("Invalid credentials");
        }
    }

    public String processAndSaveText(String email, String inputText) {

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Internal-Token", internalToken);
        headers.setContentType(MediaType.TEXT_PLAIN);

        HttpEntity<String> requestEntity = new HttpEntity<>(inputText, headers);
        String apiUrl = dataApiBaseUrl + "/api/transform";

        ResponseEntity<TransformResponse> responseEntity = restTemplate.postForEntity(apiUrl, requestEntity, TransformResponse.class);

        String transformedText = "";
        try {
            TransformResponse responseBody = responseEntity.getBody();
            if (responseBody != null && responseBody.getTransformedText() != null) {
                transformedText = responseBody.getTransformedText();
            } else {
                throw new RuntimeException("Unexpected response format from data-api");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to process response from data-api", e);
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found for logging"));

        ProcessingLog logEntry = new ProcessingLog();
        logEntry.setUserId(user.getId());
        logEntry.setInputText(inputText);
        logEntry.setOutputText(transformedText);
        logEntry.setCreatedAt(Instant.now());

        processingLogRepository.save(logEntry);

        return transformedText;
    }
}