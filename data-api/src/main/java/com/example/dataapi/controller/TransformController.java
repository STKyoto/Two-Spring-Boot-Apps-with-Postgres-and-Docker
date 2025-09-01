package com.example.dataapi.controller;

import com.example.dataapi.dto.TransformResponse;
import com.example.dataapi.service.TransformService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransformController {

    private final TransformService transformService;

    public TransformController(TransformService transformService) {
        this.transformService = transformService;
    }

    @PostMapping("/api/transform")
    public TransformResponse transformText(@RequestBody String text) {
        String transformed = transformService.transformText(text);
        return new TransformResponse(transformed);
    }
}
