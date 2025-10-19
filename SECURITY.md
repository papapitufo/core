# Security Policy

## Reporting Security Vulnerabilities

If you discover a security vulnerability in this project, please report it by emailing the maintainer directly. Please do not create public GitHub issues for security vulnerabilities.

## Security Best Practices

### For Library Users

When using this authentication starter library in your application:

1. **Never commit sensitive credentials to version control**
   - Use environment variables or external configuration
   - Add `application-local.properties` and `application-prod.properties` to `.gitignore`

2. **Change default admin password immediately**
   - The default admin password (`admin123`) is for development only
   - Set a strong password in production using environment variables:
     ```properties
     core.auth.admin.password=${ADMIN_PASSWORD}
     ```

3. **Configure secure email settings**
   - Never commit SMTP credentials to version control
   - Use application-specific passwords or OAuth2 for email services
   - Example secure configuration:
     ```properties
     spring.mail.username=${EMAIL_USERNAME}
     spring.mail.password=${EMAIL_PASSWORD}
     ```

4. **Enable HTTPS in production**
   - Always use HTTPS for production deployments
   - Update `core.auth.base-url` to use HTTPS:
     ```properties
     core.auth.base-url=https://yourdomain.com
     ```

5. **Secure session management**
   - Configure session timeout appropriately
   - Use secure session cookies in production
   - Enable CSRF protection (enabled by default)

6. **Database security**
   - Use strong database passwords
   - Never use H2 in-memory database in production
   - Configure proper database access controls

### For Contributors

1. **Do not commit sensitive data**
   - No API keys, passwords, tokens, or credentials
   - No personal email addresses or PII
   - Review your changes before committing

2. **Test security features**
   - Ensure password hashing works correctly
   - Verify CSRF protection is enabled
   - Test permission checks

3. **Code review**
   - All security-related changes require review
   - Look for potential SQL injection, XSS, or other vulnerabilities

## Security Features

This library includes:

- ✅ **Password Encryption**: BCrypt hashing with salt
- ✅ **CSRF Protection**: Enabled by default for all forms
- ✅ **Session Management**: Secure session handling with Spring Security
- ✅ **Role-Based Access Control (RBAC)**: Fine-grained permissions system
- ✅ **SQL Injection Protection**: JPA/Hibernate parameterized queries
- ✅ **XSS Protection**: Thymeleaf auto-escaping enabled
- ✅ **Password Reset**: Secure token-based password reset flow
- ✅ **Account Locking**: Failed login attempt tracking (configurable)

## Keeping Dependencies Updated

Regularly update dependencies to get security patches:

```bash
./gradlew dependencyUpdates
```

## Security Checklist for Production

- [ ] Changed default admin password
- [ ] Configured strong database password
- [ ] Using HTTPS with valid SSL certificate
- [ ] Email credentials stored securely (environment variables)
- [ ] Disabled H2 console in production
- [ ] Disabled debug logging in production
- [ ] Configured session timeout
- [ ] Set up monitoring and alerting
- [ ] Regular security updates applied
- [ ] Database backups configured

## License

This security policy is part of the Core Auth Starter library.
