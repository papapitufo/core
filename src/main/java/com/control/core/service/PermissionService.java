package com.control.core.service;

import com.control.core.model.Permission;
import com.control.core.repository.PermissionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class PermissionService {
    
    private final PermissionRepository permissionRepository;
    
    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }
    
    public List<Permission> findAll() {
        return permissionRepository.findAllOrderByCategory();
    }
    
    public Optional<Permission> findById(Long id) {
        return permissionRepository.findById(id);
    }
    
    public Optional<Permission> findByName(String name) {
        return permissionRepository.findByName(name);
    }
    
    public List<Permission> findByCategory(String category) {
        return permissionRepository.findByCategory(category);
    }
    
    public Map<String, List<Permission>> findAllByCategory() {
        return permissionRepository.findAll()
            .stream()
            .collect(Collectors.groupingBy(permission -> 
                permission.getCategory() != null ? permission.getCategory() : "UNCATEGORIZED"));
    }
    
    public List<String> findAllCategories() {
        return permissionRepository.findAllCategories();
    }
    
    public Permission save(Permission permission) {
        validatePermission(permission);
        return permissionRepository.save(permission);
    }
    
    public Permission create(String name, String description, String category) {
        if (permissionRepository.existsByName(name)) {
            throw new IllegalArgumentException("Permission with name '" + name + "' already exists");
        }
        
        Permission permission = new Permission(name, description, category);
        return permissionRepository.save(permission);
    }
    
    public Permission update(Long id, String name, String description, String category) {
        Permission permission = permissionRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Permission not found with id: " + id));
        
        // Check if name is being changed and if it would create a conflict
        if (!permission.getName().equals(name) && permissionRepository.existsByName(name)) {
            throw new IllegalArgumentException("Permission with name '" + name + "' already exists");
        }
        
        permission.setName(name);
        permission.setDescription(description);
        permission.setCategory(category);
        
        return permissionRepository.save(permission);
    }
    
    public void delete(Long id) {
        Permission permission = permissionRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Permission not found with id: " + id));
        
        // Check if permission is in use
        if (!permission.getRoles().isEmpty() || !permission.getUsers().isEmpty()) {
            throw new IllegalStateException("Cannot delete permission '" + permission.getName() + 
                "' because it is assigned to roles or users");
        }
        
        permissionRepository.delete(permission);
    }
    
    public void initializeDefaultPermissions() {
        if (permissionRepository.count() == 0) {
            createDefaultPermissions();
        }
    }
    
    private void createDefaultPermissions() {
        // User Management Permissions
        createPermissionIfNotExists("USER_VIEW", "View user information", "USER_MANAGEMENT");
        createPermissionIfNotExists("USER_CREATE", "Create new users", "USER_MANAGEMENT");
        createPermissionIfNotExists("USER_UPDATE", "Update user information", "USER_MANAGEMENT");
        createPermissionIfNotExists("USER_DELETE", "Delete users", "USER_MANAGEMENT");
        createPermissionIfNotExists("USER_PERMISSION_MANAGEMENT", "Manage user permissions", "USER_MANAGEMENT");
        
        // Role Management Permissions
        createPermissionIfNotExists("ROLE_VIEW", "View roles", "ROLE_MANAGEMENT");
        createPermissionIfNotExists("ROLE_CREATE", "Create new roles", "ROLE_MANAGEMENT");
        createPermissionIfNotExists("ROLE_UPDATE", "Update roles", "ROLE_MANAGEMENT");
        createPermissionIfNotExists("ROLE_DELETE", "Delete roles", "ROLE_MANAGEMENT");
        createPermissionIfNotExists("ROLE_PERMISSION_MANAGEMENT", "Manage role permissions", "ROLE_MANAGEMENT");
        
        // System Monitoring Permissions
        createPermissionIfNotExists("ACTUATOR_HEALTH", "View system health", "SYSTEM_MONITORING");
        createPermissionIfNotExists("ACTUATOR_METRICS", "View system metrics", "SYSTEM_MONITORING");
        createPermissionIfNotExists("ACTUATOR_INFO", "View application info", "SYSTEM_MONITORING");
        createPermissionIfNotExists("ACTUATOR_MAPPINGS", "View request mappings", "SYSTEM_MONITORING");
        createPermissionIfNotExists("ACTUATOR_BEANS", "View application beans", "SYSTEM_MONITORING");
        createPermissionIfNotExists("ACTUATOR_ENV", "View environment properties", "SYSTEM_MONITORING");
        
        // Permission Management
        createPermissionIfNotExists("PERMISSION_VIEW", "View permissions", "PERMISSION_MANAGEMENT");
        createPermissionIfNotExists("PERMISSION_CREATE", "Create new permissions", "PERMISSION_MANAGEMENT");
        createPermissionIfNotExists("PERMISSION_UPDATE", "Update permissions", "PERMISSION_MANAGEMENT");
        createPermissionIfNotExists("PERMISSION_DELETE", "Delete permissions", "PERMISSION_MANAGEMENT");
        
        // System Administration
        createPermissionIfNotExists("SYSTEM_ADMIN", "Full system administration", "SYSTEM_ADMINISTRATION");
        createPermissionIfNotExists("DASHBOARD_VIEW", "View admin dashboard", "SYSTEM_ADMINISTRATION");
    }
    
    private void createPermissionIfNotExists(String name, String description, String category) {
        if (!permissionRepository.existsByName(name)) {
            Permission permission = new Permission(name, description, category);
            permissionRepository.save(permission);
        }
    }
    
    private void validatePermission(Permission permission) {
        if (permission.getName() == null || permission.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Permission name cannot be empty");
        }
        
        // Validate permission name format (uppercase letters and underscores only)
        if (!permission.getName().matches("^[A-Z_]+$")) {
            throw new IllegalArgumentException("Permission name must contain only uppercase letters and underscores");
        }
        
        if (permission.getCategory() == null || permission.getCategory().trim().isEmpty()) {
            throw new IllegalArgumentException("Permission category cannot be empty");
        }
    }
}
