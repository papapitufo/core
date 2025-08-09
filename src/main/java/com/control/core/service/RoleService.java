package com.control.core.service;

import com.control.core.model.Role;
import com.control.core.model.Permission;
import com.control.core.repository.RoleRepository;
import com.control.core.repository.PermissionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class RoleService {
    
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    
    public RoleService(RoleRepository roleRepository, PermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }
    
    public List<Role> findAll() {
        return roleRepository.findAll();
    }
    
    public List<Role> findAllWithPermissions() {
        return roleRepository.findAllWithPermissions();
    }
    
    public Optional<Role> findById(Long id) {
        return roleRepository.findById(id);
    }
    
    public Optional<Role> findByIdWithPermissions(Long id) {
        return roleRepository.findByIdWithPermissions(id);
    }
    
    public Optional<Role> findByName(String name) {
        return roleRepository.findByName(name);
    }
    
    public Optional<Role> findByNameWithPermissions(String name) {
        return roleRepository.findByNameWithPermissions(name);
    }
    
    public Role save(Role role) {
        validateRole(role);
        return roleRepository.save(role);
    }
    
    public Role create(String name, String description) {
        if (roleRepository.existsByName(name)) {
            throw new IllegalArgumentException("Role with name '" + name + "' already exists");
        }
        
        Role role = new Role(name, description);
        return roleRepository.save(role);
    }
    
    public Role update(Long id, String name, String description) {
        Role role = roleRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Role not found with id: " + id));
        
        // Check if name is being changed and if it would create a conflict
        if (!role.getName().equals(name) && roleRepository.existsByName(name)) {
            throw new IllegalArgumentException("Role with name '" + name + "' already exists");
        }
        
        role.setName(name);
        role.setDescription(description);
        
        return roleRepository.save(role);
    }
    
    public void delete(Long id) {
        Role role = roleRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Role not found with id: " + id));
        
        // Check if role is in use
        if (!role.getUsers().isEmpty()) {
            throw new IllegalStateException("Cannot delete role '" + role.getName() + 
                "' because it is assigned to users");
        }
        
        roleRepository.delete(role);
    }
    
    public void addPermissionToRole(Long roleId, Long permissionId) {
        Role role = roleRepository.findByIdWithPermissions(roleId)
            .orElseThrow(() -> new EntityNotFoundException("Role not found with id: " + roleId));
        
        Permission permission = permissionRepository.findById(permissionId)
            .orElseThrow(() -> new EntityNotFoundException("Permission not found with id: " + permissionId));
        
        role.getPermissions().add(permission);
        roleRepository.save(role);
    }
    
    public void removePermissionFromRole(Long roleId, Long permissionId) {
        Role role = roleRepository.findByIdWithPermissions(roleId)
            .orElseThrow(() -> new EntityNotFoundException("Role not found with id: " + roleId));
        
        role.getPermissions().removeIf(p -> p.getId().equals(permissionId));
        roleRepository.save(role);
    }
    
    public void updateRolePermissions(Long roleId, Set<Long> permissionIds) {
        Role role = roleRepository.findByIdWithPermissions(roleId)
            .orElseThrow(() -> new EntityNotFoundException("Role not found with id: " + roleId));
        
        // Clear existing permissions
        role.getPermissions().clear();
        
        // Add new permissions
        if (permissionIds != null && !permissionIds.isEmpty()) {
            for (Long permissionId : permissionIds) {
                Permission permission = permissionRepository.findById(permissionId)
                    .orElseThrow(() -> new EntityNotFoundException("Permission not found with id: " + permissionId));
                role.getPermissions().add(permission);
            }
        }
        
        roleRepository.save(role);
    }
    
    public void initializeDefaultRoles() {
        if (roleRepository.count() == 0) {
            createDefaultRoles();
        }
    }
    
    private void createDefaultRoles() {
        // Create basic roles
        createRoleIfNotExists("ADMIN", "System Administrator");
        createRoleIfNotExists("USER", "Regular User");
        createRoleIfNotExists("MODERATOR", "Content Moderator");
    }
    
    private void createRoleIfNotExists(String name, String description) {
        if (!roleRepository.existsByName(name)) {
            Role role = new Role(name, description);
            roleRepository.save(role);
        }
    }
    
    private void validateRole(Role role) {
        if (role.getName() == null || role.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Role name cannot be empty");
        }
        
        // Validate role name format (letters and underscores only)
        if (!role.getName().matches("^[A-Z_]+$")) {
            throw new IllegalArgumentException("Role name must contain only uppercase letters and underscores");
        }
    }
}
