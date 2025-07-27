package com.control.core.autoconfigure;

import com.control.core.security.CustomAuthenticationSuccessHandler;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.annotation.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Auto-configuration for Core Auth Starter
 * 
 * This auto-configuration provides default beans for authentication functionality
 * but allows consuming applications to override any configuration as needed.
 */
@AutoConfiguration
@AutoConfigureBefore(SecurityAutoConfiguration.class)
@ConditionalOnClass({JpaRepository.class, UserDetailsService.class})
@EnableConfigurationProperties(CoreAuthProperties.class)
@EnableJpaRepositories(basePackages = "com.control.core.repository")
@EntityScan(basePackages = "com.control.core.model")
@ComponentScan(basePackages = "com.control.core")
public class CoreAuthAutoConfiguration {

    /**
     * Provides a default password encoder if none is defined
     */
    @Bean
    @ConditionalOnMissingBean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Provides default security configuration for the core auth starter.
     * This configuration will override Spring Boot's default security configuration.
     * Can be disabled by setting core.auth.security.auto-configure=false
     */
        @Bean
    @Order(1)
    @ConditionalOnMissingBean(name = "coreAuthDefaultSecurityFilterChain")
    @ConditionalOnProperty(name = "core.auth.security.auto-configure", havingValue = "true", matchIfMissing = true)
    public SecurityFilterChain coreAuthDefaultSecurityFilterChain(HttpSecurity http, CoreAuthProperties properties) throws Exception {
        System.out.println("🔧 CoreAuth: Configuring security with defaultSuccessUrl: " + properties.getDefaultSuccessUrl());
        
        return http
            .securityMatcher("/login", "/logout", "/signup", "/forgot-password", "/reset-password", "/admin/**", "/dashboard", "/api/**")
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/login", "/signup", "/forgot-password", "/reset-password", "/css/**", "/js/**", "/images/**").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .successHandler(new CustomAuthenticationSuccessHandler(properties.getDefaultSuccessUrl()))
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout=true")
                .permitAll()
            )
            .build();
    }
    
    /**
     * Configuration bean that makes properties available to other components
     */
    @Bean
    @ConditionalOnMissingBean
    public CoreAuthConfigurationBean coreAuthConfiguration(CoreAuthProperties properties) {
        return new CoreAuthConfigurationBean(properties);
    }
    
    /**
     * Bean to hold configuration for easy access by other components
     */
    public static class CoreAuthConfigurationBean {
        private final CoreAuthProperties properties;
        
        public CoreAuthConfigurationBean(CoreAuthProperties properties) {
            this.properties = properties;
        }
        
        public CoreAuthProperties getProperties() {
            return properties;
        }
        
        public boolean isRegistrationEnabled() {
            return properties.isRegistrationEnabled();
        }
        
        public boolean isAdminPanelEnabled() {
            return properties.isAdminPanelEnabled();
        }
        
        public boolean isForgotPasswordEnabled() {
            return properties.isForgotPasswordEnabled();
        }
        
        public String getBaseUrl() {
            return properties.getBaseUrl();
        }
    }
}
