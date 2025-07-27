package com.control.core.autoconfigure;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@SpringJUnitConfig
@TestPropertySource(properties = {
    "core.auth.registration-enabled=false",
    "core.auth.default-success-url=/custom-dashboard",
    "core.auth.base-url=http://test.example.com"
})
class CoreAuthAutoConfigurationTest {

    @TestConfiguration
    @EnableConfigurationProperties(CoreAuthProperties.class)
    static class TestConfig {
    }

    @Autowired
    private CoreAuthProperties properties;

    @Test
    void shouldLoadProperties() {
        assertThat(properties).isNotNull();
    }

    @Test
    void shouldRespectCustomConfiguration() {
        assertThat(properties.isRegistrationEnabled()).isFalse();
        assertThat(properties.getDefaultSuccessUrl()).isEqualTo("/custom-dashboard");
        assertThat(properties.getBaseUrl()).isEqualTo("http://test.example.com");
    }

    @Test
    void shouldHaveDefaultValues() {
        assertThat(properties.isPasswordResetEnabled()).isTrue();
        assertThat(properties.isAdminPanelEnabled()).isTrue();
        assertThat(properties.isForgotPasswordEnabled()).isTrue();
    }
}
