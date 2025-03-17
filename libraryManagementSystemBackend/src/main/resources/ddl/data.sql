-- System Configuration
INSERT INTO config (key, value, description) VALUES
  ('daily_fine_rate', '10', 'Fine per day for overdue books'),
  ('max_books_per_user', '5', 'Maximum books a patron can borrow'),
  ('loan_duration_days', '1', 'Default loan period');

-- Initial Admin User (password: admin123)
INSERT INTO users (id, first_name, last_name, email, password, role) VALUES
  (uuid_generate_v4(), 'Admin', 'User', 'admin@library.com','$2a$10$rwiOsIIG6e9BESMemg.53ehHEMegpoOWi5b.HEtrvVPkR.H1zhM96', 'ADMIN');