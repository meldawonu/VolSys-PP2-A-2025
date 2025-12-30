package controller;

import config.DatabaseConnection;
import model.Event;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller untuk operasi CRUD pada tabel events
 */
public class EventController {
    private Connection connection;
    
    public EventController() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }
    
    /**
     * Mengambil semua event dari database
     */
    public List<Event> getAllEvents() {
        List<Event> events = new ArrayList<>();
        String query = "SELECT e.*, u.full_name as creator_name FROM events e " +
                       "LEFT JOIN users u ON e.created_by = u.id ORDER BY e.event_date DESC";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                Event event = new Event(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("description"),
                    rs.getString("location"),
                    rs.getTimestamp("event_date"),
                    rs.getInt("created_by"),
                    rs.getString("status")
                );
                event.setCreatorName(rs.getString("creator_name"));
                events.add(event);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching events: " + e.getMessage());
        }
        
        return events;
    }
    
    /**
     * Mengambil event yang tersedia (upcoming) untuk user
     */
    public List<Event> getAvailableEvents() {
        List<Event> events = new ArrayList<>();
        String query = "SELECT e.*, u.full_name as creator_name FROM events e " +
                       "LEFT JOIN users u ON e.created_by = u.id " +
                       "WHERE e.status = 'upcoming' AND e.event_date > NOW() " +
                       "ORDER BY e.event_date ASC";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                Event event = new Event(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("description"),
                    rs.getString("location"),
                    rs.getTimestamp("event_date"),
                    rs.getInt("created_by"),
                    rs.getString("status")
                );
                event.setCreatorName(rs.getString("creator_name"));
                events.add(event);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching available events: " + e.getMessage());
        }
        
        return events;
    }
    
    /**
     * Mengambil event berdasarkan ID
     */
    public Event getEventById(int id) {
        String query = "SELECT e.*, u.full_name as creator_name FROM events e " +
                       "LEFT JOIN users u ON e.created_by = u.id WHERE e.id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Event event = new Event(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("description"),
                    rs.getString("location"),
                    rs.getTimestamp("event_date"),
                    rs.getInt("created_by"),
                    rs.getString("status")
                );
                event.setCreatorName(rs.getString("creator_name"));
                return event;
            }
        } catch (SQLException e) {
            System.err.println("Error fetching event: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Membuat event baru
     */
    public boolean createEvent(Event event) {
        String query = "INSERT INTO events (title, description, location, event_date, created_by, status) " +
                       "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, event.getTitle());
            pstmt.setString(2, event.getDescription());
            pstmt.setString(3, event.getLocation());
            pstmt.setTimestamp(4, event.getEventDate());
            pstmt.setInt(5, event.getCreatedBy());
            pstmt.setString(6, event.getStatus());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error creating event: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Mengupdate event
     */
    public boolean updateEvent(Event event) {
        String query = "UPDATE events SET title = ?, description = ?, location = ?, " +
                       "event_date = ?, status = ? WHERE id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, event.getTitle());
            pstmt.setString(2, event.getDescription());
            pstmt.setString(3, event.getLocation());
            pstmt.setTimestamp(4, event.getEventDate());
            pstmt.setString(5, event.getStatus());
            pstmt.setInt(6, event.getId());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating event: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Menghapus event
     */
    public boolean deleteEvent(int id) {
        String query = "DELETE FROM events WHERE id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting event: " + e.getMessage());
            return false;
        }
    }
}
