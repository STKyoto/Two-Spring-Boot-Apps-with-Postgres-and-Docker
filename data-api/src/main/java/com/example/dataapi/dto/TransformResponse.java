package com.example.dataapi.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class TransformResponse {
    private String transformedText;

    public TransformResponse(String transformedText) {
        this.transformedText = transformedText;
    }
}