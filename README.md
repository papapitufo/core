# Core Auth Starter

A comprehensive Spring Boot Starter for authentication and user management with a beautiful Material UI frontend.

[![Version](https://img.shields.io/badge/version-1.0.25-blue.svg)](https://github.com/papapitufo/core/packages)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.6-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17+-orange.svg)](https://openjdk.java.net/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

## ✨ Features

### 🔐 Complete Authentication System
- **Login/Logout**: Username or email authentication with "remember me"
- **User Registration**: Comprehensive validation and duplicate prevention
- **Password Reset**: Secure email-based token verification
- **Session Management**: Configurable timeout and security policies

### 🛡️ Advanced RBAC (Role-Based Access Control)
- **Granular Permissions**: Define specific actions (`USER_CREATE`, `REPORT_VIEW`, `SYSTEM_ADMIN`)
- **Role Management**: Create custom roles with permission sets (ADMIN, MODERATOR, USER)
- **Direct User Permissions**: Grant additional permissions beyond role assignments
- **Permission Categories**: Organize by function (User Management, System Monitoring, etc.)
- **Annotation-Based Security**: Use `@RequirePermission`, `@AdminOnly`, `@RequireOwnership`
- **Web Admin Interface**: Manage roles, permissions, and assignments via intuitive UI
- � **[Complete RBAC Guide](RBAC_GUIDE.md)**

### 👥 User Management
- Full user lifecycle management (create, activate, deactivate, delete)
- Real-time search and filtering
- User activity tracking and last login timestamps
- Role and permission assignment
- Bulk operations support

### 🎨 Material UI Frontend
- Responsive design for all devices
- Consistent 4px border-radius styling
- Modal dialogs with real-time validation
- CSRF protection built-in
- Loading states and user feedback

### 📧 Email Services
- Multi-provider support (Gmail, SendGrid, MailHog, custom SMTP)
- Beautiful HTML email templates
- Password reset workflow
- Configurable with fallback options

### ⚙️ Zero-Configuration Setup
- Spring Boot Auto-Configuration
- Automatic database schema creation (including RBAC tables)
- Default admin user with full permissions
- Pre-configured permissions and roles
- Sensible security defaults

## 📦 Installation

> **⚠️ Requirements:** Spring Boot 3.0+ and Java 17+  
> Using Spring Boot 2.x? See [Migration Guide](#migration-from-spring-boot-2x)

### Gradle (Kotlin DSL)

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

dependencies {
    implementation("com.control:core-auth-starter:1.0.25")
    runtimeOnly("org.postgresql:postgresql") // or your preferred database
    
    // Optional: Required for email functionality (password reset)
    implementation("org.springframework.boot:spring-boot-starter-mail")
}
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
        <version>1.0.25</version>
    </dependency>
    <!-- Optional: Required for email functionality (password reset) -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-mail</artifactId>
    </dependency>
</dependencies>
```

## � Quick Start

### 1. Configure Your Application

Add to your `application.properties`:

```properties
# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/your_db
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update

# Admin User (⚠️ Change password in production!)
core.auth.admin.enabled=true
core.auth.admin.username=admin
core.auth.admin.password=admin123
core.auth.admin.email=admin@example.com

# UI Configuration
core.auth.default-success-url=/dashboard

# Email Configuration (optional - for password reset)
core.auth.email.provider=gmail
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
```

### 2. Enable Component Scanning

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

### 3. Run Your Application

That's it! 🎉 Your application now includes:

| Feature | URL | Access |
|---------|-----|--------|
| 🔐 Login | `/login` | Public |
| 📝 Registration | `/signup` | Public |
| 📊 Dashboard | `/dashboard` | Authenticated |
| � User Management | `/admin/users` | Admin |
| �️ Role Management | `/admin/roles` | Admin |
| 🔑 Permissions | `/admin/permissions` | Admin |
| 📊 System Monitoring | `/admin/actuator` | Admin |
| 🔑 Password Reset | `/forgot-password` | Public |

## 📚 Documentation

| Topic | Description | Link |
|-------|-------------|------|
| **RBAC Guide** | Complete guide to roles, permissions, and security annotations | [RBAC_GUIDE.md](RBAC_GUIDE.md) |
| **User Management** | User lifecycle, permissions, and activity tracking | [USER_MANAGEMENT_README.md](USER_MANAGEMENT_README.md) |
| **Configuration** | All configuration properties and options | [See below](#-configuration-options) |
| **Security** | Security best practices and policies | [SECURITY.md](SECURITY.md) |
| **Changelog** | Version history and release notes | [See below](#-changelog) |

## ⚙️ Configuration Options

### Core Settings

```properties
# Navigation
core.auth.default-success-url=/dashboard         # Redirect after login
core.auth.base-url=http://localhost:8080         # Base URL for email links

# Feature Toggles
core.auth.registration-enabled=true              # Enable user registration
core.auth.admin-panel-enabled=true               # Enable admin panel
core.auth.forgot-password-enabled=true           # Enable password reset

# Admin User
core.auth.admin.enabled=true                     # Create admin on startup
core.auth.admin.username=admin                   # Admin username
core.auth.admin.password=admin123                # ⚠️ CHANGE IN PRODUCTION!
core.auth.admin.email=admin@example.com          # Admin email
```

### Email Providers

<details>
<summary><b>Gmail Configuration</b></summary>

```properties
core.auth.email.provider=gmail
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password  # Use App Password, not regular password
core.auth.email.from-address=your-email@gmail.com
core.auth.email.from-name=Your App Name
```
</details>

<details>
<summary><b>SendGrid Configuration</b></summary>

```properties
core.auth.email.provider=sendgrid
spring.mail.host=smtp.sendgrid.net
spring.mail.port=587
spring.mail.username=apikey
spring.mail.password=your-sendgrid-api-key
core.auth.email.from-address=noreply@yourcompany.com
core.auth.email.from-name=Your Company
```
</details>

<details>
<summary><b>MailHog (Development)</b></summary>

```properties
core.auth.email.provider=mailhog
spring.mail.host=localhost
spring.mail.port=1025
spring.mail.username=
spring.mail.password=
```
</details>

<details>
<summary><b>Custom SMTP</b></summary>

```properties
core.auth.email.provider=custom
spring.mail.host=your-smtp-server.com
spring.mail.port=587
spring.mail.username=your-username
spring.mail.password=your-password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```
</details>

### Database Configuration

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

### Spring Boot Actuator (System Monitoring)

```properties
# Enable actuator endpoints
management.endpoints.web.exposure.include=health,info,metrics,loggers
management.endpoint.health.show-details=when-authorized

# Application info
info.app.name=Your Application
info.app.description=Your application description
info.app.version=1.0.0
```

> **� Tip:** See [Spring Boot Actuator Documentation](#-system-monitoring-with-spring-boot-actuator) for detailed monitoring setup.

## 🎨 Customization

## 🎨 Customization

### Override Templates

Create your own templates in `src/main/resources/templates/`:
- `login.html` - Login page
- `signup.html` - Registration page
- `dashboard.html` - User dashboard
- `user-management.html` - Admin interface
- `forgot-password.html` - Password reset request
- `reset-password.html` - Password reset form
- `email/password-reset.html` - Password reset email

### Custom Styling

Override CSS in `src/main/resources/static/css/custom.css`:

```css
/* Override Material UI variables */
:root {
    --primary-color: #your-color;
    --border-radius: 8px;
}

.btn-primary {
    background-color: var(--primary-color);
}
```

### Custom Security

Add your own security rules with a different bean name:

```java
@Configuration
@EnableWebSecurity
public class YourSecurityConfig {
    
    @Bean
    public SecurityFilterChain businessSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/**").authenticated()
                .requestMatchers("/admin/custom/**").hasRole("ADMIN")
                .anyRequest().permitAll()
            )
            .formLogin(form -> form.defaultSuccessUrl("/"))
            .build();
    }
}
```

Core Auth handles its own endpoints (`/login`, `/admin/users`, etc.) while your configuration handles business logic.

## 📋 Requirements

- **Java**: 17+
- **Spring Boot**: 3.0.0+ (3.5.6+ recommended)
- **Database**: Any JPA-compatible database

### Migration from Spring Boot 2.x

<details>
<summary><b>Click to expand migration guide</b></summary>

#### 1. Update Dependencies

**Gradle:**
```kotlin
plugins {
    id("org.springframework.boot") version "3.5.6"
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}
```

**Maven:**
```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.5.6</version>
</parent>

<properties>
    <java.version>17</java.version>
</properties>
```

#### 2. Update Imports (javax → jakarta)

```java
// OLD (Spring Boot 2.x)
import javax.persistence.*;
import javax.servlet.*;

// NEW (Spring Boot 3.x)
import jakarta.persistence.*;
import jakarta.servlet.*;
```

#### 3. Update Security Configuration

```java
// OLD (Spring Boot 2.x)
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()...
    }
}

// NEW (Spring Boot 3.x)
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

</details>

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

### v1.0.25 (Latest) 🐛 **ACTUATOR URL FIX**
- **🔧 Fixed:** Resolved "URI is not absolute" errors in admin actuator endpoints
  - Fixed `/admin/actuator/endpoints` endpoint failing to load
  - Fixed `/admin/actuator/threaddump-detail` endpoint URL resolution
  - Added `buildAbsoluteActuatorUrl()` helper method for proper URL construction
  - Enhanced admin monitoring interface reliability
- **💡 Improvement:** Better URL construction for different deployment contexts
- **🔗 Technical:** Added `HttpServletRequest` parameter to affected controller methods

### v1.0.24 ⭐ **DATABASE COMPATIBILITY**
- **🛠️ Fixed:** Database compatibility issues with PostgreSQL and other databases
- **🔧 Enhanced:** Database-agnostic schema using `GENERATED BY DEFAULT AS IDENTITY`
- **⚙️ Improved:** SQL initialization configuration for better compatibility

### v1.0.23 🔄 **SCHEMA UPDATES**  
- **🛠️ Fixed:** Database schema compatibility improvements
- **⚙️ Updated:** Spring Boot SQL initialization mode configuration

### v1.0.22 ⭐ **MAJOR RBAC UPDATE**
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
