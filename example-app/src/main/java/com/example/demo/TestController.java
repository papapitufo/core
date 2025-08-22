package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class TestController {
    
    private static final Logger logger = LoggerFactory.getLogger(TestController.class);
    
    @GetMapping("/logs")
    public Map<String, Object> generateTestLogs() {
        logger.info("🚀 TEST: Info log generated at {}", LocalDateTime.now());
        logger.warn("⚠️ TEST: Warning log generated at {}", LocalDateTime.now());
        logger.error("❌ TEST: Error log generated at {}", LocalDateTime.now());
        logger.debug("🔍 TEST: Debug log generated at {}", LocalDateTime.now());
        
        return Map.of(
            "message", "Test logs generated successfully",
            "timestamp", LocalDateTime.now(),
            "logs_generated", 4
        );
    }
    
    @GetMapping("/activity/{action}")
    public Map<String, Object> simulateActivity(@PathVariable String action) {
        logger.info("📱 USER ACTIVITY: {} performed at {}", action, LocalDateTime.now());
        
        switch (action.toLowerCase()) {
            case "login":
                logger.info("🔐 User login attempt successful");
                break;
            case "logout":
                logger.info("👋 User logout completed");
                break;
            case "navigate":
                logger.info("🧭 User navigated to new page");
                break;
            case "error":
                logger.error("💥 Simulated error occurred during user action");
                break;
            default:
                logger.info("🎭 Unknown user action: {}", action);
        }
        
        return Map.of(
            "action", action,
            "timestamp", LocalDateTime.now(),
            "status", "completed"
        );
    }
}
