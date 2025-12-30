package model;

import java.sql.Timestamp;


public class Event {
    private int id;
    private String title;
    private String description;
    private String location;
    private Timestamp eventDate;
    private int createdBy;
    private String status;
    

    private String creatorName;
    
    public Event() {}
    
    public Event(int id, String title, String description, String location, 
                 Timestamp eventDate, int createdBy, String status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.eventDate = eventDate;
        this.createdBy = createdBy;
        this.status = status;
    }
    
    public Event(String title, String description, String location, 
                 Timestamp eventDate, int createdBy, String status) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.eventDate = eventDate;
        this.createdBy = createdBy;
        this.status = status;
    }
    

    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public Timestamp getEventDate() {
        return eventDate;
    }
    
    public void setEventDate(Timestamp eventDate) {
        this.eventDate = eventDate;
    }
    
    public int getCreatedBy() {
        return createdBy;
    }
    
    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getCreatorName() {
        return creatorName;
    }
    
    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }
    
    @Override
    public String toString() {
        return title;
    }
}
