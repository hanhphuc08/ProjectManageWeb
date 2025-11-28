package com.example.projectmanageweb.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.projectmanageweb.service.UserProfileService;
import com.example.projectmanageweb.service.UserService;

@Controller
public class UserProfileController {

	private final UserProfileService profileService;
    private final UserService userService;

    public UserProfileController(UserProfileService profileService,
                                 UserService userService) {
        this.profileService = profileService;
        this.userService = userService;
    }

    @GetMapping("/profile")
    public String myProfile(Authentication auth, Model model) {
        var me = userService.findByEmail(auth.getName()).orElseThrow();
        var view = profileService.loadProfile(me.getUserId());
        model.addAttribute("profile", view);
        return "commons/profile"; 
    }
}
