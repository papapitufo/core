# User Management System

This application now includes a complete user management system with PostgreSQL database integration.

## Features

- ✅ Database-backed user authentication (replaced basic auth)
- ✅ Role-based access control (ADMIN, USER)
- ✅ Password encryption using BCrypt
- ✅ User CRUD operations via REST API
- ✅ PostgreSQL database integration

## Default Users

The application creates two default users on startup:

| Username | Password | Role  | Email |
|----------|----------|-------|-------|
| admin    | admin123 | ADMIN | admin@example.com |
| user     | user123  | USER  | user@example.com |

## API Endpoints

### Authentication
All endpoints require HTTP Basic Authentication.

### User Management (Admin Only)
- `GET /api/users` - List all users
- `POST /api/users` - Create a new user
- `PUT /api/users/{id}` - Update user details
- `DELETE /api/users/{id}` - Delete a user
- `PUT /api/users/{id}/password` - Change user password (Admin or self)

### Public Endpoints
- `GET /actuator/**` - Application monitoring endpoints (configured separately)

## System Monitoring with Spring Boot Actuator

The application includes a comprehensive System Monitoring dashboard accessible through the admin interface.

### Accessing the Monitoring Dashboard

1. **Login as admin** (username: `admin`, password: `admin123`)
2. **Navigate to the dashboard** at `/dashboard`
3. **Click "System Monitoring"** in the admin section
4. **View comprehensive system metrics** and health information

### Available Monitoring Features

- **Application Health**: Overall system health and component status
- **Application Info**: Build information, version, and metadata
- **Environment Properties**: Configuration and environment variables
- **Metrics**: Performance metrics, JVM stats, and request statistics
- **Configuration Properties**: All application configuration values
- **Spring Beans**: Bean registry and dependency information
- **Request Mappings**: All HTTP endpoints and their handlers
- **Thread Dump**: JVM thread analysis for performance troubleshooting
- **Log Levels**: Runtime log level viewing and modification

### Actuator Configuration for Consumer Applications

If you're using this as a starter dependency in your own application, you need to:

1. **Add Actuator Dependency**:
   ```kotlin
   // Gradle
   implementation("org.springframework.boot:spring-boot-starter-actuator")
   ```

2. **Configure Actuator Endpoints** in your `application.properties`:
   ```properties
   # Expose actuator endpoints
   management.endpoints.web.exposure.include=*
   management.endpoint.health.show-details=when-authorized
   
   # Application information for monitoring dashboard
   info.app.name=Your Application Name
   info.app.description=Your application description
   info.app.version=1.0.0
   info.app.encoding=@project.build.sourceEncoding@
   info.app.java.version=@java.version@
   ```

3. **Restart your application** to enable actuator endpoints

### Security

- Actuator endpoints are restricted to admin users only
- Detailed health information is shown only to authorized users
- All monitoring data is protected by the same authentication system

## Database Configuration

The application uses PostgreSQL database:
- Database: `core`
- Host: `localhost:5432`
- Username: `robimoller` (current system user)
- Password: (none)

## Testing the API

### List all users (Admin only):
```bash
curl -u admin:admin123 http://localhost:8080/api/users
```

### Create a new user (Admin only):
```bash
curl -u admin:admin123 -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"username":"newuser","password":"password123","email":"newuser@example.com","role":"USER"}'
```

### Update a user (Admin only):
```bash
curl -u admin:admin123 -X PUT http://localhost:8080/api/users/3 \
  -H "Content-Type: application/json" \
  -d '{"username":"updateduser","email":"updated@example.com","role":"USER","enabled":true}'
```

### Change password:
```bash
curl -u admin:admin123 -X PUT http://localhost:8080/api/users/3/password \
  -H "Content-Type: application/json" \
  -d '{"newPassword":"newpassword123"}'
```

### Delete a user (Admin only):
```bash
curl -u admin:admin123 -X DELETE http://localhost:8080/api/users/3
```

## Security Features

- Passwords are encrypted using BCrypt
- Role-based access control with method-level security
- Admin users can manage all users
- Regular users can only change their own password
- Failed authentication returns 401 Unauthorized
- Insufficient permissions return 403 Forbidden

## Running the Application

1. Ensure PostgreSQL is running locally
2. Create the database: `psql -c "CREATE DATABASE core;"`
3. Run the application: `./gradlew bootRun`
4. Application will be available at `http://localhost:8080`
