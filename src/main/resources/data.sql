-- Insert default admin user
INSERT INTO users (username, password, email, role, enabled, created_at) VALUES 
('admin', '$2a$10$8L8W8qNr9.5K8qNr9.5K8O8L8W8qNr9.5K8qNr9.5K8O8L8W8qNr9.', 'admin@example.com', 'ADMIN', true, NOW())
ON DUPLICATE KEY UPDATE username = username;

-- Insert default regular user
INSERT INTO users (username, password, email, role, enabled, created_at) VALUES 
('user', '$2a$10$8L8W8qNr9.5K8qNr9.5K8O8L8W8qNr9.5K8qNr9.5K8O8L8W8qNr9.', 'user@example.com', 'USER', true, NOW())
ON DUPLICATE KEY UPDATE username = username;

-- Note: Permissions and roles will be initialized automatically by DataInitializationService
-- This ensures that the permission system is properly set up on application startup
