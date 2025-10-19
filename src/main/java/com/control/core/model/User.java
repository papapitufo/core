package com.control.core.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
public class User implements UserDetails {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;
    
    @Column(nullable = false)
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;
    
    @Column(unique = true, nullable = false)
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    private String email;
    
    @Column(nullable = false)
    private String role = "USER";
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_permissions",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<Permission> directPermissions = new HashSet<>();
    
    @Column(nullable = false)
    private boolean enabled = true;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column(name = "last_login")
    private LocalDateTime lastLogin;
    
    // Constructors
    public User() {}
    
    public User(String username, String password, String email, String role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }
    
    // UserDetails implementation
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        
        // Add role-based authorities
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
        
        // Add authorities from Role entities
        if (roles != null) {
            roles.forEach(roleEntity -> {
                authorities.add(new SimpleGrantedAuthority("ROLE_" + roleEntity.getName()));
                // Add permissions from roles
                if (roleEntity.getPermissions() != null) {
                    roleEntity.getPermissions().forEach(permission -> 
                        authorities.add(new SimpleGrantedAuthority(permission.getName()))
                    );
                }
            });
        }
        
        // Add direct permissions
        if (directPermissions != null) {
            directPermissions.forEach(permission -> 
                authorities.add(new SimpleGrantedAuthority(permission.getName()))
            );
        }
        
        return authorities;
    }
    
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    
    @Override
    public boolean isEnabled() {
        return enabled;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getLastLogin() {
        return lastLogin;
    }
    
    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }
    
    public Set<Role> getRoles() {
        return roles;
    }
    
    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
    
    public Set<Permission> getDirectPermissions() {
        return directPermissions;
    }
    
    public void setDirectPermissions(Set<Permission> directPermissions) {
        this.directPermissions = directPermissions;
    }
    
    /**
     * Get all permissions for this user (from roles + direct permissions)
     */
    public Set<Permission> getAllPermissions() {
        Set<Permission> allPermissions = new HashSet<>();
        
        // Add permissions from roles
        if (roles != null) {
            roles.forEach(roleEntity -> {
                if (roleEntity.getPermissions() != null) {
                    allPermissions.addAll(roleEntity.getPermissions());
                }
            });
        }
        
        // Add direct permissions
        if (directPermissions != null) {
            allPermissions.addAll(directPermissions);
        }
        
        return allPermissions;
    }
    
    /**
     * Check if user has a specific permission (through roles or direct assignment)
     */
    public boolean hasPermission(String permissionName) {
        return getAllPermissions().stream()
            .anyMatch(permission -> permission.getName().equals(permissionName));
    }
    
    /**
     * Get all permission names for this user
     */
    public Set<String> getPermissionNames() {
        return getAllPermissions().stream()
            .map(Permission::getName)
            .collect(Collectors.toSet());
    }
}
