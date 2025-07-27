package com.control.core.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for Core Auth Starter
 */
@ConfigurationProperties(prefix = "core.auth")
public class CoreAuthProperties {
    
    /**
     * Default redirect URL after successful login
     */
    private String defaultSuccessUrl = "/dashboard";
    
    /**
     * Enable user registration functionality
     */
    private boolean registrationEnabled = true;
    
    /**
     * Enable password reset functionality
     */
    private boolean passwordResetEnabled = true;
    
    /**
     * Enable admin panel functionality
     */
    private boolean adminPanelEnabled = true;
    
    /**
     * Enable forgot password functionality
     */
    private boolean forgotPasswordEnabled = true;
    
    /**
     * Base URL for the application (used in emails)
     */
    private String baseUrl = "http://localhost:8080";
    
    /**
     * Default admin user configuration
     */
    private AdminUser defaultAdmin = new AdminUser();
    
    /**
     * Email configuration
     */
    private Email email = new Email();
    
    /**
     * Security auto-configuration settings
     */
    private Security security = new Security();
    
    // Getters and Setters
    public String getDefaultSuccessUrl() {
        return defaultSuccessUrl;
    }
    
    public void setDefaultSuccessUrl(String defaultSuccessUrl) {
        this.defaultSuccessUrl = defaultSuccessUrl;
    }
    
    public boolean isRegistrationEnabled() {
        return registrationEnabled;
    }
    
    public void setRegistrationEnabled(boolean registrationEnabled) {
        this.registrationEnabled = registrationEnabled;
    }
    
    public boolean isPasswordResetEnabled() {
        return passwordResetEnabled;
    }
    
    public void setPasswordResetEnabled(boolean passwordResetEnabled) {
        this.passwordResetEnabled = passwordResetEnabled;
    }
    
    public boolean isAdminPanelEnabled() {
        return adminPanelEnabled;
    }
    
    public void setAdminPanelEnabled(boolean adminPanelEnabled) {
        this.adminPanelEnabled = adminPanelEnabled;
    }
    
    public boolean isForgotPasswordEnabled() {
        return forgotPasswordEnabled;
    }
    
    public void setForgotPasswordEnabled(boolean forgotPasswordEnabled) {
        this.forgotPasswordEnabled = forgotPasswordEnabled;
    }
    
    public String getBaseUrl() {
        return baseUrl;
    }
    
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
    
    public AdminUser getDefaultAdmin() {
        return defaultAdmin;
    }
    
    public void setDefaultAdmin(AdminUser defaultAdmin) {
        this.defaultAdmin = defaultAdmin;
    }
    
    public Email getEmail() {
        return email;
    }
    
    public void setEmail(Email email) {
        this.email = email;
    }
    
    public Security getSecurity() {
        return security;
    }
    
    public void setSecurity(Security security) {
        this.security = security;
    }
    
    /**
     * Default admin user configuration
     */
    public static class AdminUser {
        private String username = "admin";
        private String email = "admin@example.com";
        private String password = "admin123";
        private boolean createOnStartup = true;
        
        // Getters and Setters
        public String getUsername() {
            return username;
        }
        
        public void setUsername(String username) {
            this.username = username;
        }
        
        public String getEmail() {
            return email;
        }
        
        public void setEmail(String email) {
            this.email = email;
        }
        
        public String getPassword() {
            return password;
        }
        
        public void setPassword(String password) {
            this.password = password;
        }
        
        public boolean isCreateOnStartup() {
            return createOnStartup;
        }
        
        public void setCreateOnStartup(boolean createOnStartup) {
            this.createOnStartup = createOnStartup;
        }
    }
    
    /**
     * Email configuration
     */
    public static class Email {
        private String fromAddress = "noreply@example.com";
        private String fromName = "Core Auth";
        
        // Getters and Setters
        public String getFromAddress() {
            return fromAddress;
        }
        
        public void setFromAddress(String fromAddress) {
            this.fromAddress = fromAddress;
        }
        
        public String getFromName() {
            return fromName;
        }
        
        public void setFromName(String fromName) {
            this.fromName = fromName;
        }
    }
    
    /**
     * Security auto-configuration settings
     */
    public static class Security {
        /**
         * Whether to auto-configure security filter chain
         */
        private boolean autoConfigure = true;
        
        // Getters and Setters
        public boolean isAutoConfigure() {
            return autoConfigure;
        }
        
        public void setAutoConfigure(boolean autoConfigure) {
            this.autoConfigure = autoConfigure;
        }
    }
}
