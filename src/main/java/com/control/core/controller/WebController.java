package com.control.core.controller;

import com.control.core.dto.CreateUserRequest;
import com.control.core.service.UserService;
import com.control.core.service.PasswordResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

@Controller
public class WebController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private PasswordResetService passwordResetService;
    
    @GetMapping("/")
    public String index() {
        return "redirect:/dashboard";
    }
    
    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                       @RequestParam(value = "logout", required = false) String logout,
                       Model model) {
        if (error != null) {
            model.addAttribute("error", "Invalid username or password");
        }
        if (logout != null) {
            model.addAttribute("success", "You have been logged out successfully");
        }
        return "login";
    }
    
    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("createUserRequest", new CreateUserRequest());
        return "signup";
    }
    
    @PostMapping("/signup")
    public String processSignup(@Valid @ModelAttribute CreateUserRequest createUserRequest,
                               BindingResult bindingResult,
                               @RequestParam("confirmPassword") String confirmPassword,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        
        // Check if passwords match
        if (!createUserRequest.getPassword().equals(confirmPassword)) {
            bindingResult.rejectValue("password", "error.password", "Passwords do not match");
        }
        
        if (bindingResult.hasErrors()) {
            return "signup";
        }
        
        try {
            userService.createUser(
                createUserRequest.getUsername(),
                createUserRequest.getPassword(),
                createUserRequest.getEmail(),
                "USER" // Default role for signup
            );
            redirectAttributes.addFlashAttribute("success", "Account created successfully! Please log in.");
            return "redirect:/login";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "signup";
        }
    }
    
    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }
    
    @GetMapping("/test-email")
    public String testEmail() {
        try {
            passwordResetService.sendPasswordResetEmail("iborrelleom@gmail.com");
            return "redirect:/dashboard?emailTest=success";
        } catch (Exception e) {
            System.err.println("Email test failed: " + e.getMessage());
            e.printStackTrace();
            return "redirect:/dashboard?emailTest=failed&error=" + e.getMessage();
        }
    }
    
    @GetMapping("/forgot-password")
    public String forgotPassword() {
        return "forgot-password";
    }
    
    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam("email") String email, Model model) {
        try {
            passwordResetService.sendPasswordResetEmail(email);
            model.addAttribute("success", true);
            return "forgot-password";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "forgot-password";
        }
    }
    
    @GetMapping("/reset-password")
    public String resetPassword(@RequestParam("token") String token, Model model) {
        if (passwordResetService.isTokenValid(token)) {
            model.addAttribute("token", token);
            return "reset-password";
        } else {
            model.addAttribute("invalidToken", true);
            return "reset-password";
        }
    }
    
    @PostMapping("/reset-password")
    public String processResetPassword(@RequestParam("token") String token,
                                     @RequestParam("password") String password,
                                     @RequestParam("confirmPassword") String confirmPassword,
                                     Model model) {
        
        // Validate token
        if (!passwordResetService.isTokenValid(token)) {
            model.addAttribute("invalidToken", true);
            return "reset-password";
        }
        
        // Validate passwords match
        if (!password.equals(confirmPassword)) {
            model.addAttribute("token", token);
            model.addAttribute("error", "Passwords do not match");
            return "reset-password";
        }
        
        // Validate password length
        if (password.length() < 6) {
            model.addAttribute("token", token);
            model.addAttribute("error", "Password must be at least 6 characters long");
            return "reset-password";
        }
        
        try {
            passwordResetService.resetPassword(token, password);
            model.addAttribute("success", true);
            return "reset-password";
        } catch (RuntimeException e) {
            model.addAttribute("token", token);
            model.addAttribute("error", e.getMessage());
            return "reset-password";
        }
    }
}
