# Role-Based Access Control (RBAC) Guide

## Overview

The Core Authentication Library now includes a comprehensive Role-Based Access Control (RBAC) system that provides fine-grained permission management for your Spring Boot applications. This system allows you to:

- Define granular permissions for specific actions
- Group permissions into logical roles
- Assign multiple roles to users
- Grant additional direct permissions to individual users
- Use annotations for method-level security
- Manage everything through a web-based admin interface

## 🏗️ Architecture

### Core Components

```
User ←→ Roles ←→ Permissions
 ↓
Direct Permissions
```

- **Users**: Application users who need access to resources
- **Roles**: Collections of permissions that represent job functions (e.g., ADMIN, MODERATOR, USER)
- **Permissions**: Granular access rights for specific actions (e.g., USER_CREATE, REPORT_VIEW)
- **Direct Permissions**: Additional permissions granted directly to users outside of their roles

### Database Schema

The RBAC system adds the following tables to your database:

```sql
-- Core entities
permissions (id, name, description, category, created_at, updated_at)
roles (id, name, description, created_at, updated_at)

-- Junction tables for many-to-many relationships
role_permissions (role_id, permission_id)
user_roles (user_id, role_id)  
user_permissions (user_id, permission_id)  -- Direct user permissions
```

## 🚀 Quick Start

### 1. Update Your Dependency

```gradle
implementation("com.control:core-auth-starter:1.0.20")
```

### 2. Database Setup

The RBAC tables are created automatically when your application starts. No manual migration is required.

### 3. Access the Admin Interface

Navigate to `/admin/roles` and `/admin/permissions` to manage your RBAC system through the web interface.

## 📚 Concepts

### Permissions

Permissions represent specific actions in your application. They follow a naming convention using uppercase letters and underscores:

```java
// Examples of permissions
USER_CREATE     // Can create new users
USER_VIEW       // Can view user information  
USER_UPDATE     // Can modify user details
USER_DELETE     // Can delete users
REPORT_GENERATE // Can generate reports
DASHBOARD_VIEW  // Can view admin dashboard
```

### Permission Categories

Permissions are organized into categories for better management:

- **USER_MANAGEMENT**: User-related operations
- **ROLE_MANAGEMENT**: Role and permission management
- **SYSTEM_MONITORING**: System health and metrics
- **PERMISSION_MANAGEMENT**: Permission administration
- **SYSTEM_ADMINISTRATION**: General admin functions

### Roles

Roles are collections of permissions that represent job functions:

```java
// Default roles provided
ADMIN      // Full system access - all permissions
USER       // Basic access - dashboard view, health check
MODERATOR  // Intermediate access - user viewing, basic monitoring
```

### Permission Inheritance

Users receive permissions through two mechanisms:

1. **Role-based permissions**: Automatically granted through assigned roles
2. **Direct permissions**: Additional permissions granted specifically to the user

The effective permissions for a user are the union of both sources.

## 🔐 Using RBAC in Your Code

### Method-Level Security

#### Using Built-in Annotations

```java
@RestController
public class UserController {
    
    // Require specific permission
    @PreAuthorize("@authorizationService.hasPermission(authentication, 'USER_CREATE')")
    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody CreateUserRequest request) {
        // Implementation
    }
    
    // Require any of multiple permissions
    @PreAuthorize("@authorizationService.hasAnyPermission(authentication, 'USER_VIEW', 'USER_UPDATE')")
    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        // Implementation
    }
    
    // Admin-only access
    @PreAuthorize("@authorizationService.canAccessAdmin(authentication)")
    @GetMapping("/admin/dashboard")
    public String adminDashboard() {
        // Implementation
    }
}
```

#### Using Custom Annotations

```java
@RestController
public class ReportController {
    
    // Simple permission check
    @RequirePermission("REPORT_GENERATE")
    @PostMapping("/reports")
    public ResponseEntity<Report> generateReport() {
        // Implementation
    }
    
    // Admin-only endpoint
    @AdminOnly
    @DeleteMapping("/reports/{id}")
    public ResponseEntity<Void> deleteReport(@PathVariable Long id) {
        // Implementation
    }
    
    // Owner or admin access
    @RequireOwnership
    @GetMapping("/reports/user/{userId}")
    public ResponseEntity<List<Report>> getUserReports(@PathVariable Long userId) {
        // Implementation
    }
}
```

### Programmatic Permission Checks

```java
@Service
public class BusinessService {
    
    private final AuthorizationService authorizationService;
    
    public void performSensitiveOperation(Authentication auth) {
        // Check single permission
        if (authorizationService.hasPermission(auth, "SENSITIVE_OPERATION")) {
            // Perform operation
        }
        
        // Check multiple permissions (ANY)
        if (authorizationService.hasAnyPermission(auth, "PERM_A", "PERM_B")) {
            // User has at least one permission
        }
        
        // Check multiple permissions (ALL)
        if (authorizationService.hasAllPermissions(auth, "PERM_A", "PERM_B")) {
            // User has all permissions
        }
        
        // Check role
        if (authorizationService.hasRole(auth, "ADMIN")) {
            // User has admin role
        }
        
        // Check if user can access admin functions
        if (authorizationService.canAccessAdmin(auth)) {
            // User can access admin areas
        }
    }
}
```

### Working with User Permissions

```java
@Service
public class UserPermissionService {
    
    public void checkUserPermissions(User user) {
        // Get all effective permissions (roles + direct)
        Set<Permission> allPermissions = user.getAllPermissions();
        
        // Get just permission names
        Set<String> permissionNames = user.getPermissionNames();
        
        // Check if user has specific permission
        boolean canCreateUsers = user.hasPermission("USER_CREATE");
        
        // Get role-based permissions
        Set<Permission> rolePermissions = user.getRoles()
            .stream()
            .flatMap(role -> role.getPermissions().stream())
            .collect(Collectors.toSet());
            
        // Get direct permissions
        Set<Permission> directPermissions = user.getDirectPermissions();
    }
}
```

## 🎛️ Admin Interface

### Role Management (`/admin/roles`)

- **View all roles** with their assigned permissions
- **Create new roles** with custom names and descriptions
- **Edit role permissions** using a user-friendly interface
- **Delete roles** (with safeguards against roles in use)

### Permission Management (`/admin/permissions`)

- **View all permissions** organized by category
- **Create new permissions** with proper naming conventions
- **Manage categories** (create, rename, delete)
- **Delete permissions** (with dependency checking)

### User Permission Management (`/admin/users/{id}/permissions`)

- **View effective permissions** from roles and direct assignments
- **Assign direct permissions** to individual users
- **Clear explanation** of permission sources
- **Bulk permission updates**

## 🔧 Configuration

### Default Permissions

The system initializes with these default permissions:

```java
// User Management
USER_VIEW, USER_CREATE, USER_UPDATE, USER_DELETE, USER_PERMISSION_MANAGEMENT

// Role Management  
ROLE_VIEW, ROLE_CREATE, ROLE_UPDATE, ROLE_DELETE, ROLE_PERMISSION_MANAGEMENT

// System Monitoring
ACTUATOR_HEALTH, ACTUATOR_METRICS, ACTUATOR_INFO, ACTUATOR_MAPPINGS, ACTUATOR_BEANS, ACTUATOR_ENV

// Permission Management
PERMISSION_VIEW, PERMISSION_CREATE, PERMISSION_UPDATE, PERMISSION_DELETE

// System Administration
SYSTEM_ADMIN, DASHBOARD_VIEW
```

### Default Roles

- **ADMIN**: Gets all permissions
- **USER**: Gets `DASHBOARD_VIEW` and `ACTUATOR_HEALTH`
- **MODERATOR**: Gets user viewing and basic monitoring permissions

### Admin User Configuration

Configure an admin user in your `application.properties`:

```properties
# Admin user setup
core.auth.admin.enabled=true
core.auth.admin.username=admin
core.auth.admin.password=admin123
core.auth.admin.email=admin@example.com
```

## 🎯 Best Practices

### Permission Naming

- Use **UPPERCASE** with **UNDERSCORES**
- Follow pattern: `RESOURCE_ACTION` (e.g., `USER_CREATE`, `REPORT_DELETE`)
- Be specific and descriptive
- Group related permissions with common prefixes

### Role Design

- **Principle of least privilege**: Only grant necessary permissions
- **Role-based on job functions**: ADMIN, MANAGER, EMPLOYEE, etc.
- **Avoid role explosion**: Don't create too many granular roles
- **Use direct permissions** for exceptional cases

### Security Implementation

```java
// ✅ Good: Specific permission checks
@PreAuthorize("@authorizationService.hasPermission(authentication, 'USER_DELETE')")
public void deleteUser(Long userId) { ... }

// ❌ Avoid: Overly broad role checks  
@PreAuthorize("hasRole('ADMIN')")
public void deleteUser(Long userId) { ... }

// ✅ Good: Multiple permission options
@PreAuthorize("@authorizationService.hasAnyPermission(authentication, 'USER_VIEW', 'USER_UPDATE')")
public User getUser(Long userId) { ... }
```

### Category Organization

Organize permissions into logical categories:

```java
USER_MANAGEMENT:     USER_*, PROFILE_*
CONTENT_MANAGEMENT:  POST_*, COMMENT_*, ARTICLE_*
REPORTING:          REPORT_*, ANALYTICS_*
SYSTEM:             BACKUP_*, MAINTENANCE_*
```

## 🔍 Troubleshooting

### Common Issues

1. **Permission denied errors**
   - Check user has required permission: `/admin/users/{id}/permissions`
   - Verify permission exists: `/admin/permissions`
   - Check role assignments: `/admin/roles`

2. **Admin interface not accessible**
   - Ensure admin user is configured and enabled
   - Check user has `SYSTEM_ADMIN` or `DASHBOARD_VIEW` permission

3. **Database errors on startup**
   - Verify database supports the schema changes
   - Check for naming conflicts with existing tables

### Debug Permission Issues

```java
@Service 
public class PermissionDebugService {
    
    public void debugUserPermissions(Authentication auth) {
        User user = userService.findByUsername(auth.getName()).orElse(null);
        if (user != null) {
            System.out.println("User: " + user.getUsername());
            System.out.println("Roles: " + user.getRoles().stream()
                .map(Role::getName).collect(Collectors.toList()));
            System.out.println("All Permissions: " + user.getPermissionNames());
            System.out.println("Direct Permissions: " + user.getDirectPermissions().stream()
                .map(Permission::getName).collect(Collectors.toList()));
        }
    }
}
```

## 🔗 API Reference

### AuthorizationService Methods

| Method | Description | Parameters |
|--------|-------------|------------|
| `hasPermission(auth, permission)` | Check single permission | Authentication, String |
| `hasAnyPermission(auth, permissions...)` | Check any of multiple permissions | Authentication, String... |
| `hasAllPermissions(auth, permissions...)` | Check all permissions required | Authentication, String... |
| `hasRole(auth, role)` | Check role membership | Authentication, String |
| `isOwnerOrAdmin(auth, userId)` | Check ownership or admin | Authentication, Long |
| `canAccessAdmin(auth)` | Check admin access | Authentication |
| `canManageUsers(auth)` | Check user management rights | Authentication |
| `canViewSystemMonitoring(auth)` | Check monitoring access | Authentication |

### Custom Annotations

| Annotation | Description | Usage |
|------------|-------------|-------|
| `@RequirePermission("PERM_NAME")` | Require specific permission | Method level |
| `@AdminOnly` | Admin-only access | Method/Class level |
| `@RequireOwnership` | Owner or admin access | Method level |

## 📈 Migration from Simple Roles

If you're upgrading from the basic role system:

1. **Existing users keep their roles** - no data loss
2. **Role field remains** for backward compatibility  
3. **New RBAC system is additive** - existing code continues to work
4. **Gradually migrate** to permission-based checks

```java
// Old approach (still works)
@PreAuthorize("hasRole('ADMIN')")
public void adminFunction() { ... }

// New approach (recommended)
@PreAuthorize("@authorizationService.hasPermission(authentication, 'ADMIN_FUNCTION')")
public void adminFunction() { ... }
```

This allows for a smooth transition while gaining the benefits of fine-grained permissions.
