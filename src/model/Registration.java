package model;

import java.sql.Timestamp;

public class Registration {
    private int id;
    private int userId;
    private int eventId;
    private String status;
    private Timestamp registeredAt;
    

    private String userName;
    private String userEmail;
    private String eventTitle;
    private Timestamp eventDate;
    private String eventLocation;
    
    public Registration() {}
    
    public Registration(int id, int userId, int eventId, String status, Timestamp registeredAt) {
        this.id = id;
        this.userId = userId;
        this.eventId = eventId;
        this.status = status;
        this.registeredAt = registeredAt;
    }
    
    public Registration(int userId, int eventId, String status) {
        this.userId = userId;
        this.eventId = eventId;
        this.status = status;
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public int getEventId() {
        return eventId;
    }
    
    public void setEventId(int eventId) {
        this.eventId = eventId;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public Timestamp getRegisteredAt() {
        return registeredAt;
    }
    
    public void setRegisteredAt(Timestamp registeredAt) {
        this.registeredAt = registeredAt;
    }
    
    public String getUserName() {
        return userName;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public String getUserEmail() {
        return userEmail;
    }
    
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
    
    public String getEventTitle() {
        return eventTitle;
    }
    
    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }
    
    public Timestamp getEventDate() {
        return eventDate;
    }
    
    public void setEventDate(Timestamp eventDate) {
        this.eventDate = eventDate;
    }
    
    public String getEventLocation() {
        return eventLocation;
    }
    
    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }
}
