package view;

import controller.UserController;
import model.User;
import utils.ValidationUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Frame untuk halaman registrasi user baru
 */
public class RegisterFrame extends JFrame {
    private JTextField fullNameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JButton registerButton;
    private JLabel statusLabel;

    private UserController userController;

    public RegisterFrame() {
        userController = new UserController();
        initComponents();
    }

    private void initComponents() {
        setTitle("VolSys - Register");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 500);
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
                Color color1 = new Color(46, 204, 113);
                Color color2 = new Color(39, 174, 96);
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        mainPanel.setLayout(new GridBagLayout());

        // Register card panel
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(BorderFactory.createEmptyBorder(25, 40, 25, 40));
        cardPanel.setPreferredSize(new Dimension(380, 420));

        // Title
        JLabel titleLabel = new JLabel("Daftar Akun Baru");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(46, 204, 113));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Bergabung sebagai Volunteer");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitleLabel.setForeground(Color.GRAY);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Full Name field
        JLabel nameLabel = new JLabel("Nama Lengkap");
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        nameLabel.setForeground(new Color(50, 50, 50));
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        fullNameField = createTextField();

        // Email field
        JLabel emailLabel = new JLabel("Email");
        emailLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        emailLabel.setForeground(new Color(50, 50, 50));
        emailLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        emailField = createTextField();

        // Password field
        JLabel passwordLabel = new JLabel("Password (min. 6 karakter)");
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

        // Confirm Password field
        JLabel confirmLabel = new JLabel("Konfirmasi Password");
        confirmLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        confirmLabel.setForeground(new Color(50, 50, 50));
        confirmLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        confirmPasswordField = new JPasswordField();
        confirmPasswordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        confirmPasswordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        confirmPasswordField.setBackground(Color.WHITE);
        confirmPasswordField.setForeground(Color.BLACK);
        confirmPasswordField.setCaretColor(Color.BLACK);
        confirmPasswordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));

        // Register button
        registerButton = new JButton("DAFTAR");
        registerButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        registerButton.setBackground(new Color(46, 204, 113));
        registerButton.setForeground(Color.BLACK);
        registerButton.setFocusPainted(false);
        registerButton.setBorderPainted(false);
        registerButton.setOpaque(true);
        registerButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        registerButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                registerButton.setBackground(new Color(39, 174, 96));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                registerButton.setBackground(new Color(46, 204, 113));
            }
        });

        // Status label
        statusLabel = new JLabel(" ");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        statusLabel.setForeground(Color.RED);
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        statusLabel.setPreferredSize(new Dimension(280, 30));
        statusLabel.setMaximumSize(new Dimension(280, 30));

        // Login link
        JPanel loginPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        loginPanel.setBackground(Color.WHITE);
        loginPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        JLabel loginTextLabel = new JLabel("Sudah punya akun?");
        loginTextLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        loginTextLabel.setForeground(Color.GRAY);

        JLabel loginLink = new JLabel("Login di sini");
        loginLink.setFont(new Font("Segoe UI", Font.BOLD, 12));
        loginLink.setForeground(new Color(41, 128, 185));
        loginLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                LoginFrame loginFrame = new LoginFrame();
                loginFrame.setVisible(true);
                dispose();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                loginLink.setText("<html><u>Login di sini</u></html>");
            }

            @Override
            public void mouseExited(MouseEvent e) {
                loginLink.setText("Login di sini");
            }
        });

        loginPanel.add(loginTextLabel);
        loginPanel.add(loginLink);

        // Add components to card
        cardPanel.add(titleLabel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        cardPanel.add(subtitleLabel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        cardPanel.add(nameLabel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        cardPanel.add(fullNameField);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 12)));
        cardPanel.add(emailLabel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        cardPanel.add(emailField);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 12)));
        cardPanel.add(passwordLabel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        cardPanel.add(passwordField);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 12)));
        cardPanel.add(confirmLabel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        cardPanel.add(confirmPasswordField);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        cardPanel.add(registerButton);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        cardPanel.add(statusLabel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        cardPanel.add(loginPanel);

        mainPanel.add(cardPanel);
        add(mainPanel);

        // Action listeners
        registerButton.addActionListener(e -> performRegister());
        confirmPasswordField.addActionListener(e -> performRegister());
    }

    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        field.setBackground(Color.WHITE);
        field.setForeground(Color.BLACK);
        field.setCaretColor(Color.BLACK);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        return field;
    }

    private void performRegister() {
        String fullName = fullNameField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        // Validation
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

        if (!password.equals(confirmPassword)) {
            showError("Password tidak cocok!");
            confirmPasswordField.requestFocus();
            return;
        }

        // Check email exists
        if (userController.isEmailExists(email, 0)) {
            showError("Email sudah terdaftar!");
            emailField.requestFocus();
            return;
        }

        // Create user
        User newUser = new User(fullName, email, password, "user");

        if (userController.createUser(newUser)) {
            JOptionPane.showMessageDialog(this,
                    "Registrasi berhasil!\nSilakan login dengan akun Anda.",
                    "Sukses",
                    JOptionPane.INFORMATION_MESSAGE);

            // Go to login
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
            dispose();
        } else {
            showError("Gagal mendaftar! Coba lagi.");
        }
    }

    private void showError(String message) {
        statusLabel.setForeground(Color.RED);
        statusLabel.setText("<html><center>" + message + "</center></html>");
    }
}
