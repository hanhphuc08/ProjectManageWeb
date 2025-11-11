package com.example.projectmanageweb.service;


import java.util.List;
import java.util.Map;
import org.springframework.http.HttpHeaders;

import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DeepSeekService {
	private final RestTemplate restTemplate = new RestTemplate();
    private final String API_KEY = ""; // từ https://console.groq.com/

    public String ask(String question) {
        String url = "https://api.groq.com/openai/v1/chat/completions";

        Map<String, Object> body = Map.of(
                "model", "llama-3.1-8b-instant",
                "messages", List.of(
                        Map.of("role", "user", "content", question)
                )
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + API_KEY);

        HttpEntity<Map<String, Object>> req = new HttpEntity<>(body, headers);

        Map<String, Object> resp = restTemplate.postForObject(url, req, Map.class);

        List<Map<String, Object>> choices = (List<Map<String, Object>>) resp.get("choices");
        Map<String, Object> msg = (Map<String, Object>) choices.get(0).get("message");

        return msg.get("content").toString();
    }

}
