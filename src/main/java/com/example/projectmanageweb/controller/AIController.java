package com.example.projectmanageweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.projectmanageweb.service.DeepSeekService;

@RestController
@RequestMapping("/api/ai")
public class AIController {
	@Autowired
    private DeepSeekService deepSeek;

    @GetMapping("/ask")
    public String ask(@RequestParam String q) {
        return deepSeek.ask(q);
    }

}
