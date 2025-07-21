-- Insert default admin user
INSERT INTO users (username, password, email, role, enabled, created_at) VALUES 
('admin', '$2a$10$8L8W8qNr9.5K8qNr9.5K8O8L8W8qNr9.5K8qNr9.5K8O8L8W8qNr9.', 'admin@example.com', 'ADMIN', true, NOW());

-- Insert default regular user
INSERT INTO users (username, password, email, role, enabled, created_at) VALUES 
('user', '$2a$10$8L8W8qNr9.5K8qNr9.5K8O8L8W8qNr9.5K8qNr9.5K8O8L8W8qNr9.', 'user@example.com', 'USER', true, NOW());
