package com.example.dataapi.service;

import org.springframework.stereotype.Service;

    @Service
    public class TransformService {

        public String transformText(String text) {
            if (text == null) {
                return "";
            }
            return new StringBuilder(text).reverse().toString();
        }
}
