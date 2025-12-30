package view.dialogs;

import controller.EventController;
import controller.RegistrationController;
import controller.UserController;
import model.Event;
import model.Registration;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Dialog form untuk tambah/edit Registration
 */
public class RegistrationFormDialog extends JDialog {
    private JComboBox<UserItem> userCombo;
    private JComboBox<EventItem> eventCombo;
    private JComboBox<String> statusCombo;
    private JButton saveButton;
    private JButton cancelButton;
    
    private RegistrationController registrationController;
    private UserController userController;
    private EventController eventController;
    private Registration registration;
    private boolean saved = false;
    
    public RegistrationFormDialog(JFrame parent, String title, Registration registration) {
        super(parent, title, true);
        this.registrationController = new RegistrationController();
        this.userController = new UserController();
        this.eventController = new EventController();
        this.registration = registration;
        initComponents();
        loadComboData();
        if (registration != null) {
            loadRegistrationData();
        }
    }
    
    private void initComponents() {
        setSize(450, 300);
        setLocationRelativeTo(getParent());
        setResizable(false);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 5, 10, 5);
        
        // User
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(createLabel("Volunteer *"), gbc);
        
        gbc.gridx = 1; gbc.weightx = 1;
        userCombo = new JComboBox<>();
        userCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userCombo.setPreferredSize(new Dimension(250, 35));
        userCombo.setBackground(Color.WHITE);
        formPanel.add(userCombo, gbc);
        
        // Event
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        formPanel.add(createLabel("Event *"), gbc);
        
        gbc.gridx = 1; gbc.weightx = 1;
        eventCombo = new JComboBox<>();
        eventCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        eventCombo.setPreferredSize(new Dimension(250, 35));
        eventCombo.setBackground(Color.WHITE);
        formPanel.add(eventCombo, gbc);
        
        // Status
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        formPanel.add(createLabel("Status *"), gbc);
        
        gbc.gridx = 1; gbc.weightx = 1;
        statusCombo = new JComboBox<>(new String[]{"pending", "approved", "rejected"});
        statusCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        statusCombo.setPreferredSize(new Dimension(250, 35));
        statusCombo.setBackground(Color.WHITE);
        formPanel.add(statusCombo, gbc);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(Color.WHITE);
        
        cancelButton = new JButton("Batal");
        cancelButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cancelButton.setPreferredSize(new Dimension(100, 35));
        cancelButton.addActionListener(e -> dispose());
        
        saveButton = new JButton("Simpan");
        saveButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        saveButton.setBackground(new Color(46, 204, 113));
        saveButton.setForeground(Color.WHITE);
        saveButton.setFocusPainted(false);
        saveButton.setBorderPainted(false);
        saveButton.setPreferredSize(new Dimension(100, 35));
        saveButton.addActionListener(e -> saveRegistration());
        
        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);
        
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return label;
    }
    
    private void loadComboData() {
        // Load users
        List<User> users = userController.getAllVolunteers();
        for (User user : users) {
            userCombo.addItem(new UserItem(user.getId(), user.getFullName()));
        }
        
        // Load events
        List<Event> events = eventController.getAllEvents();
        for (Event event : events) {
            eventCombo.addItem(new EventItem(event.getId(), event.getTitle()));
        }
    }
    
    private void loadRegistrationData() {
        // Select user
        for (int i = 0; i < userCombo.getItemCount(); i++) {
            if (userCombo.getItemAt(i).getId() == registration.getUserId()) {
                userCombo.setSelectedIndex(i);
                break;
            }
        }
        
        // Select event
        for (int i = 0; i < eventCombo.getItemCount(); i++) {
            if (eventCombo.getItemAt(i).getId() == registration.getEventId()) {
                eventCombo.setSelectedIndex(i);
                break;
            }
        }
        
        statusCombo.setSelectedItem(registration.getStatus());
    }
    
    private void saveRegistration() {
        // Validation
        UserItem selectedUser = (UserItem) userCombo.getSelectedItem();
        EventItem selectedEvent = (EventItem) eventCombo.getSelectedItem();
        String status = (String) statusCombo.getSelectedItem();
        
        if (selectedUser == null) {
            showError("Pilih volunteer!");
            return;
        }
        
        if (selectedEvent == null) {
            showError("Pilih event!");
            return;
        }
        
        int userId = selectedUser.getId();
        int eventId = selectedEvent.getId();
        
        // Check duplicate registration (only for new registration)
        if (registration == null && registrationController.isAlreadyRegistered(userId, eventId)) {
            showError("Volunteer sudah terdaftar di event ini!");
            return;
        }
        
        // Save
        boolean success;
        if (registration == null) {
            // Create new
            Registration newReg = new Registration(userId, eventId, status);
            success = registrationController.createRegistration(newReg);
        } else {
            // Update
            registration.setUserId(userId);
            registration.setEventId(eventId);
            registration.setStatus(status);
            success = registrationController.updateRegistration(registration);
        }
        
        if (success) {
            saved = true;
            JOptionPane.showMessageDialog(this, 
                "Data berhasil disimpan!", 
                "Sukses", 
                JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            showError("Gagal menyimpan data!");
        }
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    public boolean isSaved() {
        return saved;
    }
    
    // Helper class for User combo item
    private class UserItem {
        private int id;
        private String name;
        
        public UserItem(int id, String name) {
            this.id = id;
            this.name = name;
        }
        
        public int getId() { return id; }
        
        @Override
        public String toString() { return name; }
    }
    
    // Helper class for Event combo item
    private class EventItem {
        private int id;
        private String title;
        
        public EventItem(int id, String title) {
            this.id = id;
            this.title = title;
        }
        
        public int getId() { return id; }
        
        @Override
        public String toString() { return title; }
    }
}
