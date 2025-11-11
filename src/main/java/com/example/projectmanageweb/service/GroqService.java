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
public class GroqService {

	private final RestTemplate restTemplate = new RestTemplate();
	@Value("${groq.api.key}")
    private String apiKey;

    @Value("${groq.api.model:llama-3.1-8b-instant}")
    private String model;

    private static final String URL = "https://api.groq.com/openai/v1/chat/completions";

    public String chat(String systemPrompt, String userPrompt) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);

        Map<String, Object> body = Map.of(
                "model", model,
                "messages", List.of(
                        Map.of("role", "system", "content", systemPrompt),
                        Map.of("role", "user", "content", userPrompt)
                ),
                "temperature", 0.2
        );

        HttpEntity<Map<String, Object>> req = new HttpEntity<>(body, headers);

        Map<String, Object> resp = restTemplate.postForObject(URL, req, Map.class);
        if (resp == null || !resp.containsKey("choices")) {
            throw new RuntimeException("Groq API trả về rỗng hoặc không có 'choices'");
        }

        List<Map<String, Object>> choices = (List<Map<String, Object>>) resp.get("choices");
        if (choices.isEmpty()) {
            throw new RuntimeException("Groq API không trả về choice nào");
        }

        Map<String, Object> first = choices.get(0);
        Map<String, Object> message = (Map<String, Object>) first.get("message");
        Object content = message.get("content");
        return content != null ? content.toString() : "";
    }
	
}
