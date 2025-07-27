# Core Auth Starter

A comprehensive Spring Boot Starter for authentication and user management with a beautiful Material UI frontend.

[![Version](https://img.shields.io/badge/version-1.0.3-blue.svg)](https://github.com/papapitufo/core/packages)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.3-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17+-orange.svg)](https://openjdk.java.net/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

## 🚀 Features

🔐 **Complete Authentication System**
- Login/Logout functionality with username or email
- User registration with comprehensive validation
- Password reset with secure email verification
- Role-based access control (ADMIN/USER)
- Session management and security

👥 **Advanced User Management**
- Admin dashboard for complete user lifecycle management
- Create, activate, deactivate, and delete users
- Real-time user search and filtering
- User activity tracking and last login timestamps
- Bulk operations support

🎨 **Professional Material UI Frontend**
- Responsive design that works on all devices
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
- Automatic database schema creation
- Default admin user creation
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

dependencies {
    implementation("com.control:core-auth-starter:1.0.3")
    runtimeOnly("org.postgresql:postgresql") // or your preferred database
    
    // Required for email functionality (password reset emails)
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
        <version>1.0.3</version>
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
    implementation("com.control:core-auth-starter:1.0.3")
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
        <version>1.0.2</version>
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
    implementation("com.control:core-auth-starter:1.0.3")
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
| `/forgot-password` | Password reset request | Public | Email-based token generation |
| `/reset-password` | Password reset form | Public | Secure token validation |
| `/logout` | User logout | Authenticated | Session cleanup |

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

Create your own `SecurityFilterChain` bean to customize security:

```java
@Configuration
public class CustomSecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/custom/**").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/custom-login")
                .defaultSuccessUrl("/custom-dashboard")
            )
            .build();
    }
}
```

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

If you need custom security rules, you have two approaches:

**Approach 1: Disable Auto-Configuration and Define Your Own**
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
                .requestMatchers("/login", "/signup", "/css/**", "/js/**").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
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

**Approach 2: Use Core Auth Security with Custom Rules**
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

## 🔧 Development

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

**Option 1: Disable Core Auth Security Auto-Configuration (Recommended)**
```properties
# Let your application handle security configuration completely
core.auth.security.auto-configure=false
```

**Option 2: Enable Bean Definition Overriding**
```properties
# Allow your security config to override the starter's
spring.main.allow-bean-definition-overriding=true
```

**Option 3: Use Core Auth Security (Remove Your Security Config)**
Remove your application's `@Configuration` class that defines `SecurityFilterChain` and let Core Auth handle security.

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

## Support

For issues and questions:
- GitHub Issues: [https://github.com/papapitufo/core/issues](https://github.com/papapitufo/core/issues)
- Documentation: [https://github.com/papapitufo/core/wiki](https://github.com/papapitufo/core/wiki)
