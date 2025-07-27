package com.control.core.service;

import com.control.core.autoconfigure.CoreAuthProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    
    @Autowired
    private JavaMailSender mailSender;
    
    @Autowired
    private CoreAuthProperties coreAuthProperties;
    
    // Fallback to legacy property names for backward compatibility
    @Value("${app.mail.from:#{null}}")
    private String legacyFromEmail;
    
    @Value("${app.base-url:#{null}}")
    private String legacyBaseUrl;
    
    private String getFromEmail() {
        if (legacyFromEmail != null) {
            return legacyFromEmail;
        }
        return coreAuthProperties.getEmail().getFromAddress();
    }
    
    private String getBaseUrl() {
        if (legacyBaseUrl != null) {
            return legacyBaseUrl;
        }
        return coreAuthProperties.getBaseUrl();
    }
    
    public void sendPasswordResetEmail(String toEmail, String username, String resetToken) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(getFromEmail());
        message.setTo(toEmail);
        message.setSubject("Password Reset Request - Core Application");
        
        String resetUrl = getBaseUrl() + "/reset-password?token=" + resetToken;
        
        String emailBody = String.format(
            "Hello %s,\n\n" +
            "You have requested to reset your password for Core Application.\n\n" +
            "Please click the link below to reset your password:\n" +
            "%s\n\n" +
            "This link will expire in 24 hours.\n\n" +
            "If you did not request this password reset, please ignore this email.\n\n" +
            "Best regards,\n" +
            "Core Application Team",
            username, resetUrl
        );
        
        message.setText(emailBody);
        
        try {
            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send password reset email: " + e.getMessage());
        }
    }
    
    public void sendPasswordChangeConfirmation(String toEmail, String username) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(getFromEmail());
        message.setTo(toEmail);
        message.setSubject("Password Changed Successfully - Core Application");
        
        String emailBody = String.format(
            "Hello %s,\n\n" +
            "Your password has been successfully changed for Core Application.\n\n" +
            "If you did not make this change, please contact our support team immediately.\n\n" +
            "Best regards,\n" +
            "Core Application Team",
            username
        );
        
        message.setText(emailBody);
        
        try {
            mailSender.send(message);
        } catch (Exception e) {
            // Log the error but don't throw exception for confirmation emails
            System.err.println("Failed to send password change confirmation: " + e.getMessage());
        }
    }
}
