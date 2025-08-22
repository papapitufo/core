package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    /**
     * Custom logout success handler to redirect to login page instead of dashboard
     */
    @Bean
    @Primary
    public LogoutSuccessHandler logoutSuccessHandler() {
        System.out.println("Consumer App: Configured primary logout success handler to redirect to /login?logout=true");
        return new SimpleUrlLogoutSuccessHandler() {{
            setDefaultTargetUrl("/login?logout=true");
        }};
    }

    /**
     * Consumer app security filter chain with higher precedence to override logout behavior
     * and handle /app-dashboard path
     */
    @Bean
    @Order(50)  
    public SecurityFilterChain consumerSecurityFilterChain(HttpSecurity http) throws Exception {
        System.out.println("Consumer App: Configuring consumer app security filter chain");
        
        return http
            .securityMatcher("/logout", "/app-dashboard", "/", "/home")  // Add paths we want to handle
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/", "/home").permitAll()  // Allow access to home page
                .requestMatchers("/app-dashboard").authenticated()  // Require authentication for app-dashboard
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .permitAll()
            )
            .build();
    }

    /**
     * Configure CSRF exemption for SSE endpoints
     */
    @Bean
    @Order(45)
    public SecurityFilterChain sseSecurityFilterChain(HttpSecurity http) throws Exception {
        System.out.println("Consumer App: Configuring SSE security filter chain");
        
        return http
            .securityMatcher("/admin/actuator/logs/stream")
            .authorizeHttpRequests(authz -> authz
                .anyRequest().authenticated()
            )
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/admin/actuator/logs/stream")
            )
            .build();
    }
}
