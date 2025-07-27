package com.control.core.autoconfigure;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Auto-configuration for Core Auth Starter
 */
@AutoConfiguration
@ConditionalOnClass({JpaRepository.class, UserDetailsService.class})
@EnableConfigurationProperties(CoreAuthProperties.class)
@EnableJpaRepositories(basePackages = "com.control.core.repository")
@EntityScan(basePackages = "com.control.core.model")
@ComponentScan(basePackages = "com.control.core")
public class CoreAuthAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @ConditionalOnMissingBean
    public SecurityFilterChain coreAuthSecurityFilterChain(HttpSecurity http, CoreAuthProperties properties) throws Exception {
        return http
            .authorizeHttpRequests(auth -> {
                auth.requestMatchers("/login", "/css/**", "/js/**", "/images/**").permitAll();
                
                if (properties.isRegistrationEnabled()) {
                    auth.requestMatchers("/signup").permitAll();
                }
                
                if (properties.isForgotPasswordEnabled()) {
                    auth.requestMatchers("/forgot-password", "/reset-password").permitAll();
                }
                
                if (properties.isAdminPanelEnabled()) {
                    auth.requestMatchers("/admin/**").hasRole("ADMIN");
                }
                
                auth.anyRequest().authenticated();
            })
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl(properties.getDefaultSuccessUrl(), true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            )
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/api/**") // Allow API endpoints to be called without CSRF
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
