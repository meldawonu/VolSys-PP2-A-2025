package view;

import controller.AuthController;
import model.User;
import view.panels.*;

import javax.swing.*;
import java.awt.*;

/**
 * Frame utama aplikasi dengan JTabbedPane
 */
public class MainFrame extends JFrame {
    private JTabbedPane tabbedPane;
    private User currentUser;
    
    public MainFrame() {
        this.currentUser = AuthController.getCurrentUser();
        initComponents();
    }
    
    private void initComponents() {
        setTitle("VolSys - Volunteer Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(900, 600));
        
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(236, 240, 241));
        
        // Header panel
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Tabbed pane
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabbedPane.setBackground(Color.WHITE);
        
        if (currentUser.isAdmin()) {
            // Admin tabs
            tabbedPane.addTab("📊 Dashboard", createDashboardPanel());
            tabbedPane.addTab("👥 Kelola Users", new UserManagementPanel());
            tabbedPane.addTab("📅 Kelola Events", new EventManagementPanel());
            tabbedPane.addTab("📝 Kelola Registrations", new RegistrationManagementPanel());
        } else {
            // User tabs
            tabbedPane.addTab("📅 Daftar Event", new UserEventPanel());
            tabbedPane.addTab("📋 Event Saya", new MyRegistrationsPanel());
        }
        
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        add(mainPanel);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(41, 128, 185));
        headerPanel.setPreferredSize(new Dimension(0, 60));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        // Logo and title
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftPanel.setOpaque(false);
        
        JLabel logoLabel = new JLabel("🤝");
        logoLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        
        JLabel titleLabel = new JLabel("VolSys");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        
        leftPanel.add(logoLabel);
        leftPanel.add(titleLabel);
        
        // User info and logout
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightPanel.setOpaque(false);
        
        JLabel userLabel = new JLabel(currentUser.getFullName() + " (" + currentUser.getRole().toUpperCase() + ")");
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userLabel.setForeground(Color.WHITE);
        
        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        logoutButton.setBackground(new Color(231, 76, 60));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFocusPainted(false);
        logoutButton.setBorderPainted(false);
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutButton.setPreferredSize(new Dimension(80, 30));
        
        logoutButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "Apakah Anda yakin ingin logout?",
                "Konfirmasi Logout",
                JOptionPane.YES_NO_OPTION
            );
            
            if (confirm == JOptionPane.YES_OPTION) {
                new AuthController().logout();
                LoginFrame loginFrame = new LoginFrame();
                loginFrame.setVisible(true);
                dispose();
            }
        });
        
        rightPanel.add(userLabel);
        rightPanel.add(logoutButton);
        
        headerPanel.add(leftPanel, BorderLayout.WEST);
        headerPanel.add(rightPanel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        
        // Welcome message
        JLabel welcomeLabel = new JLabel("Selamat Datang, " + currentUser.getFullName() + "!");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        welcomeLabel.setForeground(new Color(44, 62, 80));
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        panel.add(welcomeLabel, gbc);
        
        // Info cards
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        
        JPanel usersCard = createInfoCard("👥", "Users", "Kelola data pengguna", new Color(52, 152, 219));
        JPanel eventsCard = createInfoCard("📅", "Events", "Kelola event volunteer", new Color(46, 204, 113));
        JPanel regsCard = createInfoCard("📝", "Registrations", "Kelola pendaftaran", new Color(155, 89, 182));
        
        gbc.gridx = 0;
        panel.add(usersCard, gbc);
        gbc.gridx = 1;
        panel.add(eventsCard, gbc);
        gbc.gridx = 2;
        panel.add(regsCard, gbc);
        
        // Instructions
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 3;
        JLabel instructionLabel = new JLabel("Gunakan tab di atas untuk mengelola data");
        instructionLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        instructionLabel.setForeground(Color.GRAY);
        panel.add(instructionLabel, gbc);
        
        return panel;
    }
    
    private JPanel createInfoCard(String icon, String title, String description, Color color) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(color);
        card.setPreferredSize(new Dimension(200, 150));
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel descLabel = new JLabel(description);
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        descLabel.setForeground(new Color(255, 255, 255, 200));
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        card.add(iconLabel);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(titleLabel);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(descLabel);
        
        return card;
    }
}
