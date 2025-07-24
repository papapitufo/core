package com.control.core.service;

import com.control.core.model.PasswordResetToken;
import com.control.core.model.User;
import com.control.core.repository.PasswordResetTokenRepository;
import com.control.core.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordResetService {
    
    @Autowired
    private PasswordResetTokenRepository tokenRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private EmailService emailService;
    
    @Transactional
    public void sendPasswordResetEmail(String email) {
        createPasswordResetToken(email);
    }
    
    @Transactional
    public void createPasswordResetToken(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            // Don't reveal if email exists or not for security
            return;
        }
        
        User user = userOpt.get();
        
        // Delete any existing tokens for this user
        tokenRepository.deleteByUser(user);
        
        // Generate new token
        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken(token, user);
        tokenRepository.save(resetToken);
        
        // Send email (we'll handle email service errors gracefully)
        try {
            emailService.sendPasswordResetEmail(user.getEmail(), user.getUsername(), token);
        } catch (Exception e) {
            // Log error but don't fail the request
            System.err.println("Failed to send password reset email: " + e.getMessage());
            throw new RuntimeException("Failed to send password reset email. Please try again later.");
        }
    }
    
    public Optional<PasswordResetToken> findByToken(String token) {
        return tokenRepository.findByToken(token);
    }
    
    public boolean isTokenValid(PasswordResetToken token) {
        return token != null && !token.isExpired() && !token.isUsed();
    }
    
    public boolean isTokenValid(String tokenString) {
        Optional<PasswordResetToken> tokenOpt = tokenRepository.findByToken(tokenString);
        return tokenOpt.isPresent() && isTokenValid(tokenOpt.get());
    }
    
    @Transactional
    public void resetPassword(String token, String newPassword) {
        Optional<PasswordResetToken> tokenOpt = tokenRepository.findByToken(token);
        
        if (tokenOpt.isEmpty()) {
            throw new RuntimeException("Invalid password reset token");
        }
        
        PasswordResetToken resetToken = tokenOpt.get();
        
        if (!isTokenValid(resetToken)) {
            throw new RuntimeException("Password reset token is expired or already used");
        }
        
        // Update user password
        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        
        // Mark token as used
        resetToken.setUsed(true);
        tokenRepository.save(resetToken);
        
        // Send confirmation email
        try {
            emailService.sendPasswordChangeConfirmation(user.getEmail(), user.getUsername());
        } catch (Exception e) {
            // Log error but don't fail the password reset
            System.err.println("Failed to send password change confirmation: " + e.getMessage());
        }
    }
    
    @Transactional
    public void cleanupExpiredTokens() {
        tokenRepository.deleteExpiredTokens(LocalDateTime.now());
    }
}
