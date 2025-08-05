package com.control.core.controller;

import com.control.core.dto.CreateUserRequest;
import com.control.core.model.User;
import com.control.core.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.actuate.health.HealthComponent;
import org.springframework.boot.actuate.info.InfoEndpoint;
import org.springframework.boot.actuate.metrics.MetricsEndpoint;
import org.springframework.boot.actuate.env.EnvironmentEndpoint;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    
    @Autowired
    private EnvironmentEndpoint environmentEndpoint;
    
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
            if (metricsEndpoint != null) {
                // Get list of available metric names
                Object listResponse = metricsEndpoint.listNames();
                
                // Use Jackson ObjectMapper to safely convert the response
                ObjectMapper objectMapper = new ObjectMapper();
                Set<String> metricNames = new java.util.HashSet<>();
                
                if (listResponse != null) {
                    // Convert to JSON and back to Map to safely extract data
                    String jsonString = objectMapper.writeValueAsString(listResponse);
                    @SuppressWarnings("unchecked")
                    Map<String, Object> responseMap = objectMapper.readValue(jsonString, Map.class);
                    
                    if (responseMap.containsKey("names") && responseMap.get("names") instanceof java.util.Collection) {
                        java.util.Collection<?> namesCollection = (java.util.Collection<?>) responseMap.get("names");
                        for (Object name : namesCollection) {
                            if (name instanceof String) {
                                metricNames.add((String) name);
                            }
                        }
                    }
                }
                
                System.out.println("Available metrics: " + metricNames.size());
                
                // Get detailed data for key metrics (limit to avoid performance issues)
                Map<String, Object> metricValues = new HashMap<>();
                int count = 0;
                for (String metricName : metricNames) {
                    if (count >= 50) break; // Limit to first 50 metrics for performance
                    
                    try {
                        Object metricResponse = metricsEndpoint.metric(metricName, null);
                        
                        // Convert to a more template-friendly format
                        String metricJson = objectMapper.writeValueAsString(metricResponse);
                        @SuppressWarnings("unchecked")
                        Map<String, Object> metricMap = objectMapper.readValue(metricJson, Map.class);
                        
                        // Enhance the metric data with additional information
                        enhanceMetricData(metricName, metricMap);
                        
                        metricValues.put(metricName, metricMap);
                        count++;
                    } catch (Exception e) {
                        // If individual metric fails, store error info but continue
                        Map<String, Object> errorInfo = new HashMap<>();
                        errorInfo.put("error", "Failed to load: " + e.getMessage());
                        metricValues.put(metricName, errorInfo);
                    }
                }
                
                // Prepare data for template
                Map<String, Object> metricsData = new HashMap<>();
                metricsData.put("names", metricNames);
                metricsData.put("totalCount", metricNames.size());
                metricsData.put("loadedCount", count);
                
                model.addAttribute("metricsData", metricsData);
                model.addAttribute("metricValues", metricValues);
                model.addAttribute("lastUpdated", LocalDateTime.now());
                
            } else {
                model.addAttribute("error", "MetricsEndpoint is not available");
            }
            
            model.addAttribute("section", "metrics");
        } catch (Exception e) {
            System.out.println("Error fetching metrics: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Unable to fetch metrics information: " + e.getMessage());
        }
        
        return "metrics-detail";
    }
    
    @GetMapping("/actuator/environment-json")
    @ResponseBody
    public Object environmentJson() {
        try {
            if (environmentEndpoint != null) {
                return environmentEndpoint.environment(null);
            }
            return Map.of("error", "EnvironmentEndpoint not available");
        } catch (Exception e) {
            return Map.of("error", "Unable to fetch environment: " + e.getMessage());
        }
    }
    
    @GetMapping("/actuator/environment-detail")
    public String environmentDetail(Model model) {
        try {
            if (environmentEndpoint != null) {
                // Get the complete environment information
                Object environmentResponse = environmentEndpoint.environment(null);
                
                // Use Jackson ObjectMapper to convert to Map structure
                ObjectMapper objectMapper = new ObjectMapper();
                String environmentJson = objectMapper.writeValueAsString(environmentResponse);
                @SuppressWarnings("unchecked")
                Map<String, Object> environmentMap = objectMapper.readValue(environmentJson, Map.class);
                
                System.out.println("Environment data keys: " + environmentMap.keySet());
                
                // Process property sources for better display
                Object propertySources = environmentMap.get("propertySources");
                Map<String, Object> processedSources = new HashMap<>();
                Map<String, Integer> sourceCounts = new HashMap<>();
                java.util.List<Map<String, Object>> keyPropertiesList = new java.util.ArrayList<>();
                
                if (propertySources instanceof java.util.Collection) {
                    @SuppressWarnings("unchecked")
                    java.util.Collection<Map<String, Object>> sources = 
                        (java.util.Collection<Map<String, Object>>) propertySources;
                    
                    for (Map<String, Object> source : sources) {
                        String sourceName = (String) source.get("name");
                        Object properties = source.get("properties");
                        
                        if (sourceName != null && properties instanceof Map) {
                            @SuppressWarnings("unchecked")
                            Map<String, Object> props = (Map<String, Object>) properties;
                            
                            // Process properties to extract values from Spring's nested structure
                            Map<String, Object> processedProps = new HashMap<>();
                            for (Map.Entry<String, Object> propEntry : props.entrySet()) {
                                String propKey = propEntry.getKey();
                                Object propValue = propEntry.getValue();
                                
                                // Create a property object with key and extracted value
                                Map<String, Object> propertyInfo = new HashMap<>();
                                propertyInfo.put("key", propKey);
                                propertyInfo.put("value", extractPropertyValue(propValue));
                                
                                processedProps.put(propKey, propertyInfo);
                            }
                            
                            // Store processed source info
                            Map<String, Object> sourceInfo = new HashMap<>();
                            sourceInfo.put("name", sourceName);
                            sourceInfo.put("propertyCount", props.size());
                            sourceInfo.put("properties", processedProps);
                            
                            processedSources.put(sourceName, sourceInfo);
                            sourceCounts.put(sourceName, props.size());
                            
                            // Extract key properties for quick overview
                            extractKeyProperties(props, keyPropertiesList, sourceName);
                        }
                    }
                }
                
                // Prepare summary statistics
                Map<String, Object> environmentSummary = new HashMap<>();
                environmentSummary.put("totalSources", processedSources.size());
                environmentSummary.put("totalProperties", sourceCounts.values().stream().mapToInt(Integer::intValue).sum());
                
                // Add data to model
                model.addAttribute("environmentData", environmentMap);
                model.addAttribute("propertySources", processedSources);
                model.addAttribute("sourceCounts", sourceCounts);
                model.addAttribute("keyProperties", keyPropertiesList);
                model.addAttribute("environmentSummary", environmentSummary);
                model.addAttribute("lastUpdated", LocalDateTime.now());
                
            } else {
                model.addAttribute("error", "EnvironmentEndpoint is not available");
            }
            
            model.addAttribute("section", "environment");
        } catch (Exception e) {
            System.out.println("Error fetching environment: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Unable to fetch environment information: " + e.getMessage());
        }
        
        return "environment-detail";
    }
    
    /**
     * Extract key properties that are commonly of interest for environment overview
     */
    private void extractKeyProperties(Map<String, Object> properties, java.util.List<Map<String, Object>> keyPropertiesList, String sourceName) {
        String[] keyPrefixes = {
            "server.", "spring.", "management.", "logging.", "app.", "java.", 
            "user.", "os.", "file.separator", "line.separator", "path.separator"
        };
        
        for (Map.Entry<String, Object> entry : properties.entrySet()) {
            String propName = entry.getKey();
            Object propValue = entry.getValue();
            
            // Extract value from property structure
            String displayValue = extractPropertyValue(propValue);
            
            // Check if this is a key property we want to highlight
            for (String prefix : keyPrefixes) {
                if (propName.startsWith(prefix) || propName.equals("java.version") || 
                    propName.equals("os.name") || propName.equals("user.name") ||
                    propName.contains("port") || propName.contains("url") || propName.contains("path")) {
                    
                    Map<String, Object> propertyInfo = new HashMap<>();
                    propertyInfo.put("key", propName);
                    propertyInfo.put("value", displayValue);
                    propertyInfo.put("source", sourceName);
                    keyPropertiesList.add(propertyInfo);
                    break;
                }
            }
        }
    }
    
    /**
     * Extract the actual value from Spring's property value structure
     */
    private String extractPropertyValue(Object propValue) {
        if (propValue instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> propMap = (Map<String, Object>) propValue;
            Object value = propMap.get("value");
            if (value != null) {
                return value.toString();
            }
            // If no "value" key, try "origin" or return the whole map as string
            Object origin = propMap.get("origin");
            if (origin != null) {
                return origin.toString();
            }
            // Return a cleaned up version of the map
            return propMap.toString().replaceAll("\\{|\\}", "");
        }
        return propValue != null ? propValue.toString() : "null";
    }
    
    /**
     * Enhance metric data to provide better display information for metrics
     * that might not have measurements but have available tags or other data structures.
     */
    private void enhanceMetricData(String metricName, Map<String, Object> metricMap) {
        try {
            // Check if metric has measurements
            Object measurements = metricMap.get("measurements");
            Object availableTags = metricMap.get("availableTags");
            
            // If no measurements but has available tags, try to get metric with common tag values
            if ((measurements == null || (measurements instanceof java.util.Collection && ((java.util.Collection<?>) measurements).isEmpty())) 
                && availableTags != null && availableTags instanceof java.util.Collection) {
                
                @SuppressWarnings("unchecked")
                java.util.Collection<Map<String, Object>> tags = (java.util.Collection<Map<String, Object>>) availableTags;
                
                // Try common tag combinations for metrics that typically need them
                if (!tags.isEmpty()) {
                    Map<String, String> commonTags = findCommonTagValues(metricName, tags);
                    if (!commonTags.isEmpty()) {
                        try {
                            // Try to get metric with common tag values
                            java.util.List<String> tagParams = new java.util.ArrayList<>();
                            for (Map.Entry<String, String> entry : commonTags.entrySet()) {
                                tagParams.add(entry.getKey() + ":" + entry.getValue());
                            }
                            Object enhancedResponse = metricsEndpoint.metric(metricName, tagParams);
                            if (enhancedResponse != null) {
                                ObjectMapper objectMapper = new ObjectMapper();
                                String enhancedJson = objectMapper.writeValueAsString(enhancedResponse);
                                @SuppressWarnings("unchecked")
                                Map<String, Object> enhancedMap = objectMapper.readValue(enhancedJson, Map.class);
                                
                                // Update measurements if we got better data
                                Object enhancedMeasurements = enhancedMap.get("measurements");
                                if (enhancedMeasurements != null && enhancedMeasurements instanceof java.util.Collection
                                    && !((java.util.Collection<?>) enhancedMeasurements).isEmpty()) {
                                    metricMap.put("measurements", enhancedMeasurements);
                                    metricMap.put("enhancedWithTags", commonTags);
                                }
                            }
                        } catch (Exception e) {
                            // If enhancement fails, keep original data
                            metricMap.put("enhancementError", "Could not enhance with tags: " + e.getMessage());
                        }
                    }
                }
                
                // Add helper information for metrics without measurements
                if (measurements == null || (measurements instanceof java.util.Collection && ((java.util.Collection<?>) measurements).isEmpty())) {
                    metricMap.put("displayInfo", "This metric requires specific tags to show values. Available tags: " + tags.size());
                }
            }
            
            // Add metric category for better display
            String category = determineMetricCategory(metricName);
            metricMap.put("category", category);
            
        } catch (Exception e) {
            // If enhancement fails, add error info but don't break the display
            metricMap.put("enhancementError", "Enhancement failed: " + e.getMessage());
        }
    }
    
    /**
     * Find common tag values for metrics that typically need them
     */
    private Map<String, String> findCommonTagValues(String metricName, java.util.Collection<Map<String, Object>> availableTags) {
        Map<String, String> commonTags = new HashMap<>();
        
        // For tomcat session metrics, try common patterns
        if (metricName.startsWith("tomcat.sessions")) {
            for (Map<String, Object> tagDef : availableTags) {
                String tagName = (String) tagDef.get("tag");
                @SuppressWarnings("unchecked")
                java.util.Collection<String> values = (java.util.Collection<String>) tagDef.get("values");
                
                if ("application".equals(tagName) && values != null && !values.isEmpty()) {
                    // Use first available application
                    commonTags.put("application", values.iterator().next());
                }
            }
        }
        
        // For HTTP server requests, try common status codes
        if (metricName.startsWith("http.server.requests")) {
            for (Map<String, Object> tagDef : availableTags) {
                String tagName = (String) tagDef.get("tag");
                @SuppressWarnings("unchecked")
                java.util.Collection<String> values = (java.util.Collection<String>) tagDef.get("values");
                
                if ("status".equals(tagName) && values != null && values.contains("200")) {
                    commonTags.put("status", "200");
                } else if ("method".equals(tagName) && values != null && values.contains("GET")) {
                    commonTags.put("method", "GET");
                }
            }
        }
        
        return commonTags;
    }
    
    /**
     * Determine metric category for better display organization
     */
    private String determineMetricCategory(String metricName) {
        if (metricName.startsWith("jvm.")) return "JVM";
        if (metricName.startsWith("http.")) return "HTTP";
        if (metricName.startsWith("system.")) return "System";
        if (metricName.startsWith("process.")) return "Process";
        if (metricName.startsWith("tomcat.")) return "Tomcat";
        if (metricName.startsWith("hikaricp.")) return "Database";
        if (metricName.startsWith("spring.")) return "Spring";
        return "Other";
    }
}
