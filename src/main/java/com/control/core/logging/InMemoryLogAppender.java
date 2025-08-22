package com.control.core.logging;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * In-memory log appender that stores recent log events and provides live streaming capability.
 * Integrates with the admin dashboard for real-time log monitoring.
 */
@Component
public class InMemoryLogAppender extends AppenderBase<ILoggingEvent> {

    private static InMemoryLogAppender INSTANCE;
    
    private final Deque<LogEvent> buffer = new ArrayDeque<>();
    private final int maxSize = 1000; // Keep last 1000 log entries
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    public InMemoryLogAppender() {
        INSTANCE = this;
    }

    /**
     * Get the singleton instance for use by Spring beans
     */
    public static InMemoryLogAppender getInstance() {
        return INSTANCE;
    }

    @Override
    protected synchronized void append(ILoggingEvent eventObject) {
        // Keep logs at DEBUG level and above to capture Spring framework logs
        if (eventObject.getLevel().levelInt < ch.qos.logback.classic.Level.DEBUG_INT) {
            return;
        }

        LogEvent logEvent = new LogEvent(eventObject);
        
        if (buffer.size() >= maxSize) {
            buffer.removeFirst();
        }
        buffer.addLast(logEvent);

        // Push to active SSE clients
        notifyEmitters(logEvent);
    }

    private void notifyEmitters(LogEvent logEvent) {
        String payload = logEvent.toJson();
        Iterator<SseEmitter> iterator = emitters.iterator();
        while (iterator.hasNext()) {
            SseEmitter emitter = iterator.next();
            try {
                emitter.send(SseEmitter.event()
                    .name("log")
                    .data(payload));
            } catch (Exception ex) {
                iterator.remove();
                emitter.completeWithError(ex);
            }
        }
    }

    public synchronized List<LogEvent> getRecent(int limit, String level) {
        List<LogEvent> result = new ArrayList<>();
        Iterator<LogEvent> iterator = buffer.descendingIterator();
        
        while (iterator.hasNext() && result.size() < limit) {
            LogEvent event = iterator.next();
            if (level == null || "ALL".equals(level) || event.getLevel().equals(level)) {
                result.add(event);
            }
        }
        
        Collections.reverse(result);
        return result;
    }

    public SseEmitter createEmitter(long timeoutMillis) {
        SseEmitter emitter = new SseEmitter(timeoutMillis);
        emitters.add(emitter);
        
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        emitter.onError((e) -> emitters.remove(emitter));
        
        // Send immediate connection established event to trigger browser onopen callback
        try {
            emitter.send(SseEmitter.event()
                .name("connected")
                .data("{\"message\":\"SSE connection established\",\"timestamp\":\"" + 
                      LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")) + 
                      "\"}"));
        } catch (Exception e) {
            System.err.println("Failed to send initial SSE connection event: " + e.getMessage());
        }
        
        return emitter;
    }

    public synchronized Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        Map<String, Integer> levelCounts = new HashMap<>();
        
        // Initialize all level counts to 0
        levelCounts.put("ERROR", 0);
        levelCounts.put("WARN", 0);
        levelCounts.put("INFO", 0);
        levelCounts.put("DEBUG", 0);
        levelCounts.put("TRACE", 0);
        
        // Count actual log events by level
        for (LogEvent event : buffer) {
            levelCounts.merge(event.getLevel(), 1, Integer::sum);
        }
        
        stats.put("totalEvents", buffer.size());
        stats.put("levelCounts", levelCounts);
        stats.put("activeStreams", emitters.size());
        
        return stats;
    }

    /**
     * Internal class to represent a log event with enhanced metadata
     */
    public static class LogEvent {
        private final String timestamp;
        private final String level;
        private final String logger;
        private final String message;
        private final String thread;
        private final String levelColor;

        public LogEvent(ILoggingEvent event) {
            this.timestamp = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(event.getTimeStamp()), 
                ZoneId.systemDefault()
            ).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
            
            this.level = event.getLevel().toString();
            this.logger = event.getLoggerName();
            this.message = event.getFormattedMessage();
            this.thread = event.getThreadName();
            this.levelColor = getLevelColor(event.getLevel().toString());
        }

        private String getLevelColor(String level) {
            switch (level) {
                case "ERROR": return "#f44336"; // Red
                case "WARN": return "#ff9800";  // Orange
                case "INFO": return "#2196f3";  // Blue
                case "DEBUG": return "#4caf50"; // Green
                case "TRACE": return "#9e9e9e"; // Grey
                default: return "#000000";      // Black
            }
        }

        public String toJson() {
            return String.format(
                "{\"timestamp\":\"%s\",\"level\":\"%s\",\"logger\":\"%s\",\"message\":\"%s\",\"thread\":\"%s\",\"levelColor\":\"%s\"}",
                timestamp, level, escapeJson(logger), escapeJson(message), thread, levelColor
            );
        }

        private String escapeJson(String str) {
            if (str == null) return "";
            return str.replace("\\", "\\\\")
                     .replace("\"", "\\\"")
                     .replace("\n", "\\n")
                     .replace("\r", "\\r")
                     .replace("\t", "\\t");
        }

        // Getters
        public String getTimestamp() { return timestamp; }
        public String getLevel() { return level; }
        public String getLogger() { return logger; }
        public String getMessage() { return message; }
        public String getThread() { return thread; }
        public String getLevelColor() { return levelColor; }
    }
}
