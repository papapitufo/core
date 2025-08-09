# Core Auth Starter

A comprehensive Spring Boot Starter for authentication and user management with a beautiful Material UI frontend.

[![Version](https://img.shields.io/badge/version-1.0.22-blue.svg)](https://github.com/papapitufo/core/packages)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.3-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17+-orange.svg)](https://openjdk.java.net/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

## 🚀 Features

🔐 **Complete Authentication System**
- Login/Logout functionality with username or email
- User registration with comprehensive validation
- Password reset with secure email verification
- Advanced Role-Based Access Control (RBAC) with fine-grained permissions
- Session management and security

🛡️ **Advanced RBAC System** ⭐ **NEW**
- **Granular Permissions**: Define specific actions like `USER_CREATE`, `REPORT_VIEW`, `SYSTEM_ADMIN`
- **Role Management**: Create custom roles with permission sets (ADMIN, MODERATOR, USER, etc.)
- **Direct User Permissions**: Grant additional permissions to individual users
- **Permission Categories**: Organize permissions by function (User Management, System Monitoring, etc.)
- **Annotation-Based Security**: Use `@RequirePermission`, `@AdminOnly`, `@RequireOwnership` annotations
- **Web-Based Admin Interface**: Manage roles, permissions, and user assignments through intuitive UI

👥 **Advanced User Management**
- Admin dashboard for complete user lifecycle management
- **Permission Assignment**: Grant direct permissions to users beyond their roles
- Create, activate, deactivate, and delete users
- Real-time user search and filtering
- User activity tracking and last login timestamps
- **Role Assignment**: Assign multiple roles to users with inheritance
- Bulk operations support

🎨 **Professional Material UI Frontend**
- Responsive design that works on all devices
- **New Admin Interfaces**: Role management, permission management, and category organization
- Consistent 4px border-radius styling throughout
- Modal dialogs with real-time validation feedback
- CSRF protection built-in
- Loading states and user feedback

📧 **Flexible Email Services**
- Support for Gmail, SendGrid, MailHog, and custom SMTP
- Beautifully designed HTML email templates
- Password reset email workflow
- Configurable email providers with fallback options

⚙️ **Zero-Configuration Setup**
- Spring Boot Auto-Configuration
- Automatic database schema creation with RBAC tables
- Default admin user creation with full permissions
- **Default RBAC Setup**: Pre-configured permissions and roles for immediate use
- Sensible security defaults

## 📦 Installation

> **⚠️ Important:** This starter requires **Spring Boot 3.0+** and **Java 17+**. If your application uses Spring Boot 2.x, please see the [Migration Guide](#migration-from-spring-boot-2x) below.

### For GitHub Packages

Add the repository and dependency to your `build.gradle.kts`:

```kotlin
repositories {
    mavenCentral()
    maven {
        name = "GitHubPackages"
        url = uri("https://maven.pkg.github.com/papapitufo/core")
        credentials {
            username = "your_github_username"
            password = "your_github_token" // Needs read:packages permission
        }
    }
}

```kotlin
dependencies {
    implementation("com.control:core-auth-starter:1.0.22")
    runtimeOnly("org.postgresql:postgresql") // or your preferred database
    
    // Required for email functionality (password reset emails)
    implementation("org.springframework.boot:spring-boot-starter-mail")
}
```
```

### Maven

```xml
<repositories>
    <repository>
        <id>github</id>
        <url>https://maven.pkg.github.com/papapitufo/core</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.control</groupId>
        <artifactId>core-auth-starter</artifactId>
        <version>1.0.14</version>
    </dependency>
    <!-- Required for email functionality (password reset emails) -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-mail</artifactId>
    </dependency>
</dependencies>
```

## 🛠️ Quick Setup

### 1. Add Required Dependencies

**For Email Functionality (Password Reset):**
If you want to use the password reset feature, you must add the mail starter dependency:

```kotlin
// Gradle
dependencies {
    implementation("com.control:core-auth-starter:1.0.22")
    implementation("org.springframework.boot:spring-boot-starter-mail") // Required for email
    runtimeOnly("org.postgresql:postgresql") // or your preferred database
}
```

```xml
<!-- Maven -->
<dependencies>
    <dependency>
        <groupId>com.control</groupId>
        <artifactId>core-auth-starter</artifactId>
        <version>1.0.22</version>
    </dependency>
    <!-- Required for email functionality -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-mail</artifactId>
    </dependency>
</dependencies>
```

**Without Email (Basic Auth Only):**
If you don't need password reset emails, you can skip the mail dependency:

```kotlin
// Gradle - Basic auth without email
dependencies {
    implementation("com.control:core-auth-starter:1.0.22")
    runtimeOnly("org.postgresql:postgresql") // or your preferred database
}
```

### 2. Configure Your Application

### 2. Configure Your Application

Add to your `application.properties`:

```properties
# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/your_db
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update

# Admin User (created automatically on startup)
core.auth.admin.enabled=true
core.auth.admin.username=admin
core.auth.admin.password=admin123
core.auth.admin.email=admin@example.com

# UI Configuration
core.auth.default-success-url=/dashboard
# Other available options:
# core.auth.registration-enabled=true
# core.auth.admin-panel-enabled=true
# core.auth.forgot-password-enabled=true

# Email Configuration (optional - for password reset)
core.auth.email.provider=gmail
core.auth.email.smtp.host=smtp.gmail.com
core.auth.email.smtp.port=587
core.auth.email.username=your-email@gmail.com
core.auth.email.password=your-app-password
```

### 3. Enable Component Scanning

Update your main application class:

```java
@SpringBootApplication
@ComponentScan(basePackages = {"com.yourcompany.yourapp", "com.control.core"})
public class YourApplication {
    public static void main(String[] args) {
        SpringApplication.run(YourApplication.class, args);
    }
}
```

### 4. Run Your Application

That's it! Your application now includes:
- 🔐 Login page at `/login`
- 📝 User registration at `/signup`
- 📊 User dashboard at `/dashboard`
- 👨‍💼 Admin interface at `/admin/users`
- 🔑 Password reset at `/forgot-password`

## 📚 Available Endpoints

| Endpoint | Description | Access Level | Features |
|----------|-------------|--------------|----------|
| `/login` | User login page | Public | Username/email login, remember me |
| `/signup` | User registration | Public | Validation, duplicate prevention |
| `/dashboard` | User dashboard | Authenticated | Profile info, last login |
| `/admin/users` | User management | Admin only | CRUD operations, search, activate/deactivate |
| `/admin/roles` | **Role management** ⭐ **NEW** | Admin only | Create roles, assign permissions, role deletion |
| `/admin/permissions` | **Permission management** ⭐ **NEW** | Admin only | Create permissions, manage categories, delete permissions |
| `/admin/users/{id}/permissions` | **User permissions** ⭐ **NEW** | Admin only | Assign direct permissions, view effective permissions |
| `/admin/actuator` | System monitoring | Admin only | Spring Boot Actuator dashboard |
| `/forgot-password` | Password reset request | Public | Email-based token generation |
| `/reset-password` | Password reset form | Public | Secure token validation |
| `/logout` | User logout | Authenticated | Session cleanup |

## 🛡️ Role-Based Access Control (RBAC)

The Core Auth Starter now includes a comprehensive RBAC system for fine-grained access control. See the complete [**RBAC Guide**](RBAC_GUIDE.md) for detailed documentation.

### 🚀 Quick RBAC Examples

**Method-Level Security with Permissions:**
```java
@RestController
public class UserController {
    
    @PreAuthorize("@authorizationService.hasPermission(authentication, 'USER_CREATE')")
    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody CreateUserRequest request) {
        // Only users with USER_CREATE permission can access
    }
    
    @RequirePermission("USER_DELETE")
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        // Clean annotation-based permission check
    }
    
    @AdminOnly
    @GetMapping("/admin/reports")
    public ResponseEntity<List<Report>> getAdminReports() {
        // Admin-only access
    }
}
```

**Programmatic Permission Checks:**
```java
@Service
public class BusinessService {
    
    private final AuthorizationService authorizationService;
    
    public void performAction(Authentication auth) {
        if (authorizationService.hasPermission(auth, "SPECIAL_ACTION")) {
            // User has permission
        }
        
        if (authorizationService.hasAnyPermission(auth, "PERM_A", "PERM_B")) {
            // User has at least one permission
        }
        
        if (authorizationService.canAccessAdmin(auth)) {
            // User can access admin functions
        }
    }
}
```

### 🎛️ RBAC Admin Interface

Access the new admin interfaces:
- **`/admin/roles`** - Create and manage roles, assign permissions to roles
- **`/admin/permissions`** - Create permissions, organize by categories  
- **`/admin/users/{id}/permissions`** - Assign direct permissions to individual users

### 📊 Default RBAC Setup

The system comes pre-configured with:

**Default Roles:**
- **ADMIN** - Full system access with all permissions
- **USER** - Basic access (dashboard, health checks)
- **MODERATOR** - Intermediate access (user viewing, basic monitoring)

**Permission Categories:**
- **USER_MANAGEMENT** - User CRUD operations, permission management
- **ROLE_MANAGEMENT** - Role CRUD operations, permission assignment
- **SYSTEM_MONITORING** - Actuator endpoints, health checks, metrics
- **PERMISSION_MANAGEMENT** - Permission CRUD operations
- **SYSTEM_ADMINISTRATION** - General admin functions, dashboard access

**Migration from Simple Roles:**
- ✅ **100% Backward Compatible** - Existing role-based code continues to work
- ✅ **Additive Enhancement** - New permission system works alongside existing roles
- ✅ **Gradual Migration** - Migrate to permission-based security at your own pace

For complete RBAC documentation, examples, and best practices, see: **[RBAC_GUIDE.md](RBAC_GUIDE.md)**

## 📊 System Monitoring with Spring Boot Actuator

The Core Auth Starter includes a comprehensive System Monitoring dashboard that integrates with Spring Boot Actuator endpoints. The dashboard provides real-time monitoring capabilities for production applications.

### 🔧 Enabling Actuator in Your Application

**Step 1: Add Actuator Dependency**

```kotlin
// Gradle (build.gradle.kts)
dependencies {
    implementation("com.control:core-auth-starter:1.0.14")
    implementation("org.springframework.boot:spring-boot-starter-actuator") // Add this
    // ... other dependencies
}
```

```xml
<!-- Maven (pom.xml) -->
<dependencies>
    <dependency>
        <groupId>com.control</groupId>
        <artifactId>core-auth-starter</artifactId>
        <version>1.0.14</version>
    </dependency>
    <!-- Add Spring Boot Actuator -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>
</dependencies>
```

**Step 2: Configure Actuator Endpoints**

Add to your `application.properties`:

```properties
# Actuator Configuration
management.endpoints.web.exposure.include=*
management.endpoint.health.show-details=when-authorized
management.endpoint.info.enabled=true

# Application Information (displayed in monitoring dashboard)
info.app.name=Your Application Name
info.app.description=Your application description
info.app.version=1.0.0
info.app.encoding=@project.build.sourceEncoding@
info.app.java.version=@java.version@

# Optional: Customize management port (if different from main app port)
# management.server.port=8081
```

**Step 3: Access the Monitoring Dashboard**

After adding actuator and restarting your application:

1. **Login as admin** to your application
2. **Navigate to the dashboard** (`/dashboard`)
3. **Click "System Monitoring"** card in the admin section
4. **Access comprehensive monitoring** with real-time data

### 🎛️ Monitoring Dashboard Features

The System Monitoring dashboard (`/admin/actuator`) provides:

| Monitoring Card | Endpoint | Description |
|----------------|----------|-------------|
| **Application Health** | `/actuator/health` | Overall application health status and component checks |
| **Application Info** | `/actuator/info` | Application metadata, version, and build information |
| **Environment Properties** | `/actuator/env` | Configuration properties and environment variables |
| **Application Metrics** | `/actuator/metrics` | Performance metrics, JVM stats, and custom metrics |
| **Configuration Properties** | `/actuator/configprops` | All configuration properties and their values |
| **Spring Beans** | `/actuator/beans` | All Spring beans and their dependencies |
| **Request Mappings** | `/actuator/mappings` | All HTTP endpoints and their handlers |
| **Thread Dump** | `/actuator/threaddump` | JVM thread information for performance analysis |
| **Log Levels** | `/actuator/loggers` | View and modify logging levels at runtime |

### 🔒 Security Configuration for Actuator

By default, the Core Auth Starter secures actuator endpoints to admin users only. You can customize this:

**Option 1: Admin-Only Access (Default)**
```java
// No configuration needed - this is the default behavior
// Actuator endpoints at /actuator/* require ADMIN role
```

**Option 2: Custom Actuator Security**
```java
@Configuration
@Order(1) // Higher priority
public class ActuatorSecurityConfig {
    
    @Bean
    public SecurityFilterChain actuatorSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
            .securityMatcher("/actuator/**")
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/actuator/health", "/actuator/info").permitAll() // Public
                .requestMatchers("/actuator/**").hasRole("ADMIN") // Admin only
            )
            .httpBasic(Customizer.withDefaults()) // Enable basic auth for monitoring tools
            .build();
    }
}
```

**Option 3: Monitoring Tool Integration**
```properties
# For external monitoring tools (Prometheus, etc.)
management.endpoints.web.exposure.include=health,info,metrics,prometheus
management.endpoint.health.show-details=always
management.endpoint.prometheus.enabled=true
```

### 📈 Production Monitoring Setup

**Recommended Production Configuration:**

```properties
# Security: Only expose necessary endpoints
management.endpoints.web.exposure.include=health,info,metrics,loggers

# Health endpoint configuration
management.endpoint.health.show-details=when-authorized
management.endpoint.health.probes.enabled=true
management.health.diskspace.enabled=true
management.health.db.enabled=true

# Info endpoint with build information
management.info.build.enabled=true
management.info.env.enabled=true
management.info.git.enabled=true

# Application information
info.app.name=${spring.application.name:Your App}
info.app.description=Production application with Core Auth
info.app.version=@project.version@
info.app.environment=${spring.profiles.active:production}

# Optional: Separate management port for security
management.server.port=8081
management.server.address=127.0.0.1

# Metrics configuration
management.metrics.distribution.percentiles-histogram.http.server.requests=true
management.metrics.distribution.percentiles.http.server.requests=0.5,0.95,0.99
management.metrics.tags.application=${spring.application.name}
```

### 🚨 Troubleshooting Actuator

**Problem: "Whitelabel Error Page" when clicking System Monitoring**

This means actuator endpoints aren't exposed. Add to your `application.properties`:

```properties
# Expose actuator endpoints
management.endpoints.web.exposure.include=*
```

**Problem: Empty or missing application info**

Add application metadata to your `application.properties`:

```properties
# Application information for monitoring dashboard
info.app.name=Your Application Name
info.app.description=Your application description
info.app.version=1.0.0
```

**Problem: Health endpoint shows limited information**

Configure health details visibility:

```properties
# Show detailed health information for authorized users
management.endpoint.health.show-details=when-authorized
# Or for all users (not recommended for production)
# management.endpoint.health.show-details=always
```

**Problem: Actuator endpoints return 404**

Ensure you've added the actuator dependency:

```kotlin
// Gradle
implementation("org.springframework.boot:spring-boot-starter-actuator")
```

```xml
<!-- Maven -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

### 💡 Advanced Actuator Features

**Custom Health Indicators:**
```java
@Component
public class CustomHealthIndicator implements HealthIndicator {
    
    @Override
    public Health health() {
        // Custom health check logic
        boolean isHealthy = checkExternalService();
        
        if (isHealthy) {
            return Health.up()
                .withDetail("service", "Available")
                .withDetail("checked-at", Instant.now())
                .build();
        } else {
            return Health.down()
                .withDetail("service", "Unavailable")
                .withDetail("error", "Connection timeout")
                .build();
        }
    }
}
```

**Custom Application Info:**
```java
@Component
public class CustomInfoContributor implements InfoContributor {
    
    @Override
    public void contribute(Info.Builder builder) {
        builder.withDetail("app", Map.of(
            "name", "My Application",
            "version", "1.0.0",
            "environment", "production",
            "startup-time", Instant.now()
        ));
    }
}
```

**Custom Metrics:**
```java
@RestController
public class MetricsController {
    
    private final MeterRegistry meterRegistry;
    private final Counter customCounter;
    
    public MetricsController(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        this.customCounter = Counter.builder("custom.requests")
            .description("Custom request counter")
            .register(meterRegistry);
    }
    
    @GetMapping("/api/data")
    public ResponseEntity<String> getData() {
        customCounter.increment(); // Track custom metric
        return ResponseEntity.ok("Data response");
    }
}
```

## ⚙️ Configuration Options

### Admin User Settings

```properties
# Enable/disable automatic admin user creation
core.auth.admin.enabled=true

# Admin credentials (change these!)
core.auth.admin.username=admin
core.auth.admin.password=securePassword123
core.auth.admin.email=admin@yourcompany.com
```

### UI and Navigation Settings

```properties
# Default page after successful login (default: /dashboard)
core.auth.default-success-url=/dashboard

# Enable/disable user registration (default: true)
core.auth.registration-enabled=true

# Enable/disable admin panel access (default: true)
core.auth.admin-panel-enabled=true

# Enable/disable forgot password functionality (default: true)
core.auth.forgot-password-enabled=true

# Base URL for email links (used in password reset emails)
core.auth.base-url=http://localhost:8080
```

### Database Support

Works with any JPA-compatible database:

```properties
# PostgreSQL (recommended for production)
spring.datasource.url=jdbc:postgresql://localhost:5432/myapp
spring.datasource.username=postgres
spring.datasource.password=password

# H2 (great for development/testing)
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.h2.console.enabled=true

# MySQL
spring.datasource.url=jdbc:mysql://localhost:3306/myapp
spring.datasource.username=root
spring.datasource.password=password
```

### 📧 Email Provider Configuration

> **⚠️ Important:** Email functionality requires adding `spring-boot-starter-mail` to your dependencies. See [Quick Setup](#1-add-required-dependencies) for details.

#### Gmail Configuration
```properties
core.auth.email.provider=gmail
core.auth.email.smtp.host=smtp.gmail.com
core.auth.email.smtp.port=587
core.auth.email.username=your-email@gmail.com
core.auth.email.password=your-app-password  # Use App Password, not regular password
core.auth.email.from-address=your-email@gmail.com
core.auth.email.from-name=Your App Name
```

#### SendGrid Configuration
```properties
core.auth.email.provider=sendgrid
core.auth.email.smtp.host=smtp.sendgrid.net
core.auth.email.smtp.port=587
core.auth.email.username=apikey
core.auth.email.password=your-sendgrid-api-key
core.auth.email.from-address=noreply@yourcompany.com
core.auth.email.from-name=Your Company
```

#### MailHog (Development)
```properties
core.auth.email.provider=mailhog
core.auth.email.smtp.host=localhost
core.auth.email.smtp.port=1025
core.auth.email.username=
core.auth.email.password=
```

#### Custom SMTP
```properties
core.auth.email.provider=custom
core.auth.email.smtp.host=your-smtp-server.com
core.auth.email.smtp.port=587
core.auth.email.smtp.auth=true
core.auth.email.smtp.starttls=true
core.auth.email.username=your-username
core.auth.email.password=your-password
```

## 🗄️ Database Schema

The starter automatically creates these tables:

### Users Table
```sql
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) DEFAULT 'USER',
    active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_login TIMESTAMP
);
```

### Password Reset Tokens Table
```sql
CREATE TABLE password_reset_tokens (
    id BIGSERIAL PRIMARY KEY,
    token VARCHAR(255) UNIQUE NOT NULL,
    user_id BIGINT REFERENCES users(id),
    expiry_date TIMESTAMP NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

## 📋 Complete Configuration Reference

Below is the complete list of all configuration properties available in the Core Auth Starter:

### 🔧 Core Settings

| Property | Type | Default | Description |
|----------|------|---------|-------------|
| `core.auth.default-success-url` | String | `/dashboard` | URL to redirect to after successful login |
| `core.auth.registration-enabled` | Boolean | `true` | Enable/disable user registration functionality |
| `core.auth.password-reset-enabled` | Boolean | `true` | Enable/disable password reset functionality |
| `core.auth.admin-panel-enabled` | Boolean | `true` | Enable/disable admin panel access |
| `core.auth.forgot-password-enabled` | Boolean | `true` | Enable/disable forgot password feature |
| `core.auth.base-url` | String | `http://localhost:8080` | Base URL for email links and redirects |

### 👤 Admin User Configuration

| Property | Type | Default | Description |
|----------|------|---------|-------------|
| `core.auth.admin.enabled` | Boolean | `true` | Enable automatic admin user creation |
| `core.auth.admin.username` | String | `admin` | Default admin username |
| `core.auth.admin.password` | String | `admin123` | Default admin password ⚠️ Change this! |
| `core.auth.admin.email` | String | `admin@example.com` | Default admin email address |

### 📧 Email Configuration

| Property | Type | Default | Description |
|----------|------|---------|-------------|
| `core.auth.email.enabled` | Boolean | `true` | Enable/disable email functionality |
| `core.auth.email.from-address` | String | `noreply@example.com` | From address for system emails |
| `core.auth.email.from-name` | String | `Core Auth` | From name for system emails |

### 🔒 Security Configuration

| Property | Type | Default | Description |
|----------|------|---------|-------------|
| `core.auth.security.auto-configure` | Boolean | `true` | Enable/disable automatic security configuration |

### 📝 Example Configuration

Here's a complete example configuration for `application.properties`:

```properties
# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/myapp
spring.datasource.username=postgres
spring.datasource.password=password
spring.jpa.hibernate.ddl-auto=update

# Core Auth Settings
core.auth.default-success-url=/home
core.auth.registration-enabled=true
core.auth.admin-panel-enabled=true
core.auth.forgot-password-enabled=true
core.auth.base-url=https://myapp.com

# Admin User (⚠️ Change these in production!)
core.auth.admin.enabled=true
core.auth.admin.username=admin
core.auth.admin.password=MySecurePassword123!
core.auth.admin.email=admin@mycompany.com

# Email Settings
core.auth.email.enabled=true
core.auth.email.from-address=noreply@mycompany.com
core.auth.email.from-name=My Application

# Security
core.auth.security.auto-configure=true

# Spring Mail Configuration (required for email features)
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

### 🚫 Disabling Features

You can disable specific features by setting them to `false`:

```properties
# Disable user registration
core.auth.registration-enabled=false

# Disable admin panel
core.auth.admin-panel-enabled=false

# Disable password reset
core.auth.forgot-password-enabled=false

# Disable email functionality
core.auth.email.enabled=false

# Disable automatic admin user creation
core.auth.admin.enabled=false

# Disable automatic security configuration
core.auth.security.auto-configure=false
```

## 🎨 Customization

### Overriding Templates

Create your own templates in `src/main/resources/templates/`:

- `login.html` - Login page
- `signup.html` - Registration page  
- `dashboard.html` - User dashboard
- `user-management.html` - Admin interface
- `forgot-password.html` - Password reset request
- `reset-password.html` - Password reset form
- `email/password-reset.html` - Password reset email template

### Custom Styling

Override CSS by creating `src/main/resources/static/css/custom.css`:

```css
/* Override Material UI variables */
:root {
    --primary-color: #your-color;
    --border-radius: 8px; /* Change from default 4px */
}

/* Custom button styles */
.btn-primary {
    background-color: var(--primary-color);
}
```

### Security Customization

The easiest way to add your own security rules is to use a different bean method name:

```java
@Configuration
@EnableWebSecurity
public class YourSecurityConfig {
    
    @Bean
    public SecurityFilterChain businessSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
            .authorizeHttpRequests(auth -> auth
                // Your business endpoints
                .requestMatchers("/api/**").authenticated()
                .requestMatchers("/admin/custom/**").hasRole("ADMIN")
                .anyRequest().permitAll()
            )
            .formLogin(form -> form.defaultSuccessUrl("/"))
            .build();
    }
}
```

Core Auth will handle its own endpoints (`/login`, `/admin/users`, etc.) while your configuration handles your business logic endpoints.

## 🚀 Advanced Configuration

### Security Configuration

The starter provides flexible security configuration options:

```properties
# Security auto-configuration
core.auth.security.auto-configure=true  # Enable/disable automatic security setup

# Feature toggles
core.auth.registration.enabled=true
core.auth.password-reset.enabled=true
core.auth.admin-panel.enabled=true
core.auth.user-dashboard.enabled=true
```

### Custom Security Configuration

If you need custom security rules, you have several approaches:

**Approach 1: Use Different Bean Names (Recommended)**

Keep both Core Auth security and your custom security by using different bean method names:

```java
@Configuration
@EnableWebSecurity
public class YourSecurityConfig {
    
    @Bean
    public SecurityFilterChain businessSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
            .authorizeHttpRequests(auth -> auth
                // Your business-specific endpoints
                .requestMatchers("/blog/manager/**").authenticated()
                .requestMatchers("/api/**", "/actuator/**").authenticated()
                .anyRequest().permitAll()
            )
            .formLogin(form -> form.defaultSuccessUrl("/", true))
            .logout(logout -> logout.deleteCookies("JSESSIONID"))
            .rememberMe(remember -> remember.key("secret-key"))
            .sessionManagement(session -> session.maximumSessions(1))
            .httpBasic(httpBasic -> {})
            .csrf(csrf -> csrf.disable())
            .build();
    }
}
```

**Benefits:**
- ✅ Core Auth handles authentication UI (`/login`, `/signup`, `/dashboard`, `/admin/**`)
- ✅ Your config handles business endpoints (`/api/**`, `/blog/**`, etc.)
- ✅ Spring Security automatically merges configurations
- ✅ Zero configuration needed for Core Auth endpoints

**Approach 2: Disable Auto-Configuration and Define Your Own**
**Approach 2: Disable Auto-Configuration and Define Your Own**
```properties
core.auth.security.auto-configure=false
```

Then create your own security configuration:
```java
@Configuration
@EnableWebSecurity
public class CustomSecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            .authorizeHttpRequests(auth -> auth
                // Core Auth endpoints (you need to configure these manually)
                .requestMatchers("/login", "/signup", "/css/**", "/js/**").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/dashboard").authenticated()
                
                // Your business endpoints
                .requestMatchers("/api/**").authenticated()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/dashboard")
            )
            .build();
    }
}
```

**Approach 3: Use Core Auth Security with Additional Rules**
Keep the auto-configuration enabled and add additional security rules:
```java
@Configuration
public class AdditionalSecurityConfig {
    
    @Bean
    @Order(1) // Higher priority than Core Auth security
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
            .securityMatcher("/api/**")
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/public/**").permitAll()
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .httpBasic(Customizer.withDefaults())
            .build();
    }
}
```

### Feature Toggles
```properties
# Password policy
core.auth.password.min-length=8
core.auth.password.require-uppercase=true
core.auth.password.require-lowercase=true
core.auth.password.require-numbers=true
core.auth.password.require-special-chars=false

# Session management
core.auth.session.max-sessions=1
core.auth.session.timeout=30m
core.auth.remember-me.enabled=true
core.auth.remember-me.validity=14d
```

## 📋 Requirements

- **Java**: 17 or higher
- **Spring Boot**: 3.0.0+ (minimum), 3.5.3+ (recommended)
- **Spring Security**: 6.0.0+ (comes with Spring Boot 3.x)
- **Database**: Any JPA-compatible database (PostgreSQL, H2, MySQL, etc.)
- **Build Tool**: Gradle 8+ or Maven 3.6+

### ⚠️ Version Compatibility

**This starter is built for Spring Boot 3.x and is NOT compatible with Spring Boot 2.x**

| Core Auth Starter | Spring Boot | Spring Security | Java | Notes |
|-------------------|-------------|-----------------|------|-------|
| 1.0.x - 1.x.x | 3.0.0 - 3.5.x+ | 6.0.0+ | 17+ | **Current supported version** |
| ❌ Not supported | 2.x.x | 5.x.x | 8-16 | Use Spring Boot 3.x instead |

### Migration from Spring Boot 2.x

If your application is currently using Spring Boot 2.x, you'll need to upgrade to Spring Boot 3.x to use this starter. Here's what you need to do:

#### 1. Update Your Application Dependencies

**Gradle (`build.gradle.kts`):**
```kotlin
plugins {
    id("org.springframework.boot") version "3.2.0" // or later
    id("io.spring.dependency-management") version "1.1.4"
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17) // Minimum Java 17
    }
}
```

**Maven (`pom.xml`):**
```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.2.0</version> <!-- or later -->
    <relativePath/>
</parent>

<properties>
    <java.version>17</java.version> <!-- Minimum Java 17 -->
</properties>
```

#### 2. Package Migration (javax → jakarta)

Spring Boot 3.x uses Jakarta EE instead of Java EE. Update your imports:

```java
// OLD (Spring Boot 2.x)
import javax.persistence.*;
import javax.servlet.*;
import javax.validation.*;

// NEW (Spring Boot 3.x)
import jakarta.persistence.*;
import jakarta.servlet.*;
import jakarta.validation.*;
```

#### 3. Security Configuration Updates

If you have custom security configuration, update from deprecated patterns:

```java
// OLD (Spring Boot 2.x with WebSecurityConfigurerAdapter)
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()...
    }
}

// NEW (Spring Boot 3.x with SecurityFilterChain)
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            .authorizeHttpRequests(auth -> auth...)
            .build();
    }
}
```

#### 4. Common Migration Issues

**Thymeleaf Security Integration:**
```kotlin
// Update dependency
implementation("org.thymeleaf.extras:thymeleaf-extras-springsecurity6") // not springsecurity5
```

**Repository Configuration:**
```java
// Usually no changes needed, but ensure you're using JPA 3.0+ annotations
```

### Quick Compatibility Check

Run this command to check your current Spring Boot version:

```bash
# Gradle
./gradlew dependencies | grep spring-boot-starter

# Maven  
mvn dependency:tree | grep spring-boot-starter
```

If you see version 2.x.x, you need to upgrade to Spring Boot 3.x before using this starter.

## �️ Route Security Configuration FAQ

### How do I make routes public (accessible without authentication)?

Routes that are **not** in the starter's security matcher are automatically public. The starter only secures these specific paths:
- `/login`, `/logout`, `/signup`, `/forgot-password`, `/reset-password`
- `/admin/**` 
- `/dashboard`

**Example: Creating public routes**
```java
@Controller
public class PublicController {
    
    @GetMapping("/pictures")
    public String pictures() {
        return "pictures"; // Automatically public - no authentication required
    }
    
    @GetMapping("/about")
    public String about() {
        return "about"; // Automatically public
    }
    
    @GetMapping("/gallery/{id}")
    public String galleryItem(@PathVariable String id, Model model) {
        model.addAttribute("imageId", id);
        return "gallery-item"; // Automatically public
    }
}
```

### How do I secure routes behind authentication?

For routes you want to protect, create a security configuration in your consumer application:

**Option 1: Simple Authentication (Any logged-in user)**
```java
@Configuration
@Order(2) // Lower priority than the starter's @Order(1)
public class AppSecurityConfig {
    
    @Bean
    public SecurityFilterChain appSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
            .securityMatcher("/protected/**", "/private/**", "/user/**")
            .authorizeHttpRequests(authz -> authz
                .anyRequest().authenticated() // Require authentication
            )
            .build();
    }
}
```

**Option 2: Role-Based Access Control**
```java
@Configuration
@Order(2)
public class AppSecurityConfig {
    
    @Bean
    public SecurityFilterChain appSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
            .securityMatcher("/protected/**", "/admin-stuff/**", "/user/**")
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/admin-stuff/**").hasRole("ADMIN")
                .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers("/protected/**").authenticated()
                .anyRequest().authenticated()
            )
            .build();
    }
}
```

**Option 3: Method-Level Security**
```java
@SpringBootApplication
@EnableMethodSecurity(prePostEnabled = true)
public class YourApplication {
    public static void main(String[] args) {
        SpringApplication.run(YourApplication.class, args);
    }
}

@Controller
public class SecureController {
    
    @GetMapping("/protected/photos")
    @PreAuthorize("isAuthenticated()")
    public String protectedPhotos() {
        return "protected-photos";
    }
    
    @GetMapping("/admin/settings")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminSettings() {
        return "admin-settings";
    }
    
    @GetMapping("/user/profile")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public String userProfile() {
        return "user-profile";
    }
}
```

### What happens if I don't configure security for my routes?

- **Routes NOT in the starter's security matcher**: Completely public, no authentication required
- **Routes in the starter's security matcher**: Protected by the starter's security configuration

**Starter's Protected Paths:**
```java
// These paths are automatically secured by the starter:
"/login", "/logout", "/signup", "/forgot-password", "/reset-password", 
"/admin/**", "/dashboard"

// All other paths (including /api/**) are public by default
```

### Can I override the starter's security configuration?

Yes, you have several options:

1. **Disable auto-configuration** and define your own:
```properties
core.auth.security.auto-configure=false
```

2. **Use different security matcher patterns** in your own configuration (recommended approach above)

3. **Use higher priority** security filter chains with `@Order(0)` or `@Order(1)`

### How do I check what security is applied to my routes?

Enable debug logging to see security decisions:

```properties
logging.level.org.springframework.security=DEBUG
logging.level.com.control.core=DEBUG
```

Or check your application's security filter chains at startup - they will be logged during application boot.

## �🔧 Development

### Building from Source

```bash
git clone https://github.com/papapitufo/core.git
cd core
./gradlew clean build
```

### Running Tests

```bash
./gradlew test
```

### Publishing to Local Repository

```bash
./gradlew publishToMavenLocal
```

## 📖 Example Application

See the complete working example in the [`example-app/`](./example-app/) directory:

```bash
cd example-app
./gradlew bootRun
```

Navigate to `http://localhost:8080/login` to see the starter in action.

## 🐛 Troubleshooting

### Common Issues

**Email Dependency Missing Error**

If you see errors like:
- `Field mailSender in com.control.core.service.EmailService required a bean of type 'org.springframework.mail.javamail.JavaMailSender'`
- `Parameter 0 of constructor in EmailService required a bean of type 'JavaMailSender' that could not be found`
- `Consider defining a bean of type 'org.springframework.mail.javamail.JavaMailSender'`

This means you're trying to use password reset functionality without the required email dependency.

**Solution 1: Add Email Support (Recommended)**
```kotlin
// Add to your build.gradle.kts dependencies
implementation("org.springframework.boot:spring-boot-starter-mail")
```

```xml
<!-- Add to your pom.xml dependencies -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-mail</artifactId>
</dependency>
```

**Solution 2: Disable Email Features**
```properties
# Disable email-dependent features
core.auth.email.enabled=false
core.auth.password-reset.enabled=false
core.auth.forgot-password.enabled=false
```

**Spring Boot Version Compatibility Error**

If you see errors like:
- `Not a managed type: class com.control.core.model.User`
- `IllegalArgumentException: Unable to locate persister`
- `AutoConfiguration cannot be resolved to a type`
- `Error creating bean with name 'userRepository'`
- `Invocation of init method failed; nested exception is java.lang.IllegalArgumentException: Not a managed type`

This usually means you're using **Spring Boot 2.x** with our **Spring Boot 3.x** starter. The JPA entity scanning and auto-configuration are incompatible between these major versions.

**Solution:** Upgrade your application to Spring Boot 3.x (see [Migration Guide](#migration-from-spring-boot-2x) above).

**Quick Check:**
```bash
# Check your Spring Boot version in terminal output or dependencies
./gradlew dependencies | grep spring-boot-starter
# Look for version numbers like:
# ✅ spring-boot-starter:3.2.0 (compatible)
# ❌ spring-boot-starter:2.7.17 (incompatible - need to upgrade)
```

**Security FilterChain Bean Conflicts**

If you get an error like "The bean 'filterChain' could not be registered", it means your application already has a security configuration. You have several options:

**Option 1: Use Different Bean Names (Recommended)**

Simply rename your security bean method to avoid the conflict:

```java
@Configuration
@EnableWebSecurity
public class YourSecurityConfig {
    
    @Bean
    public SecurityFilterChain applicationSecurityFilterChain(HttpSecurity http) throws Exception {
        // Your existing security configuration
        http.authorizeHttpRequests(authz -> authz
                .requestMatchers("/blog/manager/**").authenticated()
                .requestMatchers("/api/**", "/actuator/**").authenticated()
                .anyRequest().permitAll()
            )
            .formLogin(form -> form.defaultSuccessUrl("/", true))
            .logout(logout -> logout.deleteCookies("JSESSIONID"))
            .rememberMe(remember -> remember
                .key("secret-key")
                .tokenValiditySeconds(2592000) // 30 days
            )
            .sessionManagement(session -> session
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false)
            )
            .httpBasic(httpBasic -> {})
            .csrf(csrf -> csrf.disable());
        return http.build();
    }
}
```

**Why this works:**
- Core Auth Starter creates a bean named `coreAuthDefaultSecurityFilterChain`
- Your application creates a bean named `applicationSecurityFilterChain`
- Spring Security will merge both configurations automatically
- Core Auth endpoints (`/login`, `/admin/**`, etc.) are handled by Core Auth
- Your business endpoints (`/blog/manager/**`, `/api/**`) are handled by your config

**Option 2: Disable Core Auth Security Auto-Configuration**
```properties
# Let your application handle security configuration completely
core.auth.security.auto-configure=false
```

**Option 3: Enable Bean Definition Overriding**
```properties
# Allow your security config to override the starter's
spring.main.allow-bean-definition-overriding=true
```

**Database Connection Errors**
```properties
# Ensure database is running and credentials are correct
spring.datasource.url=jdbc:postgresql://localhost:5432/your_db
spring.jpa.show-sql=true  # Enable to see SQL queries
```

**Email Configuration Issues**
```properties
# Enable debug logging for email
logging.level.com.control.core.service.EmailService=DEBUG
logging.level.org.springframework.mail=DEBUG
```

**Template Not Found Errors**
- Ensure component scanning includes `com.control.core`
- Check that templates are properly packaged in the JAR

**Access Denied Errors**
```properties
# Enable security debug logging
logging.level.org.springframework.security=DEBUG
```

### Debug Mode

Enable detailed logging:

```properties
logging.level.com.control.core=DEBUG
logging.level.org.springframework.security=DEBUG
logging.level.org.springframework.web=DEBUG
```

## 📈 Performance Considerations

- **Database Indexing**: The starter creates indexes on username and email fields
- **Password Hashing**: Uses BCrypt with strength 10 (configurable)
- **Session Management**: Configure session timeout based on your security requirements
- **Email Async**: Email sending is asynchronous to avoid blocking web requests

## 🔒 Security Features

- **CSRF Protection**: Enabled by default for all POST requests
- **Password Hashing**: BCrypt with configurable strength
- **Session Fixation**: Protection enabled
- **XSS Protection**: Headers configured
- **Token-based Password Reset**: Secure, time-limited tokens
- **Role-based Access Control**: Granular permissions

## 🤝 Contributing

We welcome contributions! Please see our [Contributing Guidelines](CONTRIBUTING.md):

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### Development Setup

1. Clone the repository
2. Import into your IDE as a Gradle project
3. Run tests: `./gradlew test`
4. Test with example app: `cd example-app && ./gradlew bootRun`

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🆘 Support

- **Documentation**: [README.md](README.md) and inline code comments
- **Issues**: [GitHub Issues](https://github.com/papapitufo/core/issues)
- **Discussions**: [GitHub Discussions](https://github.com/papapitufo/core/discussions)
- **Email**: For private inquiries, contact [robimoller@example.com](mailto:robimoller@example.com)

## 📝 Changelog

### Version 1.0.4 (2025-07-27)

#### 🔧 Property Configuration Fixes
- **Configuration Properties**: Fixed property mapping for admin configuration
  - Renamed field from `defaultAdmin` to `admin` in CoreAuthProperties
  - Updated getter/setter methods from `getDefaultAdmin()/setDefaultAdmin()` to `getAdmin()/setAdmin()`
  - Corrected conditional property annotations from `core.auth.default-admin.*` to `core.auth.admin.*`
  - Updated AdminUser properties: replaced `createOnStartup` with `enabled` field
  - Fixed DefaultAdminCreator to use correct property references and conditional annotations

#### 📚 Documentation
- **Property Examples**: Updated all documentation to reflect corrected property names
- **Configuration Guide**: Enhanced property mapping examples for better clarity

#### ✅ Compatibility
- **Spring Boot Configuration Processor**: Improved property recognition in IDEs
- **Consumer Applications**: Resolved "unknown properties" errors in application.properties

### Version 1.0.3 (2025-07-27)

#### 🐛 Bug Fixes
- **Email Dependencies**: Made email functionality optional to prevent startup errors
  - EmailService now uses `@ConditionalOnClass(JavaMailSender.class)` and `@ConditionalOnProperty`
  - PasswordResetService handles missing EmailService gracefully with `@Autowired(required = false)`
  - Changed `spring-boot-starter-mail` from `api` to `compileOnly` dependency
  - Added comprehensive documentation for email dependency requirements

#### 📚 Documentation
- Added detailed dependency instructions for email functionality
- Enhanced troubleshooting section with email dependency error solutions
- Updated version compatibility documentation
- Added migration guide for Spring Boot 2.x to 3.x upgrade

### Version 1.0.2 (2025-07-27)

#### 🐛 Bug Fixes
- **Security Configuration**: Fixed FilterChain bean conflicts with consuming applications
  - Removed conflicting SecurityConfig class
  - Enhanced auto-configuration with conditional beans
  - Added comprehensive troubleshooting documentation

### Version 1.0.0 (2025-07-27)

#### ✨ Features
- Complete authentication system with login/logout
- User registration with validation
- Password reset functionality with email
- Admin dashboard for user management
- Material UI responsive frontend
- Multi-provider email support (Gmail, SendGrid, MailHog)
- Auto-configuration for Spring Boot
- Comprehensive security configuration
- Role-based access control
- Session management
- CSRF protection

#### 🛠️ Technical
- Spring Boot 3.5.3 compatibility
- Java 17+ support
- PostgreSQL, H2, MySQL database support
- Thymeleaf templating with Material UI styling
- BCrypt password encryption
- JPA/Hibernate entity management
- Maven and Gradle publishing support

#### 📚 Documentation
- Comprehensive README with examples
- Configuration reference
- Troubleshooting guide
- Example application
- API documentation

---

**Made with ❤️ by [Rob Moller](https://github.com/papapitufo)**

*Ready to add authentication to your Spring Boot application? Get started in minutes!*

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests
5. Submit a pull request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## 📝 Version History

### v1.0.22 (Latest) ⭐ **MAJOR RBAC UPDATE**
- **🛡️ NEW: Complete Role-Based Access Control (RBAC) System**
  - Granular permissions system with categories (USER_MANAGEMENT, SYSTEM_MONITORING, etc.)
  - Role management with permission inheritance
  - Direct user permissions beyond role-based permissions
  - Admin interfaces for roles (`/admin/roles`), permissions (`/admin/permissions`), and user permissions (`/admin/users/{id}/permissions`)
- **🔐 NEW: Security Annotations**
  - `@RequirePermission("PERMISSION_NAME")` for method-level security
  - `@AdminOnly` for admin-only access
  - `@RequireOwnership` for owner-or-admin access patterns
- **⚙️ NEW: Authorization Service**
  - Programmatic permission checking with `AuthorizationService`
  - Methods: `hasPermission()`, `hasAnyPermission()`, `hasAllPermissions()`, `canAccessAdmin()`
- **🗄️ Database Schema**: Automatic RBAC table creation (permissions, roles, junction tables)
- **📚 Documentation**: Comprehensive [RBAC Guide](RBAC_GUIDE.md) with examples and best practices
- **🔄 Migration**: 100% backward compatible - existing role-based code continues to work
- **🎨 UI Enhancements**: Material Design admin interfaces for complete RBAC management
- **🔧 Default Setup**: Pre-configured permissions, roles, and admin user with full access

### v1.0.14
- **Added**: Comprehensive System Monitoring dashboard with Spring Boot Actuator integration
- **Enhanced**: Complete actuator documentation with setup instructions for consumer applications
- **Added**: Material Design monitoring dashboard with 9 monitoring cards (Health, Info, Metrics, Environment, etc.)
- **Improved**: Detailed troubleshooting guide for actuator configuration issues
- **Added**: Production-ready actuator configuration examples and security best practices
- **Enhanced**: USER_MANAGEMENT_README.md with actuator setup instructions

### v1.0.13
- **Fixed**: Excluded `index.html` template from library JAR to prevent conflicts with consumer applications
- **Improved**: Consumer applications now have complete control over root route handling
- **Enhanced**: Better separation between starter templates and consumer application templates
- **Added**: Comprehensive route security configuration FAQ section

### v1.0.12
- **Fixed**: Enhanced logout URL filtering in `CustomAuthenticationSuccessHandler`
- **Improved**: Prevents redirects to `/logout?continue` and other logout-related URLs after successful login
- **Enhanced**: More robust URL filtering for edge cases in authentication redirect flow

### v1.0.11
- **Fixed**: Removed root path mapping (`/`) from WebController to allow consumer applications to define their own root routes
- **Improved**: Consumer applications can now properly handle root path without interference from the starter
- **Enhanced**: Better separation of concerns between starter routes and consumer routes

### v1.0.10
- **Fixed**: Chrome DevTools and browser-specific URL interference with authentication redirects
- **Added**: Custom `AuthenticationSuccessHandler` that filters out invalid redirect URLs
- **Improved**: Better handling of saved requests and redirect URL validation
- **Enhanced**: More robust redirect behavior that ignores `.well-known`, `devtools`, and other browser-specific URLs

### v1.0.8
- **Fixed**: Redirect URL behavior after login now respects Spring MVC view controller mappings
- **Changed**: `defaultSuccessUrl` now uses `alwaysUse=false` to allow custom URL routing in consumer applications
- **Improved**: Consumer applications can now properly map "/" to custom templates (e.g., "home") without conflicts

### v1.0.7
- Complete configuration documentation with all properties
- Enhanced README with troubleshooting section
- Improved auto-configuration stability

### v1.0.6
- Added comprehensive property configuration system
- Enhanced security configuration options
- Improved integration documentation

### v1.0.5
- Enhanced email service with multiple provider support
- Added password reset functionality
- Improved Material UI styling

### Earlier Versions
- Initial release with Thymeleaf templates
- Basic authentication and user management
- Admin panel functionality

## Support

For issues and questions:
- GitHub Issues: [https://github.com/papapitufo/core/issues](https://github.com/papapitufo/core/issues)
- Documentation: [https://github.com/papapitufo/core/wiki](https://github.com/papapitufo/core/wiki)
