# Core Auth Starter

A comprehensive Spring Boot Starter for authentication and user management with a beautiful Material UI frontend.

[![Version](https://img.shields.io/badge/version-1.0.1-blue.svg)](https://github.com/papapitufo/core/packages)
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
    implementation("com.control:core-auth-starter:1.0.1")
    runtimeOnly("org.postgresql:postgresql") // or your preferred database
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
        <version>1.0.1</version>
    </dependency>
</dependencies>
```

## 🛠️ Quick Setup

### 1. Configure Your Application

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

### Feature Toggles
```properties
# Enable/disable features
core.auth.registration.enabled=true
core.auth.password-reset.enabled=true
core.auth.admin-panel.enabled=true
core.auth.user-dashboard.enabled=true

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
- **Spring Boot**: 3.5.3+
- **Database**: Any JPA-compatible database (PostgreSQL, H2, MySQL, etc.)
- **Build Tool**: Gradle 8+ or Maven 3.6+

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
