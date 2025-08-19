package com.example.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            model.addAttribute("username", authentication.getName());
            model.addAttribute("authorities", authentication.getAuthorities());
        }
        return "home";
    }

    @GetMapping("/app-dashboard")
    public String appDashboard(Model model, Authentication authentication) {
        System.out.println("🔍 [DEBUG] appDashboard() called");
        System.out.println("🔍 [DEBUG] Authentication object: " + authentication);
        
        if (authentication != null && authentication.isAuthenticated()) {
            System.out.println("🔍 [DEBUG] User is authenticated: " + authentication.getName());
            System.out.println("🔍 [DEBUG] Authorities: " + authentication.getAuthorities());
            
            model.addAttribute("username", authentication.getName());
            model.addAttribute("authorities", authentication.getAuthorities());
        } else {
            System.out.println("🔍 [DEBUG] User is NOT authenticated");
            model.addAttribute("username", "Not authenticated");
            model.addAttribute("authorities", "None");
        }
        
        System.out.println("🔍 [DEBUG] Returning app-dashboard template");
        return "app-dashboard";
    }
}
