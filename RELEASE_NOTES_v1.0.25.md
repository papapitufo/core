# Release Notes - Version 1.0.25

**Release Date:** August 9, 2025

## 🐛 Bug Fixes

### Actuator Endpoint URL Resolution Fix

**Issue Fixed:** Resolved "URI is not absolute" errors when accessing admin actuator endpoints.

**Details:**
- **Problem**: The actuator endpoints (`/admin/actuator/endpoints` and `/admin/actuator/threaddump-detail`) were failing with "Failed to fetch actuator endpoints: URI is not absolute" errors when using RestTemplate with relative URLs.
- **Root Cause**: RestTemplate requires absolute URLs, but the code was using relative paths like `/actuator` and `/actuator/threaddump`.
- **Solution**: 
  - Created a new helper method `buildAbsoluteActuatorUrl(HttpServletRequest request, String endpoint)` that constructs absolute URLs using the current request's scheme, server name, port, and context path.
  - Updated both `allEndpoints()` and `threadDumpDetail()` methods to use this helper method.
  - Added `HttpServletRequest` parameter to the affected controller methods to access request information.

**Files Modified:**
- `src/main/java/com/control/core/controller/AdminController.java`
  - Added `buildAbsoluteActuatorUrl()` helper method
  - Updated `allEndpoints()` method signature and implementation
  - Updated `threadDumpDetail()` method signature and implementation
  - Added proper import for `jakarta.servlet.http.HttpServletRequest`

**Impact:**
- Admin users can now successfully access actuator endpoints without URI resolution errors
- Thread dump analysis functionality is now working correctly
- Actuator endpoints overview page loads without errors
- Improved reliability of the admin monitoring interface

## 🔧 Technical Improvements

- **Enhanced Error Handling**: Better URL construction reduces potential runtime errors
- **Code Reusability**: Centralized URL building logic for future actuator endpoint additions
- **Request Context Awareness**: Endpoints now properly adapt to different deployment contexts and port configurations

## 📋 Migration Notes

This is a backward-compatible release. No configuration changes are required when upgrading from version 1.0.24.

## 🧪 Testing

- Verified actuator endpoints functionality with:
  - Local development servers (localhost:8080, localhost:8081)
  - Different context paths
  - Various Spring Boot actuator configurations

## 📦 Artifacts

Published to GitHub Packages:
- `com.control:core-auth-starter:1.0.25`
- Source JAR: `core-auth-starter-1.0.25-sources.jar`
- Javadoc JAR: `core-auth-starter-1.0.25-javadoc.jar`

---

**Previous Version:** 1.0.24  
**Next Development Version:** 1.0.26-SNAPSHOT
