package com.control.core.config;

import com.control.core.model.User;
import com.control.core.model.Role;
import com.control.core.model.Permission;
import com.control.core.repository.UserRepository;
import com.control.core.repository.RoleRepository;
import com.control.core.repository.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Create default permissions if they don't exist
        createDefaultPermissions();
        
        // Create default users if they don't exist (roles are created by DataInitializationService)
        createDefaultUsers();
    }
    
    private void createDefaultPermissions() {
        if (!permissionRepository.existsByName("READ_USERS")) {
            Permission readUsers = new Permission();
            readUsers.setName("READ_USERS");
            readUsers.setDescription("View user information");
            readUsers.setCategory("USER_MANAGEMENT");
            permissionRepository.save(readUsers);
        }
        
        if (!permissionRepository.existsByName("WRITE_USERS")) {
            Permission writeUsers = new Permission();
            writeUsers.setName("WRITE_USERS");
            writeUsers.setDescription("Create and edit users");
            writeUsers.setCategory("USER_MANAGEMENT");
            permissionRepository.save(writeUsers);
        }
        
        if (!permissionRepository.existsByName("ADMIN_ACCESS")) {
            Permission adminAccess = new Permission();
            adminAccess.setName("ADMIN_ACCESS");
            adminAccess.setDescription("Access to admin panel");
            adminAccess.setCategory("SYSTEM_ADMINISTRATION");
            permissionRepository.save(adminAccess);
        }
    }
    
    private void createDefaultUsers() {
        // Note: Default admin user is created by DefaultAdminCreator using properties
        // We only create the regular user here
        
        // Create default regular user if not exists
        if (!userRepository.existsByUsername("user")) {
            User user = new User();
            user.setUsername("user");
            user.setPassword(passwordEncoder.encode("user123"));
            user.setEmail("user@example.com");
            user.setRole("USER");
            user.setEnabled(true);
            
            // Assign USER role to regular user (role created by DataInitializationService)
            Role userRole = roleRepository.findByName("USER").orElse(null);
            if (userRole != null) {
                user.getRoles().add(userRole);
            }
            
            userRepository.save(user);
            System.out.println("Default regular user created: username=user, password=user123");
        }
    }
}
