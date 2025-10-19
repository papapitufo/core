package com.control.core.service;

import com.control.core.model.Role;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Service
@Transactional
public class DataInitializationService {
    
    private final PermissionService permissionService;
    private final RoleService roleService;
    
    public DataInitializationService(PermissionService permissionService, RoleService roleService) {
        this.permissionService = permissionService;
        this.roleService = roleService;
    }
    
    @PostConstruct
    public void initializeData() {
        initializePermissions();
        initializeRoles();
        assignPermissionsToRoles();
    }
    
    private void initializePermissions() {
        permissionService.initializeDefaultPermissions();
    }
    
    private void initializeRoles() {
        roleService.initializeDefaultRoles();
    }
    
    private void assignPermissionsToRoles() {
        // Assign permissions to ADMIN role
        var adminRole = roleService.findByName("ADMIN");
        if (adminRole.isPresent()) {
            Role admin = adminRole.get();
            
            // Admin gets all permissions
            String[] adminPermissions = {
                "USER_VIEW", "USER_CREATE", "USER_UPDATE", "USER_DELETE", "USER_PERMISSION_MANAGEMENT",
                "ROLE_VIEW", "ROLE_CREATE", "ROLE_UPDATE", "ROLE_DELETE", "ROLE_PERMISSION_MANAGEMENT",
                "ACTUATOR_HEALTH", "ACTUATOR_METRICS", "ACTUATOR_INFO", "ACTUATOR_MAPPINGS", 
                "ACTUATOR_BEANS", "ACTUATOR_ENV",
                "PERMISSION_VIEW", "PERMISSION_CREATE", "PERMISSION_UPDATE", "PERMISSION_DELETE",
                "SYSTEM_ADMIN", "DASHBOARD_VIEW"
            };
            
            Arrays.stream(adminPermissions).forEach(permissionName -> {
                var permission = permissionService.findByName(permissionName);
                if (permission.isPresent() && !admin.getPermissions().contains(permission.get())) {
                    admin.getPermissions().add(permission.get());
                }
            });
            
            roleService.save(admin);
        }
        
        // Assign basic permissions to USER role
        var userRole = roleService.findByName("USER");
        if (userRole.isPresent()) {
            Role user = userRole.get();
            
            // Regular users get basic permissions
            String[] userPermissions = {
                "DASHBOARD_VIEW",
                "ACTUATOR_HEALTH" // Basic health check
            };
            
            Arrays.stream(userPermissions).forEach(permissionName -> {
                var permission = permissionService.findByName(permissionName);
                if (permission.isPresent() && !user.getPermissions().contains(permission.get())) {
                    user.getPermissions().add(permission.get());
                }
            });
            
            roleService.save(user);
        }
        
        // Assign moderate permissions to MODERATOR role
        var moderatorRole = roleService.findByName("MODERATOR");
        if (moderatorRole.isPresent()) {
            Role moderator = moderatorRole.get();
            
            // Moderators get some admin permissions
            String[] moderatorPermissions = {
                "USER_VIEW", "USER_UPDATE",
                "DASHBOARD_VIEW",
                "ACTUATOR_HEALTH", "ACTUATOR_METRICS", "ACTUATOR_INFO"
            };
            
            Arrays.stream(moderatorPermissions).forEach(permissionName -> {
                var permission = permissionService.findByName(permissionName);
                if (permission.isPresent() && !moderator.getPermissions().contains(permission.get())) {
                    moderator.getPermissions().add(permission.get());
                }
            });
            
            roleService.save(moderator);
        }
    }
}
