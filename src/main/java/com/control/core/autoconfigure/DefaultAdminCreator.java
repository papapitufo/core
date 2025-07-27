package com.control.core.autoconfigure;

import com.control.core.model.User;
import com.control.core.repository.UserRepository;
import com.control.core.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * Creates default admin user on startup if configured
 */
@Component
@ConditionalOnProperty(prefix = "core.auth.admin", name = "enabled", havingValue = "true", matchIfMissing = true)
public class DefaultAdminCreator implements CommandLineRunner {
    
    private static final Logger logger = LoggerFactory.getLogger(DefaultAdminCreator.class);
    
    @Autowired
    private CoreAuthProperties properties;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private UserRepository userRepository;
    
    @Override
    public void run(String... args) throws Exception {
        CoreAuthProperties.AdminUser adminConfig = properties.getAdmin();
        
        if (!adminConfig.isEnabled()) {
            return;
        }
        
        // Check if admin user already exists
        if (userRepository.findByUsername(adminConfig.getUsername()).isPresent() ||
            userRepository.findByEmail(adminConfig.getEmail()).isPresent()) {
            logger.info("Default admin user already exists, skipping creation");
            return;
        }
        
        try {
            User adminUser = userService.createUser(
                adminConfig.getUsername(),
                adminConfig.getPassword(),
                adminConfig.getEmail(),
                "ADMIN"
            );
            
            logger.info("Default admin user created successfully: {}", adminUser.getUsername());
        } catch (Exception e) {
            logger.error("Failed to create default admin user", e);
        }
    }
}
