package com.example.projectmanageweb.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.example.projectmanageweb.service.UserService;

@ControllerAdvice
public class GlobalControllerAdvice {

    private final UserService userService;

    public GlobalControllerAdvice(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute("me")
    public Object currentUser(Authentication auth) {
        if (auth == null) return null;
        return userService.findByEmail(auth.getName()).orElse(null);
    }
}
