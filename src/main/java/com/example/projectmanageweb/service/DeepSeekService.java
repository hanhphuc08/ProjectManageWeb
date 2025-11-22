package com.example.projectmanageweb.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DeepSeekService {
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${groq.api.key:}")
    private String apiKey;

    @Value("${groq.api.model:llama-3.1-8b-instant}")
    private String model;

    private static final String URL =
            "https://api.groq.com/openai/v1/chat/completions";

    public String ask(String question) {
        Map<String, Object> body = Map.of(
                "model", model,
                "messages", List.of(
                        Map.of("role", "user", "content", question)
                )
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // chỉ set Authorization nếu có key
        if (apiKey != null && !apiKey.isBlank()) {
            headers.setBearerAuth(apiKey);
        }

        HttpEntity<Map<String, Object>> req = new HttpEntity<>(body, headers);

        Map<String, Object> resp =
                restTemplate.postForObject(URL, req, Map.class);

        if (resp == null || resp.get("choices") == null) {
            throw new IllegalStateException("Groq response is empty");
        }

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> choices = (List<Map<String, Object>>) resp.get("choices");

        @SuppressWarnings("unchecked")
        Map<String, Object> msg =
                (Map<String, Object>) choices.get(0).get("message");

        return msg.get("content").toString();
    }
}
