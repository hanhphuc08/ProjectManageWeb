package com.example.projectmanageweb.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.projectmanageweb.service.OwnerDashboardService;

@Controller
@RequestMapping("/owner")
public class OwnerDashboardController {

    private final OwnerDashboardService dashboardService;

    public OwnerDashboardController(OwnerDashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/dashboard")
    public String ownerDashboard(Model model) {
        model.addAttribute("dashboard", dashboardService.buildDashboard());
        return "admin/dashboard";
    }
}
