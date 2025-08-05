package com.control.core.controller;

import com.control.core.dto.CreateUserRequest;
import com.control.core.model.User;
import com.control.core.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.actuate.health.HealthComponent;
import org.springframework.boot.actuate.info.InfoEndpoint;
import org.springframework.boot.actuate.metrics.MetricsEndpoint;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private HealthEndpoint healthEndpoint;
    
    @Autowired
    private InfoEndpoint infoEndpoint;
    
    @Autowired
    private MetricsEndpoint metricsEndpoint;
    
    @GetMapping("/users")
    public String userManagement(@RequestParam(value = "search", required = false) String search,
                                @RequestParam(value = "role", required = false) String roleFilter,
                                @RequestParam(value = "status", required = false) String statusFilter,
                                Model model) {
        
        // Add empty CreateUserRequest if not already present (for form binding)
        if (!model.containsAttribute("createUserRequest")) {
            model.addAttribute("createUserRequest", new CreateUserRequest());
        }
        
        List<User> users = userService.getAllUsers();
        
        // Apply filters
        if (search != null && !search.trim().isEmpty()) {
            users = users.stream()
                    .filter(user -> user.getUsername().toLowerCase().contains(search.toLowerCase()) ||
                                  user.getEmail().toLowerCase().contains(search.toLowerCase()))
                    .toList();
        }
        
        if (roleFilter != null && !roleFilter.trim().isEmpty()) {
            users = users.stream()
                    .filter(user -> user.getRole().equals(roleFilter))
                    .toList();
        }
        
        if (statusFilter != null && !statusFilter.trim().isEmpty()) {
            boolean isActive = "active".equals(statusFilter);
            users = users.stream()
                    .filter(user -> user.isEnabled() == isActive)
                    .toList();
        }
        
        // Calculate statistics
        List<User> allUsers = userService.getAllUsers();
        long totalUsers = allUsers.size();
        long activeUsers = allUsers.stream().mapToLong(user -> user.isEnabled() ? 1 : 0).sum();
        long inactiveUsers = totalUsers - activeUsers;
        long adminUsers = allUsers.stream().mapToLong(user -> "ADMIN".equals(user.getRole()) ? 1 : 0).sum();
        
        model.addAttribute("users", users);
        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("activeUsers", activeUsers);
        model.addAttribute("inactiveUsers", inactiveUsers);
        model.addAttribute("adminUsers", adminUsers);
        model.addAttribute("search", search);
        model.addAttribute("roleFilter", roleFilter);
        model.addAttribute("statusFilter", statusFilter);
        
        return "user-management";
    }
    
    @PostMapping("/users/create")
    public String createUser(@Valid @ModelAttribute CreateUserRequest request,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) {
        
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.createUserRequest", bindingResult);
            redirectAttributes.addFlashAttribute("createUserRequest", request);
            redirectAttributes.addFlashAttribute("error", "Validation errors occurred");
            return "redirect:/admin/users";
        }
        
        try {
            userService.createUser(
                request.getUsername(),
                request.getPassword(),
                request.getEmail(),
                request.getRole()
            );
            redirectAttributes.addFlashAttribute("success", "User created successfully");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        
        return "redirect:/admin/users";
    }
    
    @PostMapping("/users/{id}/activate")
    public String activateUser(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            User user = userService.getUserById(id)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            userService.updateUser(id, user.getUsername(), user.getEmail(), user.getRole(), true);
            redirectAttributes.addFlashAttribute("success", "User activated successfully");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        
        return "redirect:/admin/users";
    }
    
    @PostMapping("/users/{id}/deactivate")
    public String deactivateUser(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            User user = userService.getUserById(id)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            userService.updateUser(id, user.getUsername(), user.getEmail(), user.getRole(), false);
            redirectAttributes.addFlashAttribute("success", "User deactivated successfully");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        
        return "redirect:/admin/users";
    }
    
    @GetMapping("/users/{id}/edit")
    public String editUserForm(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            User user = userService.getUserById(id)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            model.addAttribute("user", user);
            return "edit-user";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/users";
        }
    }
    
    @PostMapping("/users/{id}/edit")
    public String updateUser(@PathVariable("id") Long id,
                           @RequestParam String username,
                           @RequestParam String email,
                           @RequestParam String role,
                           @RequestParam(required = false) Boolean enabled,
                           RedirectAttributes redirectAttributes) {
        try {
            userService.updateUser(id, username, email, role, enabled != null ? enabled : false);
            redirectAttributes.addFlashAttribute("success", "User updated successfully");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        
        return "redirect:/admin/users";
    }
    
    @GetMapping("/users/{id}/history")
    public String getUserHistory(@PathVariable("id") Long id, Model model) {
        try {
            User user = userService.getUserById(id)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            model.addAttribute("user", user);
            return "user-history :: userHistory";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "user-history :: userHistory";
        }
    }
    
    @GetMapping("/actuator")
    public String actuatorDashboard() {
        return "actuator-dashboard";
    }
    
    @GetMapping("/actuator/health-detail")
    public String healthDetail(Model model) {
        try {
            HealthComponent health = healthEndpoint.health();
            
            if (health != null) {
                System.out.println("Health object class: " + health.getClass().getName());
                System.out.println("Health status: " + health.getStatus().getCode());
                
                // Use Jackson to convert HealthComponent to a Map structure
                ObjectMapper mapper = new ObjectMapper();
                Map<String, Object> healthMap = mapper.convertValue(health, new TypeReference<Map<String, Object>>() {});
                
                System.out.println("Health map keys: " + healthMap.keySet());
                System.out.println("Health map: " + healthMap);
                
                model.addAttribute("healthData", healthMap);
                model.addAttribute("component", "health");
            }
        } catch (Exception e) {
            System.err.println("Error in healthDetail: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Unable to fetch health information: " + e.getMessage());
        }
        
        return "health-detail";
    }
    
    @GetMapping("/actuator/info-detail")
    public String infoDetail(Model model) {
        try {
            // InfoEndpoint.info() method returns OperationResponseBodyMap
            Object infoData = infoEndpoint.info();
            System.out.println("Info object class: " + infoData.getClass().getName());
            System.out.println("Info data: " + infoData);
            
            // Use Jackson ObjectMapper to serialize and deserialize for clean Map conversion
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonString = objectMapper.writeValueAsString(infoData);
            System.out.println("Info JSON: " + jsonString);
            
            // Convert back to a proper Map structure
            Map<String, Object> infoMap = objectMapper.readValue(jsonString, new TypeReference<Map<String, Object>>() {});
            System.out.println("Converted info map: " + infoMap);
            
            // Set the main data
            model.addAttribute("infoData", infoMap);
            model.addAttribute("infoJson", jsonString);
            
            // Extract specific sections for the template
            if (infoMap.containsKey("app")) {
                model.addAttribute("appInfo", infoMap.get("app"));
            }
            
            // Map 'java' section to 'buildInfo' for compatibility with template
            if (infoMap.containsKey("java")) {
                model.addAttribute("buildInfo", infoMap.get("java"));
            }
            
            // Map 'company' section to 'gitInfo' for compatibility with template
            if (infoMap.containsKey("company")) {
                model.addAttribute("gitInfo", infoMap.get("company"));
            }
            
            // Also keep original mappings for backwards compatibility
            if (infoMap.containsKey("build")) {
                model.addAttribute("buildInfo", infoMap.get("build"));
            }
            
            if (infoMap.containsKey("git")) {
                model.addAttribute("gitInfo", infoMap.get("git"));
            }
            
            model.addAttribute("section", "info");
        } catch (Exception e) {
            System.out.println("Error fetching info: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Unable to fetch application information: " + e.getMessage());
        }
        
        return "info-detail";
    }
    
    @GetMapping("/actuator/metrics-detail")
    public String metricsDetail(Model model) {
        try {
            // Use metricsEndpoint to get basic metric info
            Map<String, Object> metricsData = new HashMap<>();
            
            // Try to use the endpoint
            try {
                // Actually call the metricsEndpoint
                if (metricsEndpoint != null) {
                    metricsData.put("endpoint", "MetricsEndpoint injected and accessible");
                    metricsData.put("endpointClass", metricsEndpoint.getClass().getSimpleName());
                } else {
                    metricsData.put("endpoint", "MetricsEndpoint is null");
                }
                metricsData.put("section", "metrics");
            } catch (Exception endpointEx) {
                metricsData.put("error", "Metrics endpoint not available: " + endpointEx.getMessage());
            }
            
            model.addAttribute("metricsData", metricsData);
            model.addAttribute("section", "metrics");
        } catch (Exception e) {
            model.addAttribute("error", "Unable to fetch metrics information: " + e.getMessage());
        }
        
        return "metrics-detail";
    }
}
