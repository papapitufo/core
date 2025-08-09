package com.control.core.service;

import com.control.core.model.User;
import com.control.core.model.Permission;
import com.control.core.model.Role;
import com.control.core.repository.UserRepository;
import com.control.core.repository.RoleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private PermissionService permissionService;
    
    @Autowired
    private RoleService roleService;
    
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }
    
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    public User createUser(String username, String password, String email, String role) {
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already exists");
        }
        
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setRole(role);
        
        return userRepository.save(user);
    }
    
    public User updateUser(Long id, String username, String email, String role, Boolean enabled) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        if (username != null && !username.equals(user.getUsername())) {
            if (userRepository.existsByUsername(username)) {
                throw new RuntimeException("Username already exists");
            }
            user.setUsername(username);
        }
        
        if (email != null && !email.equals(user.getEmail())) {
            if (userRepository.existsByEmail(email)) {
                throw new RuntimeException("Email already exists");
            }
            user.setEmail(email);
        }
        
        if (role != null) {
            user.setRole(role);
        }
        
        if (enabled != null) {
            user.setEnabled(enabled);
        }
        
        return userRepository.save(user);
    }
    
    public void changePassword(Long id, String newPassword) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
    
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(id);
    }
    
    // Permission-related methods
    
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
    
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    /**
     * Add a direct permission to a user
     */
    public void addDirectPermission(Long userId, String permissionName) {
        User user = findById(userId)
            .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
        
        Permission permission = permissionService.findByName(permissionName)
            .orElseThrow(() -> new EntityNotFoundException("Permission not found: " + permissionName));
        
        user.getDirectPermissions().add(permission);
        userRepository.save(user);
    }
    
    /**
     * Remove a direct permission from a user
     */
    public void removeDirectPermission(Long userId, String permissionName) {
        User user = findById(userId)
            .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
        
        user.getDirectPermissions().removeIf(p -> p.getName().equals(permissionName));
        userRepository.save(user);
    }
    
    /**
     * Update user's direct permissions
     */
    public void updateDirectPermissions(Long userId, List<Long> permissionIds) {
        User user = findById(userId)
            .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
        
        // Clear existing direct permissions
        user.getDirectPermissions().clear();
        
        // Add new permissions
        if (permissionIds != null && !permissionIds.isEmpty()) {
            for (Long permissionId : permissionIds) {
                Permission permission = permissionService.findById(permissionId)
                    .orElseThrow(() -> new EntityNotFoundException("Permission not found with id: " + permissionId));
                user.getDirectPermissions().add(permission);
            }
        }
        
        userRepository.save(user);
    }
    
    /**
     * Check if user has a specific direct permission
     */
    public boolean hasDirectPermission(Long userId, String permissionName) {
        User user = findById(userId)
            .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
        
        return user.getDirectPermissions().stream()
            .anyMatch(p -> p.getName().equals(permissionName));
    }
    
    /**
     * Get all effective permissions for a user (roles + direct)
     */
    public Set<String> getEffectivePermissions(Long userId) {
        User user = findById(userId)
            .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
        
        return user.getPermissionNames();
    }
    
    /**
     * Add a role to a user
     */
    public void addRoleToUser(Long userId, String roleName) {
        User user = findById(userId)
            .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
        
        Role role = roleService.findByName(roleName)
            .orElseThrow(() -> new EntityNotFoundException("Role not found: " + roleName));
        
        user.getRoles().add(role);
        userRepository.save(user);
    }
    
    /**
     * Remove a role from a user
     */
    public void removeRoleFromUser(Long userId, String roleName) {
        User user = findById(userId)
            .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
        
        user.getRoles().removeIf(r -> r.getName().equals(roleName));
        userRepository.save(user);
    }
    
    /**
     * Update user with role assignments
     */
    public User updateUserWithRoles(Long id, String username, String email, String role, Boolean enabled, Set<Long> roleIds) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        if (username != null && !username.equals(user.getUsername())) {
            if (userRepository.existsByUsername(username)) {
                throw new RuntimeException("Username already exists");
            }
            user.setUsername(username);
        }
        
        if (email != null && !email.equals(user.getEmail())) {
            if (userRepository.existsByEmail(email)) {
                throw new RuntimeException("Email already exists");
            }
            user.setEmail(email);
        }
        
        if (role != null) {
            user.setRole(role);
        }
        
        if (enabled != null) {
            user.setEnabled(enabled);
        }
        
        // Update roles if provided
        if (roleIds != null) {
            Set<Role> newRoles = new HashSet<>();
            for (Long roleId : roleIds) {
                roleRepository.findById(roleId).ifPresent(newRoles::add);
            }
            user.setRoles(newRoles);
        }
        
        return userRepository.save(user);
    }
}
