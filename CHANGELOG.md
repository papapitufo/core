# Changelog

All notable changes to the Core Auth Starter project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.0.20] - 2025-08-06

### ✨ Added

#### Complete Actuator Endpoints Overview
- **All Endpoints Overview Page** - Comprehensive catalog of all available Spring Boot actuator endpoints
- **Categorized Organization** - Endpoints grouped by functionality (Monitoring, Configuration, Diagnostics, Runtime, Database, Performance, etc.)
- **Interactive Endpoint Cards** - Click-through navigation with status indicators and descriptions
- **Advanced Search Functionality** - Real-time search across endpoint names, descriptions, and categories with highlighting
- **Professional Material UI Styling** - Consistent design language across all actuator views

#### Enhanced Navigation & User Experience
- **Improved Breadcrumb Navigation** - Consistent navigation paths across all actuator views
- **Unified Dashboard Integration** - Seamless navigation from main actuator dashboard to detailed endpoint views
- **Responsive Design** - Optimized for desktop and mobile viewing with adaptive layouts
- **Smart Endpoint Detection** - Automatic discovery and categorization of available actuator endpoints

### 🔧 Fixed

#### Template Processing Issues
- **Fixed Thread Dump Stack Trace Rendering** - Resolved SpringEL property access issues for LinkedHashMap objects from JSON responses
- **Corrected Property Access Patterns** - Changed from object notation to map notation for actuator JSON data (`frame.property` → `frame['property']`)
- **Enhanced Error Handling** - Improved graceful degradation when actuator endpoints are unavailable

#### UI/UX Improvements
- **Fixed Breadcrumb Consistency** - Corrected "Thread Dump" page breadcrumb navigation
- **Improved Card Navigation** - Updated existing "All Endpoints" card to navigate to organized overview instead of raw JSON
- **Enhanced Search Coordination** - Better state management between search functionality and filtering

### 📊 Enhanced Monitoring Capabilities

#### Endpoint Categories & Metadata
- **Monitoring** - Health checks, application info, and performance metrics
- **Configuration** - Environment properties and configuration management
- **Application Structure** - Request mappings and Spring bean analysis
- **Diagnostics** - Thread dumps and logger configuration
- **Runtime** - Scheduled tasks and session management
- **Database** - Migration tracking (Flyway, Liquibase)
- **Performance** - Cache statistics and optimization
- **Auto-Configuration** - Condition evaluation and debugging
- **Management** - Application lifecycle controls

#### Smart Endpoint Discovery
- **Automatic Categorization** - Intelligent grouping of endpoints by their purpose and functionality
- **Dynamic Status Detection** - Real-time availability checking with visual indicators
- **Metadata Enrichment** - Descriptive information and appropriate icons for each endpoint
- **URL Mapping** - Direct navigation to detailed views where available

### 🎯 Technical Improvements

#### Controller Enhancements
- **RestTemplate Integration** - Consistent approach for actuator data fetching across all endpoints
- **Helper Method Library** - Reusable utilities for endpoint formatting, categorization, and metadata generation
- **Error Handling** - Comprehensive exception management with user-friendly error messages
- **Performance Optimization** - Efficient data processing and caching strategies

#### Template Architecture
- **Modular Design** - Reusable components and consistent styling patterns
- **Search & Filter Logic** - Advanced JavaScript for real-time content filtering and highlighting
- **Accessibility** - Proper ARIA labels and keyboard navigation support
- **SEO Optimization** - Semantic HTML structure and proper meta tags

### 📖 Documentation

#### Updated Feature Documentation
- **Complete Actuator Integration Guide** - Step-by-step setup and configuration instructions
- **Endpoint Reference** - Comprehensive documentation of all available monitoring capabilities
- **Troubleshooting Guide** - Common issues and resolution strategies
- **Best Practices** - Recommendations for production monitoring setups

## [1.0.19] - 2025-08-05

### ✨ Added

#### Complete Actuator Dashboard Integration
- **Comprehensive Admin Dashboard** with full Spring Boot Actuator endpoint integration
- **Environment Configuration View** - Browse and analyze all application properties and environment variables
- **Request Mappings View** - Detailed view of all HTTP endpoints with methods, patterns, and handlers
- **Configuration Properties View** - Complete overview of all Spring Boot configuration properties with sources
- **Spring Beans View** - Searchable and filterable view of all Spring beans by context with dependency information
- **Thread Dump View** - Real-time thread monitoring with stack traces, state analysis, and thread grouping

#### Enhanced Material UI Dashboard
- **Consistent Material UI styling** across all actuator views with professional card-based layouts
- **Advanced search functionality** with real-time highlighting and filtering
- **Interactive components** with expandable sections, tabs, and detailed views
- **Responsive design** optimized for desktop and mobile viewing
- **Professional color scheme** with proper Material UI elevation and shadows

#### Advanced Monitoring Features
- **Thread Analysis** - Thread state statistics, grouping by type (Tomcat, Database, JVM), daemon vs user threads
- **Configuration Search** - Search across all configuration properties with source attribution
- **Bean Dependency Mapping** - Visual representation of bean relationships and contexts
- **Request Mapping Details** - Complete endpoint analysis with HTTP methods, parameters, and handler information
- **Environment Insights** - System properties, JVM arguments, and application-specific configurations

### 🔧 Technical Improvements

#### AdminController Enhancements
- **RestTemplate-based actuator integration** for better Spring Boot compatibility across versions
- **Comprehensive error handling** with user-friendly error pages and fallback content
- **Performance optimizations** with efficient data processing and caching
- **Thread-safe implementations** for concurrent actuator data access

#### Template Architecture
- **Reusable template components** with consistent navigation and breadcrumb systems
- **Advanced JavaScript functionality** for search, filtering, and interactive elements
- **Optimized CSS** with custom scrollbars, hover effects, and loading states
- **Accessibility improvements** with proper ARIA labels and keyboard navigation

#### Data Processing
- **Smart categorization** of threads, beans, and configuration properties
- **Statistical analysis** of thread states and bean distributions
- **Efficient search algorithms** with regex support and case-insensitive matching
- **Real-time data updates** with refresh functionality

### 🎯 New Admin Dashboard Features

| Feature | Description | Functionality |
|---------|-------------|--------------|
| **Environment** | Application properties and system variables | Search, categorize, and analyze all configuration |
| **Request Mappings** | HTTP endpoint analysis | View methods, patterns, handlers, and parameters |
| **Configuration Properties** | Spring Boot configuration overview | Browse properties by source with descriptions |
| **Spring Beans** | Bean container analysis | Search beans by name, type, and context |
| **Thread Dump** | Real-time thread monitoring | Analyze thread states, view stack traces, filter by type |

### 📊 Enhanced Analytics

#### Thread Monitoring
- **Thread state distribution** with visual statistics
- **Thread grouping** by application layer (Tomcat, Database, Catalina, JVM)
- **Stack trace analysis** with expandable detailed views
- **Daemon vs User thread classification** with separate counting

#### Configuration Analysis
- **Property source tracking** to understand configuration origins
- **Environment variable mapping** with system property correlation
- **Profile-specific configuration** viewing and analysis
- **Configuration validation** and error detection

#### Performance Insights
- **Bean initialization tracking** with dependency resolution
- **Request mapping performance** analysis with handler information
- **Thread pool monitoring** with detailed thread state tracking
- **Memory and resource usage** indicators through actuator integration

### 🔧 Fixed

#### Template Processing Issues
- **Fixed Thymeleaf expression evaluation** for thread stack traces with proper null checking
- **Resolved SpringEL conversion errors** for ArrayList to Boolean conversions
- **Enhanced error handling** in template rendering with graceful degradation
- **Improved data binding** for complex nested actuator data structures

#### UI/UX Improvements
- **Fixed breadcrumb navigation** with correct page titles and links
- **Enhanced search coordination** with proper state management between filters and search
- **Improved scrollable containers** with custom scrollbar styling
- **Fixed responsive layout** issues on mobile devices

### 📖 Documentation

#### Updated Admin Panel Documentation
- **Complete actuator integration guide** with setup instructions
- **Feature documentation** for all new dashboard capabilities
- **Configuration examples** for enabling actuator endpoints
- **Troubleshooting guide** for common actuator integration issues

## [1.0.7] - 2025-07-27

### 📖 Documentation

#### Complete Configuration Reference
- **Added comprehensive configuration table** with all available properties, types, defaults, and descriptions
- **Added complete example configuration** showing all properties in action
- **Added feature disabling examples** showing how to turn off specific functionality
- **Enhanced property organization** with clear categorization (Core, Admin, Email, Security)
- **Added production security warnings** for default admin credentials

#### Configuration Properties Documentation
- **Complete reference table** for all 12+ configuration properties
- **Practical examples** for common configuration scenarios
- **Security best practices** with warnings about default values
- **Copy-paste ready configurations** for different use cases

### 📋 New Documentation Sections
- **📋 Complete Configuration Reference** - Master list of all properties
- **🚫 Disabling Features** - How to turn off specific functionality
- **📝 Example Configuration** - Complete working example

## [1.0.6] - 2025-07-27

### 📖 Documentation

#### Enhanced Configuration Documentation
- **Added comprehensive UI and Navigation settings** documentation in README
- **Documented `core.auth.default-success-url` property** for customizing post-login landing page
- **Enhanced configuration examples** with all available UI control properties
- **Improved property organization** with clear sections for admin, UI, database, and email settings
- **Added examples for common configuration scenarios** including custom landing pages

#### Configuration Properties Reference
- **`core.auth.default-success-url`** - Set custom landing page after login (default: `/dashboard`)
- **`core.auth.registration-enabled`** - Enable/disable user registration
- **`core.auth.admin-panel-enabled`** - Enable/disable admin panel access
- **`core.auth.forgot-password-enabled`** - Enable/disable password reset functionality
- **`core.auth.base-url`** - Base URL for email links

### ✨ Features
- **Default success URL** is already configurable via `core.auth.default-success-url` property
- **All UI features** can be enabled/disabled via configuration properties

## [1.0.5] - 2025-07-27

### 🔧 Fixed

#### Security Configuration Override
- **Fixed security configuration precedence** - removed `@ConditionalOnMissingBean(SecurityFilterChain.class)` to ensure starter's security config takes precedence
- **Added `@Order(1)` annotation** to prioritize starter's security configuration over Spring Boot's default
- **Added `@AutoConfigureBefore(SecurityAutoConfiguration.class)`** to ensure proper configuration order
- **Named security bean** as `coreAuthSecurityFilterChain` to avoid conflicts
- **Resolved default Spring login page issue** - consumers will now see the custom Material UI login page

#### Spring Boot Version Compatibility
- **Improved compatibility** with different Spring Boot versions (3.0.x and 3.5.x)
- **Enhanced auto-configuration reliability** for security configurations
- **Better handling of default SecurityFilterChain** beans in consumer applications

### 📖 Documentation
- **Updated troubleshooting guide** with security configuration override information
- **Added version compatibility notes** for different Spring Boot versions

## [1.0.4] - 2025-07-27

### 🔧 Fixed

#### Configuration Property Mapping
- **Fixed property mapping for admin configuration** - resolved "unknown properties" error in consumer applications
- **Renamed field from `defaultAdmin` to `admin`** in CoreAuthProperties to match documented property paths
- **Updated getter/setter methods** from `getDefaultAdmin()/setDefaultAdmin()` to `getAdmin()/setAdmin()`
- **Corrected conditional property annotations** from `core.auth.default-admin.*` to `core.auth.admin.*`
- **Updated AdminUser properties** replaced `createOnStartup` with `enabled` field for better clarity
- **Fixed DefaultAdminCreator** to use correct property references and conditional annotations

#### Property Structure Alignment
- **Properties now correctly map** from `application.properties` to Java configuration objects
- **Improved Spring Boot configuration processor compatibility** for better IDE support and property recognition
- **Consistent property naming** between documentation and implementation

### 📖 Documentation
- **Updated README examples** to reflect corrected property names
- **Enhanced configuration documentation** with proper property paths

## [1.0.3] - 2025-07-27

### 🔧 Fixed

#### Email Dependencies
- **Fixed optional email functionality** to gracefully handle missing spring-boot-starter-mail dependency
- **Added conditional loading** for EmailService with `@ConditionalOnClass(JavaMailSender.class)`
- **Improved error handling** when email features are used without mail starter dependency

#### Enhanced Configuration
- **Better dependency management** for optional features
- **Clearer error messages** when required dependencies are missing
- **Improved Spring Boot auto-configuration** for email services

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
