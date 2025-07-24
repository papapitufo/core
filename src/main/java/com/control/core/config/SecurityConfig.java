package com.control.core.config;

import com.control.core.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/actuator/**").permitAll()  // Allow public access to actuator endpoints
                .requestMatchers("/login", "/signup", "/forgot-password", "/reset-password", "/css/**", "/js/**", "/images/**").permitAll()  // Allow public access to login, signup, password reset and static resources
                .requestMatchers("/admin/**").hasRole("ADMIN")  // Require ADMIN role for admin endpoints
                .requestMatchers("/api/**").authenticated()  // Require authentication for API endpoints
                .anyRequest().authenticated()  // Require authentication for all other requests
            )
            .userDetailsService(userDetailsService)  // Use custom user details service
            .formLogin(form -> form
                .loginPage("/login")  // Custom login page
                .defaultSuccessUrl("/dashboard", true)  // Redirect to dashboard after successful login
                .failureUrl("/login?error=true")  // Redirect to login page with error parameter on failure
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")  // Logout URL
                .logoutSuccessUrl("/login?logout=true")  // Redirect to login page with logout parameter
                .invalidateHttpSession(true)  // Invalidate session
                .deleteCookies("JSESSIONID")  // Delete session cookie
                .permitAll()
            )
            .httpBasic(httpBasic -> {})  // Enable basic authentication for API endpoints
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/api/**")  // Disable CSRF for API endpoints only
            );
        
        return http.build();
    }
}
