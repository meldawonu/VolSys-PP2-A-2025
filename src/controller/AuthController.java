package controller;

import config.DatabaseConnection;
import model.User;

import java.sql.*;

/**
 * Controller untuk autentikasi user
 */
public class AuthController {
    private Connection connection;
    private static User currentUser;
    private String lastError = null;

    public AuthController() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    /**
     * Login user dengan email dan password
     */
    public User login(String email, String password) {
        lastError = null;

        // Check connection
        if (connection == null) {
            lastError = "Database tidak terhubung! Pastikan MySQL berjalan.";
            System.err.println(lastError);
            return null;
        }

        String query = "SELECT * FROM users WHERE email = ? AND password = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                currentUser = new User(
                        rs.getInt("id"),
                        rs.getString("full_name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getTimestamp("created_at"));
                System.out.println("Login berhasil: " + currentUser.getFullName());
                return currentUser;
            } else {
                lastError = "Email atau password salah!";
                System.out.println("Login gagal: email/password tidak cocok");
            }
        } catch (SQLException e) {
            lastError = "Database error: " + e.getMessage();
            System.err.println("Error during login: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Mendapatkan error terakhir
     */
    public String getLastError() {
        return lastError;
    }

    /**
     * Logout user
     */
    public void logout() {
        currentUser = null;
    }

    /**
     * Mendapatkan user yang sedang login
     */
    public static User getCurrentUser() {
        return currentUser;
    }

    /**
     * Set current user (untuk testing)
     */
    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    /**
     * Cek apakah ada user yang sedang login
     */
    public static boolean isLoggedIn() {
        return currentUser != null;
    }

    /**
     * Cek apakah user yang login adalah admin
     */
    public static boolean isAdmin() {
        return currentUser != null && currentUser.isAdmin();
    }
}
