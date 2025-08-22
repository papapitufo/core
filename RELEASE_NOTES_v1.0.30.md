# Release Notes v1.0.30

## 🚀 New Features

### Real-Time Log Streaming with Server-Sent Events (SSE)
- **Live Log Monitoring**: Added comprehensive SSE infrastructure for real-time log streaming in the admin dashboard
- **EventSource Integration**: Implemented browser-compatible EventSource with proper callback handling (onopen, onmessage, onerror)
- **Enhanced Debugging**: Added emoji-based console logging and connection state monitoring for easier debugging

### Enhanced Admin Dashboard
- **Logs Detail Page**: New interactive logs viewer with Material UI components
- **Live Stream Toggle**: Start/stop real-time log streaming with visual indicators
- **Log Level Filtering**: Filter logs by ERROR, WARN, INFO, DEBUG levels
- **Real-Time Statistics**: Display active stream count and log level distribution

### Improved Logging Infrastructure
- **InMemoryLogAppender**: Enhanced log appender with configurable buffer size and DEBUG level support
- **Immediate Connection Events**: SSE connections now send immediate "connected" events to ensure proper browser EventSource establishment
- **JSON Log Format**: Structured log events with timestamp, level, logger, message, thread, and color coding
- **Security Integration**: CSRF-exempt SSE endpoints with proper Spring Security integration

## 🔧 Technical Improvements

### SSE Connection Handling
- **Browser Compatibility**: Fixed EventSource callback issues by implementing immediate event transmission pattern
- **Connection Timeout**: Configurable 30-minute SSE connection timeout with proper cleanup
- **Error Handling**: Comprehensive error handling and automatic connection cleanup on failures
- **Memory Management**: CopyOnWriteArrayList for thread-safe emitter management

### Configuration Enhancements
- **Debug Logging**: Added Spring framework debug logging configuration options
- **Flexible Log Levels**: Support for DEBUG, INFO, WARN, ERROR level filtering
- **Performance Optimized**: Efficient log buffering with configurable maximum size (1000 entries)

### Security & CSRF
- **SSE Security Filter**: Dedicated security filter chain for SSE endpoints
- **CSRF Exemption**: Proper CSRF exemption for `/admin/actuator/logs/stream` endpoint
- **Admin Access**: SSE endpoints protected with existing admin role authorization

## 🎯 Developer Experience

### Testing & Debugging
- **Test Controller**: Added TestController with endpoints for generating test logs
- **Activity Simulation**: Simulate user activities (login, navigate, error) for testing
- **Enhanced Console Output**: Emoji-based debugging with connection state monitoring
- **Log Generation Endpoints**: `/api/test/logs` and `/api/test/activity/{action}` for testing

### Frontend Integration
- **Material UI Styling**: Consistent styling with existing admin dashboard
- **Responsive Design**: Mobile-friendly log viewer interface  
- **Visual Feedback**: Loading indicators, connection status, and stream statistics
- **Event Type Handling**: Separate handling for "connected" and "log" events

## 📝 Configuration Changes

### Application Properties
```properties
# Enhanced logging configuration
logging.level.org.springframework.web.servlet.DispatcherServlet=DEBUG
logging.level.org.springframework.security.web=DEBUG
logging.level.org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping=DEBUG
```

### New Endpoints
- `GET /admin/actuator/logs/stream` - SSE endpoint for real-time log streaming
- `GET /admin/actuator/logs/detail` - Interactive logs viewer page
- `GET /api/test/logs` - Generate test logs (development/testing)
- `GET /api/test/activity/{action}` - Simulate user activities (development/testing)

## 🐛 Bug Fixes
- **EventSource Callbacks**: Fixed issue where onopen, onmessage, and onerror callbacks weren't firing
- **Log Level Filtering**: Fixed DEBUG logs being filtered out in InMemoryLogAppender
- **Connection Establishment**: Resolved EventSource remaining in CONNECTING state issue
- **Memory Leaks**: Proper cleanup of SSE connections on timeout and error

## 🔄 Breaking Changes
None - this release is fully backward compatible.

## 📦 Dependencies
No new external dependencies added. Uses existing Spring Boot SSE infrastructure.

## 🚀 Migration Guide
Existing applications will automatically receive the new SSE functionality. No configuration changes required for basic operation.

To access the new log streaming features:
1. Navigate to `/admin/actuator/logs/detail` as an admin user
2. Click "Start Live" to begin real-time log streaming
3. Use the level filter dropdown to focus on specific log levels

## 🎉 What's Next
- Enhanced log search and filtering capabilities
- Log export functionality
- Historical log analysis
- Custom log pattern configuration
- WebSocket alternative for high-frequency logging

---

**Full Changelog**: https://github.com/papapitufo/core/compare/v1.0.29...v1.0.30
