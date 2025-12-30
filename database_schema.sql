-- VolSys (Volunteer System) Database Schema
-- Jalankan script ini di MySQL untuk membuat database

CREATE DATABASE IF NOT EXISTS volsys;
USE volsys;

-- Tabel Users
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'user',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabel Events
CREATE TABLE IF NOT EXISTS events (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    location VARCHAR(255),
    event_date DATETIME NOT NULL,
    created_by INT,
    status VARCHAR(20) DEFAULT 'upcoming',
    FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE SET NULL
);

-- Tabel Registrations
CREATE TABLE IF NOT EXISTS registrations (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    event_id INT NOT NULL,
    status VARCHAR(20) DEFAULT 'pending',
    registered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE,
    UNIQUE KEY unique_registration (user_id, event_id)
);

-- Insert default admin user (password: admin123)
INSERT INTO users (full_name, email, password, role) VALUES 
('Administrator', 'admin@volsys.com', 'admin123', 'admin');

-- Insert sample user
INSERT INTO users (full_name, email, password, role) VALUES 
('John Doe', 'john@example.com', 'password123', 'user');

-- Insert sample events
INSERT INTO events (title, description, location, event_date, created_by, status) VALUES 
('Beach Cleanup', 'Kegiatan membersihkan pantai dari sampah plastik', 'Pantai Ancol, Jakarta', '2025-01-15 08:00:00', 1, 'upcoming'),
('Blood Donation Drive', 'Donor darah untuk kemanusiaan', 'RS Fatmawati, Jakarta', '2025-01-20 09:00:00', 1, 'upcoming'),
('Tree Planting', 'Penanaman 1000 pohon untuk penghijauan', 'Taman Kota BSD', '2025-02-01 07:00:00', 1, 'upcoming');
