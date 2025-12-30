package controller;

import config.DatabaseConnection;
import model.Registration;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller untuk operasi CRUD pada tabel registrations
 */
public class RegistrationController {
    private Connection connection;
    
    public RegistrationController() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }
    
    /**
     * Mengambil semua registrations dengan join user dan event
     */
    public List<Registration> getAllRegistrations() {
        List<Registration> registrations = new ArrayList<>();
        String query = "SELECT r.*, u.full_name as user_name, u.email as user_email, " +
                       "e.title as event_title, e.event_date, e.location as event_location " +
                       "FROM registrations r " +
                       "JOIN users u ON r.user_id = u.id " +
                       "JOIN events e ON r.event_id = e.id " +
                       "ORDER BY r.registered_at DESC";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                Registration reg = new Registration(
                    rs.getInt("id"),
                    rs.getInt("user_id"),
                    rs.getInt("event_id"),
                    rs.getString("status"),
                    rs.getTimestamp("registered_at")
                );
                reg.setUserName(rs.getString("user_name"));
                reg.setUserEmail(rs.getString("user_email"));
                reg.setEventTitle(rs.getString("event_title"));
                reg.setEventDate(rs.getTimestamp("event_date"));
                reg.setEventLocation(rs.getString("event_location"));
                registrations.add(reg);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching registrations: " + e.getMessage());
        }
        
        return registrations;
    }
    
    /**
     * Mengambil registrations berdasarkan user ID
     */
    public List<Registration> getRegistrationsByUser(int userId) {
        List<Registration> registrations = new ArrayList<>();
        String query = "SELECT r.*, u.full_name as user_name, u.email as user_email, " +
                       "e.title as event_title, e.event_date, e.location as event_location " +
                       "FROM registrations r " +
                       "JOIN users u ON r.user_id = u.id " +
                       "JOIN events e ON r.event_id = e.id " +
                       "WHERE r.user_id = ? " +
                       "ORDER BY e.event_date ASC";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Registration reg = new Registration(
                    rs.getInt("id"),
                    rs.getInt("user_id"),
                    rs.getInt("event_id"),
                    rs.getString("status"),
                    rs.getTimestamp("registered_at")
                );
                reg.setUserName(rs.getString("user_name"));
                reg.setUserEmail(rs.getString("user_email"));
                reg.setEventTitle(rs.getString("event_title"));
                reg.setEventDate(rs.getTimestamp("event_date"));
                reg.setEventLocation(rs.getString("event_location"));
                registrations.add(reg);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching user registrations: " + e.getMessage());
        }
        
        return registrations;
    }
    
    /**
     * Mengambil registration berdasarkan ID
     */
    public Registration getRegistrationById(int id) {
        String query = "SELECT r.*, u.full_name as user_name, u.email as user_email, " +
                       "e.title as event_title, e.event_date, e.location as event_location " +
                       "FROM registrations r " +
                       "JOIN users u ON r.user_id = u.id " +
                       "JOIN events e ON r.event_id = e.id " +
                       "WHERE r.id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Registration reg = new Registration(
                    rs.getInt("id"),
                    rs.getInt("user_id"),
                    rs.getInt("event_id"),
                    rs.getString("status"),
                    rs.getTimestamp("registered_at")
                );
                reg.setUserName(rs.getString("user_name"));
                reg.setUserEmail(rs.getString("user_email"));
                reg.setEventTitle(rs.getString("event_title"));
                reg.setEventDate(rs.getTimestamp("event_date"));
                reg.setEventLocation(rs.getString("event_location"));
                return reg;
            }
        } catch (SQLException e) {
            System.err.println("Error fetching registration: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Membuat registration baru
     */
    public boolean createRegistration(Registration registration) {
        String query = "INSERT INTO registrations (user_id, event_id, status) VALUES (?, ?, ?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, registration.getUserId());
            pstmt.setInt(2, registration.getEventId());
            pstmt.setString(3, registration.getStatus());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error creating registration: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Mengupdate registration status
     */
    public boolean updateRegistration(Registration registration) {
        String query = "UPDATE registrations SET user_id = ?, event_id = ?, status = ? WHERE id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, registration.getUserId());
            pstmt.setInt(2, registration.getEventId());
            pstmt.setString(3, registration.getStatus());
            pstmt.setInt(4, registration.getId());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating registration: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Menghapus registration
     */
    public boolean deleteRegistration(int id) {
        String query = "DELETE FROM registrations WHERE id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting registration: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Cek apakah user sudah terdaftar ke event tertentu
     */
    public boolean isAlreadyRegistered(int userId, int eventId) {
        String query = "SELECT COUNT(*) FROM registrations WHERE user_id = ? AND event_id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, eventId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking registration: " + e.getMessage());
        }
        
        return false;
    }
}
