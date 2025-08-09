package com.control.core.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

@Service("authorizationService")
public class AuthorizationService {
    
    private final UserService userService;
    
    public AuthorizationService(UserService userService) {
        this.userService = userService;
    }
    
    /**
     * Check if the authenticated user has a specific permission
     */
    public boolean hasPermission(Authentication authentication, String permissionName) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        
        // Check if user has the permission through Spring Security authorities
        return authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .anyMatch(authority -> authority.equals(permissionName));
    }
    
    /**
     * Check if the authenticated user has any of the specified permissions
     */
    public boolean hasAnyPermission(Authentication authentication, String... permissions) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        
        for (String permission : permissions) {
            if (hasPermission(authentication, permission)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Check if the authenticated user has all of the specified permissions
     */
    public boolean hasAllPermissions(Authentication authentication, String... permissions) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        
        for (String permission : permissions) {
            if (!hasPermission(authentication, permission)) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Check if the authenticated user has a specific role
     */
    public boolean hasRole(Authentication authentication, String roleName) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        
        String roleAuthority = "ROLE_" + roleName;
        return authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .anyMatch(authority -> authority.equals(roleAuthority));
    }
    
    /**
     * Check if the authenticated user is the owner of a resource or has admin role
     */
    public boolean isOwnerOrAdmin(Authentication authentication, Long userId) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        
        // Check if user is admin
        if (hasRole(authentication, "ADMIN")) {
            return true;
        }
        
        // Check if user is the owner
        try {
            var user = userService.findByUsername(authentication.getName());
            return user.isPresent() && user.get().getId().equals(userId);
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Check if the authenticated user can access admin functions
     */
    public boolean canAccessAdmin(Authentication authentication) {
        return hasRole(authentication, "ADMIN") || 
               hasPermission(authentication, "SYSTEM_ADMIN") ||
               hasPermission(authentication, "DASHBOARD_VIEW");
    }
    
    /**
     * Check if the authenticated user can manage users
     */
    public boolean canManageUsers(Authentication authentication) {
        return hasAnyPermission(authentication, 
            "USER_CREATE", "USER_UPDATE", "USER_DELETE", "USER_VIEW");
    }
    
    /**
     * Check if the authenticated user can view system monitoring
     */
    public boolean canViewSystemMonitoring(Authentication authentication) {
        return hasAnyPermission(authentication,
            "ACTUATOR_HEALTH", "ACTUATOR_METRICS", "ACTUATOR_INFO", 
            "ACTUATOR_MAPPINGS", "ACTUATOR_BEANS", "ACTUATOR_ENV");
    }
}
