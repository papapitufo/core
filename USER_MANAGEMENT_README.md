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
- `GET /actuator/**` - Application monitoring endpoints

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
