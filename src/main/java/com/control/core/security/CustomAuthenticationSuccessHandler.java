package com.control.core.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.util.StringUtils;

import java.io.IOException;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationSuccessHandler.class);
    
    private final String defaultTargetUrl;
    private final RequestCache requestCache = new HttpSessionRequestCache();
    
    public CustomAuthenticationSuccessHandler(String defaultTargetUrl) {
        this.defaultTargetUrl = defaultTargetUrl;
        logger.info("CustomAuthenticationSuccessHandler initialized with defaultTargetUrl: {}", defaultTargetUrl);
    }
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, 
                                      HttpServletResponse response, 
                                      Authentication authentication) throws IOException, ServletException {
        
        String targetUrl = determineTargetUrl(request, response);
        
        logger.info("Authentication successful for user: {}. Redirecting to: {}", 
                   authentication.getName(), targetUrl);
        
        // Clear the authentication attributes
        clearAuthenticationAttributes(request);
        
        // Perform the redirect
        response.sendRedirect(targetUrl);
    }
    
    private String determineTargetUrl(HttpServletRequest request, HttpServletResponse response) {
        // Check if there's a saved request (user was trying to access a protected resource)
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        
        if (savedRequest != null) {
            String targetUrl = savedRequest.getRedirectUrl();
            logger.debug("Found saved request with URL: {}", targetUrl);
            
            // Filter out Chrome DevTools and other browser-specific URLs
            if (isValidRedirectUrl(targetUrl)) {
                requestCache.removeRequest(request, response);
                logger.info("Using saved request URL: {}", targetUrl);
                return targetUrl;
            } else {
                logger.warn("Filtered out invalid saved request URL: {}", targetUrl);
            }
        }
        
        // No saved request or invalid URL, use default
        logger.info("Using default target URL: {}", defaultTargetUrl);
        return defaultTargetUrl;
    }
    
    private boolean isValidRedirectUrl(String url) {
        if (!StringUtils.hasText(url)) {
            return false;
        }
        
        // Filter out browser-specific URLs that should not be used for redirects
        String[] invalidPatterns = {
            ".well-known",
            "chrome-extension:",
            "devtools",
            "favicon.ico",
            "robots.txt",
            "/logout",  // Never redirect to logout after successful login
            "logout"    // Also catch relative logout URLs
        };
        
        String lowerUrl = url.toLowerCase();
        for (String pattern : invalidPatterns) {
            if (lowerUrl.contains(pattern)) {
                return false;
            }
        }
        
        return true;
    }
    
    private void clearAuthenticationAttributes(HttpServletRequest request) {
        request.getSession(false).removeAttribute("SPRING_SECURITY_LAST_EXCEPTION");
    }
}
