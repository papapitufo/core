# Changelog

All notable changes to the Core Auth Starter project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.0.2] - 2025-07-27

### 🔧 Fixed

#### Security Configuration Conflicts
- **Fixed SecurityFilterChain bean conflicts** when integrating with applications that have existing security configurations
- **Removed conflicting SecurityConfig class** from the starter library that was causing bean definition override exceptions
- **Enhanced auto-configuration** with proper conditional annotations to prevent conflicts

#### Improved Compatibility
- **Added @ConditionalOnMissingBean(SecurityFilterChain.class)** to ensure the starter's security configuration only applies when no existing configuration is present
- **Added security auto-configuration toggle** via `core.auth.security.auto-configure` property
- **Better integration patterns** for consuming applications with existing security setups

#### Configuration Options
- **New security configuration property**: `core.auth.security.auto-configure=true/false`
- **Flexible security integration** allowing consuming applications to choose their security approach
- **Multiple integration strategies** documented for different use cases

### 📚 Documentation

#### Enhanced Troubleshooting
- **Added comprehensive troubleshooting section** for SecurityFilterChain conflicts
- **Multiple resolution strategies** documented with clear examples
- **Step-by-step guides** for different integration approaches

#### Advanced Configuration Guide
- **Custom security configuration examples** for different scenarios
- **Security order and priority documentation** for multiple SecurityFilterChain beans
- **Best practices** for integrating with existing Spring Security configurations

### 🔧 Technical Improvements

#### Auto-Configuration
- **Better conditional bean creation** to prevent conflicts
- **Enhanced property-based configuration** for fine-tuned control
- **Improved Spring Boot compatibility** across different application types

#### Security Architecture
- **Cleaner separation** between library security defaults and application-specific security
- **More flexible security configuration** allowing easier customization
- **Better documentation** of security integration patterns

### 🎯 Migration Guide

If upgrading from 1.0.1 and experiencing security conflicts:

1. **Option 1 - Disable Auto-Configuration (Recommended)**:
   ```properties
   core.auth.security.auto-configure=false
   ```

2. **Option 2 - Allow Bean Override**:
   ```properties
   spring.main.allow-bean-definition-overriding=true
   ```

3. **Option 3 - Remove Your Security Config**: Let Core Auth handle all security

## [1.0.1] - 2025-07-27

### 📚 Enhanced Documentation

#### Added
- Comprehensive README with detailed installation instructions
- Complete configuration reference with all available properties
- Troubleshooting guide with common issues and solutions
- Example code snippets for various integration scenarios
- Performance considerations and security features documentation
- Contributing guidelines and development setup instructions
- Detailed changelog with release notes

#### Improved
- Enhanced POM description with feature highlights
- Better package metadata for GitHub Packages
- Clear version history and upgrade path
- Professional documentation formatting with emojis and badges
- Step-by-step setup instructions for different databases
- Comprehensive email provider configuration examples

#### Package Information
- Added detailed feature descriptions to package metadata
- Improved discoverability on GitHub Packages
- Enhanced POM information for Maven repositories
- Better artifact description and keywords

## [1.0.0] - 2025-07-27

### 🎉 Initial Release

The first stable release of Core Auth Starter - a comprehensive Spring Boot Starter for authentication and user management.

### ✨ Added

#### Authentication & Security
- Complete user authentication system with login/logout
- User registration with comprehensive validation
- Password reset functionality with secure email verification
- Role-based access control (ADMIN/USER roles)
- Session management with configurable timeouts
- CSRF protection enabled by default
- BCrypt password hashing with configurable strength
- Remember me functionality

#### User Management
- Admin dashboard for complete user lifecycle management
- Create, activate, deactivate, and delete users
- Real-time user search and filtering capabilities
- User activity tracking with last login timestamps
- Bulk operations support for admin users
- User profile management

#### Frontend & UI
- Professional Material UI responsive design
- Consistent 4px border-radius styling throughout
- Modal dialogs with real-time validation feedback
- Loading states and user feedback mechanisms
- Mobile-responsive design that works on all devices
- Accessible form controls and navigation

#### Email Services
- Multi-provider email support (Gmail, SendGrid, MailHog, Custom SMTP)
- Beautifully designed HTML email templates
- Password reset email workflow with secure tokens
- Configurable email providers with fallback options
- Asynchronous email sending to avoid blocking web requests

#### Configuration & Auto-Setup
- Spring Boot Auto-Configuration for zero-code setup
- Automatic database schema creation
- Default admin user creation on startup
- Sensible security defaults with customization options
- Comprehensive configuration properties
- Environment-specific configuration support

#### Database Support
- Support for any JPA-compatible database
- Automatic table creation for users and password reset tokens
- Database migration support
- Optimized queries with proper indexing
- Transaction management

#### Development Features
- Hot reloading support with Spring Boot DevTools
- Comprehensive test suite with H2 database
- Example application demonstrating all features
- Debug logging configuration
- Development-friendly defaults

### 🛠️ Technical Details

#### Dependencies
- Spring Boot 3.5.3 compatibility
- Java 17+ support
- Gradle 8+ and Maven 3.6+ build support
- PostgreSQL, H2, MySQL database drivers
- Thymeleaf templating engine
- Spring Security 6.x
- Spring Data JPA
- Spring Mail

#### Architecture
- Clean separation of concerns with service layer
- Repository pattern for data access
- DTO pattern for data transfer
- Event-driven architecture for extensibility
- Configuration properties for customization
- Auto-configuration classes for Spring Boot integration

#### Security
- Industry-standard security practices
- XSS protection headers
- Session fixation protection
- CSRF token validation
- Secure password reset tokens with expiration
- Role-based method security

### 📚 Documentation

#### Comprehensive Documentation
- Detailed README with step-by-step setup instructions
- Configuration reference with all available properties
- Troubleshooting guide with common issues and solutions
- API documentation with endpoint descriptions
- Example code snippets for common use cases
- Database schema documentation

#### Examples
- Complete working example application
- Integration examples for different scenarios
- Custom template examples
- Security configuration examples
- Email provider configuration examples

### 🎯 Endpoints

The following endpoints are available out of the box:

| Endpoint | Description | Access Level |
|----------|-------------|--------------|
| `/login` | User login page | Public |
| `/signup` | User registration | Public |
| `/dashboard` | User dashboard | Authenticated |
| `/admin/users` | User management | Admin only |
| `/forgot-password` | Password reset request | Public |
| `/reset-password` | Password reset form | Public |
| `/logout` | User logout | Authenticated |

### 🏗️ Database Schema

#### Users Table
- `id` - Primary key (BIGSERIAL)
- `username` - Unique username (VARCHAR 50)
- `email` - Unique email address (VARCHAR 100)
- `password` - BCrypt hashed password (VARCHAR 255)
- `role` - User role (VARCHAR 20, default 'USER')
- `active` - Account status (BOOLEAN, default true)
- `created_at` - Account creation timestamp
- `last_login` - Last login timestamp

#### Password Reset Tokens Table
- `id` - Primary key (BIGSERIAL)
- `token` - Unique reset token (VARCHAR 255)
- `user_id` - Foreign key to users table
- `expiry_date` - Token expiration timestamp
- `created_at` - Token creation timestamp

### 🔧 Configuration Properties

Over 30 configuration properties available for customization:

#### Core Authentication
- `core.auth.admin.enabled` - Enable/disable admin user creation
- `core.auth.admin.username` - Default admin username
- `core.auth.admin.password` - Default admin password
- `core.auth.admin.email` - Default admin email

#### Email Configuration
- `core.auth.email.provider` - Email provider (gmail, sendgrid, mailhog, custom)
- `core.auth.email.smtp.host` - SMTP server host
- `core.auth.email.smtp.port` - SMTP server port
- `core.auth.email.username` - SMTP username
- `core.auth.email.password` - SMTP password

#### Feature Toggles
- `core.auth.registration.enabled` - Enable/disable user registration
- `core.auth.password-reset.enabled` - Enable/disable password reset
- `core.auth.admin-panel.enabled` - Enable/disable admin panel

### 🧪 Testing

#### Test Coverage
- Unit tests for all service classes
- Integration tests for web endpoints
- Security tests for authentication flows
- Email service tests with mock providers
- Database tests with H2 in-memory database

#### Test Configuration
- H2 database for testing
- MockMvc for web layer testing
- TestContainers support for integration testing
- Security test utilities

### 📦 Distribution

#### Maven Publishing
- Published to GitHub Packages
- Maven Central compatible metadata
- Source and Javadoc artifacts included
- POM with complete dependency information

#### Gradle Plugin
- Java Library plugin configuration
- Spring Boot plugin integration
- Maven Publish plugin setup
- Dependency management

### 🎁 What's Included

#### Templates
- `login.html` - Modern login page with validation
- `signup.html` - User registration with real-time validation
- `dashboard.html` - User dashboard with profile information
- `user-management.html` - Admin panel with search and CRUD operations
- `forgot-password.html` - Password reset request form
- `reset-password.html` - Password reset form with token validation
- `email/password-reset.html` - HTML email template for password reset

#### Static Assets
- Material UI CSS framework
- Custom CSS with 4px border-radius theme
- JavaScript for form validation and AJAX operations
- Responsive design utilities

#### Java Classes
- `User` entity with UserDetails implementation
- `PasswordResetToken` entity for secure token management
- `UserService` for user operations
- `EmailService` for multi-provider email support
- `UserController` for web endpoints
- `AdminController` for admin operations
- Auto-configuration classes for Spring Boot integration

### 🚀 Getting Started

This release provides everything needed to add authentication to a Spring Boot application:

1. Add the dependency to your project
2. Configure your database connection
3. Optionally configure email settings
4. Run your application
5. Visit `/login` to see the authentication system in action

No additional configuration required for basic functionality!

### 🔮 Future Enhancements

While this 1.0.0 release is feature-complete, future versions may include:

- OAuth2 integration (Google, GitHub, etc.)
- Two-factor authentication (2FA)
- Advanced password policies
- User profile customization
- Audit logging
- API-first authentication for REST services
- Social login integration
- Advanced admin analytics

### 📞 Support

For issues, questions, or contributions:
- GitHub Issues: https://github.com/papapitufo/core/issues
- GitHub Discussions: https://github.com/papapitufo/core/discussions
- Email: robimoller@example.com

---

**Thank you for using Core Auth Starter!** 🎉

This release represents months of development and testing to provide a production-ready authentication solution for Spring Boot applications. We hope it saves you time and helps you build secure applications faster.
