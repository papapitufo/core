package com.control.core.logging;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.filter.ThresholdFilter;
import jakarta.annotation.PostConstruct;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class that attaches the in-memory log appender to the root logger
 * at application startup. This enables log collection for the admin dashboard.
 */
@Configuration
public class LogbackConfiguration {

    @Autowired
    private InMemoryLogAppender inMemoryLogAppender;

    @PostConstruct
    public void attachAppender() {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        
        // Configure the appender
        inMemoryLogAppender.setContext(context);
        inMemoryLogAppender.setName("IN_MEMORY");
        
        // Add threshold filter to capture INFO and above
        ThresholdFilter filter = new ThresholdFilter();
        filter.setLevel("INFO");
        filter.setContext(context);
        filter.start();
        inMemoryLogAppender.addFilter(filter);
        
        // Start the appender
        inMemoryLogAppender.start();
        
        // Attach to root logger for broad coverage
        Logger rootLogger = context.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
        rootLogger.addAppender(inMemoryLogAppender);
        
        // Configure specific loggers for better coverage
        configureLogger(context, "com.control", Level.INFO);
        configureLogger(context, "com.example", Level.INFO);
        configureLogger(context, "org.springframework.web", Level.DEBUG);
        configureLogger(context, "org.springframework.security", Level.DEBUG);
        
        System.out.println("🔧 Core LogbackConfiguration: InMemoryLogAppender configured");
        System.out.println("📊 InMemoryLogAppender instance: " + inMemoryLogAppender.getClass().getName() + "@" + inMemoryLogAppender.hashCode());
        System.out.println("🚀 InMemoryLogAppender started: " + inMemoryLogAppender.isStarted());
    }
    
    private void configureLogger(LoggerContext context, String loggerName, Level level) {
        Logger logger = context.getLogger(loggerName);
        logger.setLevel(level);
        logger.addAppender(inMemoryLogAppender);
        // Keep additivity true so logs also go to root logger and console
    }
}
