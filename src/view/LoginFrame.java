package view;

import controller.AuthController;
import model.User;
import utils.ValidationUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Frame untuk halaman login
 */
public class LoginFrame extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel statusLabel;

    private AuthController authController;

    public LoginFrame() {
        authController = new AuthController();
        initComponents();
    }

    private void initComponents() {
        setTitle("VolSys - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 420);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main panel with gradient background
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth(), h = getHeight();
                Color color1 = new Color(41, 128, 185);
                Color color2 = new Color(44, 62, 80);
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        mainPanel.setLayout(new GridBagLayout());

        // Login card panel
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        cardPanel.setPreferredSize(new Dimension(350, 350));

        // Title
        JLabel titleLabel = new JLabel("VolSys");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(41, 128, 185));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Volunteer Management System");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitleLabel.setForeground(Color.GRAY);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Email field
        JLabel emailLabel = new JLabel("Email");
        emailLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        emailLabel.setForeground(new Color(50, 50, 50));
        emailLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        emailField = new JTextField();
        emailField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        emailField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        emailField.setBackground(Color.WHITE);
        emailField.setForeground(Color.BLACK);
        emailField.setCaretColor(Color.BLACK);
        emailField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));

        // Password field
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        passwordLabel.setForeground(new Color(50, 50, 50));
        passwordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        passwordField.setBackground(Color.WHITE);
        passwordField.setForeground(Color.BLACK);
        passwordField.setCaretColor(Color.BLACK);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));

        // Login button - menggunakan warna gelap agar kontras
        loginButton = new JButton("LOGIN");
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginButton.setBackground(new Color(41, 128, 185));
        loginButton.setForeground(Color.BLACK); // Warna font hitam agar terlihat
        loginButton.setFocusPainted(false);
        loginButton.setBorderPainted(false);
        loginButton.setOpaque(true);
        loginButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Hover effect
        loginButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                loginButton.setBackground(new Color(52, 152, 219));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                loginButton.setBackground(new Color(41, 128, 185));
            }
        });

        // Status label
        statusLabel = new JLabel(" ");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        statusLabel.setForeground(Color.RED);
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        statusLabel.setPreferredSize(new Dimension(270, 35));
        statusLabel.setMinimumSize(new Dimension(270, 35));
        statusLabel.setMaximumSize(new Dimension(270, 35));

        // Register link panel
        JPanel registerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        registerPanel.setBackground(Color.WHITE);
        registerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        JLabel registerTextLabel = new JLabel("Belum punya akun?");
        registerTextLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        registerTextLabel.setForeground(Color.GRAY);

        JLabel registerLink = new JLabel("Daftar di sini");
        registerLink.setFont(new Font("Segoe UI", Font.BOLD, 12));
        registerLink.setForeground(new Color(46, 204, 113));
        registerLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                RegisterFrame registerFrame = new RegisterFrame();
                registerFrame.setVisible(true);
                dispose();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                registerLink.setText("<html><u>Daftar di sini</u></html>");
            }

            @Override
            public void mouseExited(MouseEvent e) {
                registerLink.setText("Daftar di sini");
            }
        });

        registerPanel.add(registerTextLabel);
        registerPanel.add(registerLink);

        // Add components to card
        cardPanel.add(titleLabel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        cardPanel.add(subtitleLabel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        cardPanel.add(emailLabel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        cardPanel.add(emailField);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        cardPanel.add(passwordLabel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        cardPanel.add(passwordField);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        cardPanel.add(loginButton);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        cardPanel.add(statusLabel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        cardPanel.add(registerPanel);

        mainPanel.add(cardPanel);
        add(mainPanel);

        // Action listeners
        loginButton.addActionListener(e -> performLogin());
        passwordField.addActionListener(e -> performLogin());
        emailField.addActionListener(e -> passwordField.requestFocus());
    }

    private void performLogin() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());

        // Debug output
        System.out.println("=== LOGIN ATTEMPT ===");
        System.out.println("Email: " + email);
        System.out.println("Password length: " + password.length());

        // Validation
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

        // Attempt login
        statusLabel.setForeground(new Color(41, 128, 185));
        statusLabel.setText("Mencoba login...");

        User user = authController.login(email, password);

        if (user != null) {
            statusLabel.setForeground(new Color(39, 174, 96));
            statusLabel.setText("Login berhasil! Redirecting...");

            // Open main frame
            Timer timer = new Timer(500, e -> {
                MainFrame mainFrame = new MainFrame();
                mainFrame.setVisible(true);
                dispose();
            });
            timer.setRepeats(false);
            timer.start();
        } else {
            // Show specific error from controller
            String error = authController.getLastError();
            if (error != null) {
                showError(error);
            } else {
                showError("Email atau password salah!");
            }
            passwordField.setText("");
            passwordField.requestFocus();
        }
    }

    private void showError(String message) {
        statusLabel.setForeground(Color.RED);
        statusLabel.setText("<html><center>" + message + "</center></html>");
        System.out.println("ERROR: " + message);
    }
}
