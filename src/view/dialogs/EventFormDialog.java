package view.dialogs;

import controller.AuthController;
import controller.EventController;
import model.Event;
import utils.ValidationUtils;

import javax.swing.*;
import java.awt.*;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Dialog form untuk tambah/edit Event
 */
public class EventFormDialog extends JDialog {
    private JTextField titleField;
    private JTextArea descriptionArea;
    private JTextField locationField;
    private JTextField dateField;
    private JComboBox<String> statusCombo;
    private JButton saveButton;
    private JButton cancelButton;
    
    private EventController eventController;
    private Event event;
    private boolean saved = false;
    
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    
    public EventFormDialog(JFrame parent, String title, Event event) {
        super(parent, title, true);
        this.eventController = new EventController();
        this.event = event;
        initComponents();
        if (event != null) {
            loadEventData();
        }
    }
    
    private void initComponents() {
        setSize(500, 450);
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
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.anchor = GridBagConstraints.NORTHWEST;
        
        // Title
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(createLabel("Judul Event *"), gbc);
        
        gbc.gridx = 1; gbc.weightx = 1;
        titleField = createTextField();
        formPanel.add(titleField, gbc);
        
        // Description
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        formPanel.add(createLabel("Deskripsi"), gbc);
        
        gbc.gridx = 1; gbc.weightx = 1;
        descriptionArea = new JTextArea(4, 20);
        descriptionArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        JScrollPane descScroll = new JScrollPane(descriptionArea);
        descScroll.setPreferredSize(new Dimension(250, 80));
        formPanel.add(descScroll, gbc);
        
        // Location
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        formPanel.add(createLabel("Lokasi *"), gbc);
        
        gbc.gridx = 1; gbc.weightx = 1;
        locationField = createTextField();
        formPanel.add(locationField, gbc);
        
        // Date
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        formPanel.add(createLabel("Tanggal Event *"), gbc);
        
        gbc.gridx = 1; gbc.weightx = 1;
        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        datePanel.setBackground(Color.WHITE);
        dateField = createTextField();
        dateField.setPreferredSize(new Dimension(180, 35));
        JLabel dateHint = new JLabel("  (yyyy-MM-dd HH:mm)");
        dateHint.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        dateHint.setForeground(Color.GRAY);
        datePanel.add(dateField);
        datePanel.add(dateHint);
        formPanel.add(datePanel, gbc);
        
        // Status
        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0;
        formPanel.add(createLabel("Status *"), gbc);
        
        gbc.gridx = 1; gbc.weightx = 1;
        statusCombo = new JComboBox<>(new String[]{"upcoming", "ongoing", "completed", "cancelled"});
        statusCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        statusCombo.setPreferredSize(new Dimension(200, 35));
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
        saveButton.addActionListener(e -> saveEvent());
        
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
    
    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(250, 35));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return field;
    }
    
    private void loadEventData() {
        titleField.setText(event.getTitle());
        descriptionArea.setText(event.getDescription());
        locationField.setText(event.getLocation());
        if (event.getEventDate() != null) {
            dateField.setText(dateFormat.format(event.getEventDate()));
        }
        statusCombo.setSelectedItem(event.getStatus());
    }
    
    private void saveEvent() {
        // Validation
        String title = titleField.getText().trim();
        String description = descriptionArea.getText().trim();
        String location = locationField.getText().trim();
        String dateStr = dateField.getText().trim();
        String status = (String) statusCombo.getSelectedItem();
        
        if (!ValidationUtils.isNotEmpty(title)) {
            showError("Judul event tidak boleh kosong!");
            titleField.requestFocus();
            return;
        }
        
        if (!ValidationUtils.isNotEmpty(location)) {
            showError("Lokasi tidak boleh kosong!");
            locationField.requestFocus();
            return;
        }
        
        if (!ValidationUtils.isNotEmpty(dateStr)) {
            showError("Tanggal event tidak boleh kosong!");
            dateField.requestFocus();
            return;
        }
        
        // Parse date
        Timestamp eventDate;
        try {
            java.util.Date parsedDate = dateFormat.parse(dateStr);
            eventDate = new Timestamp(parsedDate.getTime());
        } catch (ParseException e) {
            showError("Format tanggal tidak valid!\nGunakan format: yyyy-MM-dd HH:mm");
            dateField.requestFocus();
            return;
        }
        
        // Save
        boolean success;
        if (event == null) {
            // Create new
            int createdBy = AuthController.getCurrentUser().getId();
            Event newEvent = new Event(title, description, location, eventDate, createdBy, status);
            success = eventController.createEvent(newEvent);
        } else {
            // Update
            event.setTitle(title);
            event.setDescription(description);
            event.setLocation(location);
            event.setEventDate(eventDate);
            event.setStatus(status);
            success = eventController.updateEvent(event);
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
}
