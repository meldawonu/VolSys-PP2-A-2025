package view.dialogs;

import controller.UserController;
import model.User;
import utils.ValidationUtils;

import javax.swing.*;
import java.awt.*;

/**
 * Dialog form untuk tambah/edit User
 */
public class UserFormDialog extends JDialog {
    private JTextField fullNameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JComboBox<String> roleCombo;
    private JButton saveButton;
    private JButton cancelButton;
    
    private UserController userController;
    private User user;
    private boolean saved = false;
    
    public UserFormDialog(JFrame parent, String title, User user) {
        super(parent, title, true);
        this.userController = new UserController();
        this.user = user;
        initComponents();
        if (user != null) {
            loadUserData();
        }
    }
    
    private void initComponents() {
        setSize(400, 350);
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
        
        // Full Name
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(createLabel("Nama Lengkap *"), gbc);
        
        gbc.gridx = 1; gbc.weightx = 1;
        fullNameField = createTextField();
        formPanel.add(fullNameField, gbc);
        
        // Email
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        formPanel.add(createLabel("Email *"), gbc);
        
        gbc.gridx = 1; gbc.weightx = 1;
        emailField = createTextField();
        formPanel.add(emailField, gbc);
        
        // Password
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        formPanel.add(createLabel("Password *"), gbc);
        
        gbc.gridx = 1; gbc.weightx = 1;
        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setPreferredSize(new Dimension(200, 35));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        formPanel.add(passwordField, gbc);
        
        // Role
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        formPanel.add(createLabel("Role *"), gbc);
        
        gbc.gridx = 1; gbc.weightx = 1;
        roleCombo = new JComboBox<>(new String[]{"user", "admin"});
        roleCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        roleCombo.setPreferredSize(new Dimension(200, 35));
        roleCombo.setBackground(Color.WHITE);
        formPanel.add(roleCombo, gbc);
        
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
        saveButton.addActionListener(e -> saveUser());
        
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
        field.setPreferredSize(new Dimension(200, 35));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return field;
    }
    
    private void loadUserData() {
        fullNameField.setText(user.getFullName());
        emailField.setText(user.getEmail());
        passwordField.setText(user.getPassword());
        roleCombo.setSelectedItem(user.getRole());
    }
    
    private void saveUser() {
        // Validation
        String fullName = fullNameField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        String role = (String) roleCombo.getSelectedItem();
        
        if (!ValidationUtils.isNotEmpty(fullName)) {
            showError("Nama lengkap tidak boleh kosong!");
            fullNameField.requestFocus();
            return;
        }
        
        if (!ValidationUtils.isNotEmpty(email)) {
            showError("Email tidak boleh kosong!");
            emailField.requestFocus();
            return;
        }
        
        if (!ValidationUtils.isValidEmail(email)) {
            showError("Format email tidak valid!");
            emailField.requestFocus();
            return;
        }
        
        if (!ValidationUtils.isNotEmpty(password)) {
            showError("Password tidak boleh kosong!");
            passwordField.requestFocus();
            return;
        }
        
        if (!ValidationUtils.isValidPassword(password)) {
            showError("Password minimal 6 karakter!");
            passwordField.requestFocus();
            return;
        }
        
        // Check email uniqueness
        int excludeId = (user != null) ? user.getId() : 0;
        if (userController.isEmailExists(email, excludeId)) {
            showError("Email sudah digunakan!");
            emailField.requestFocus();
            return;
        }
        
        // Save
        boolean success;
        if (user == null) {
            // Create new
            User newUser = new User(fullName, email, password, role);
            success = userController.createUser(newUser);
        } else {
            // Update
            user.setFullName(fullName);
            user.setEmail(email);
            user.setPassword(password);
            user.setRole(role);
            success = userController.updateUser(user);
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
