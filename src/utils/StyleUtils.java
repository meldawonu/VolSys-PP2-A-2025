package utils;

import javax.swing.*;
import java.awt.*;

/**
 * Utility class untuk styling komponen UI yang konsisten
 */
public class StyleUtils {

    // Colors
    public static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    public static final Color SUCCESS_COLOR = new Color(46, 204, 113);
    public static final Color WARNING_COLOR = new Color(241, 196, 15);
    public static final Color DANGER_COLOR = new Color(231, 76, 60);
    public static final Color SECONDARY_COLOR = new Color(149, 165, 166);
    public static final Color PURPLE_COLOR = new Color(155, 89, 182);
    public static final Color DARK_COLOR = new Color(44, 62, 80);
    public static final Color TEXT_COLOR = new Color(50, 50, 50);

    /**
     * Membuat styled button dengan warna background dan font yang terlihat
     */
    public static JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBackground(bgColor);
        // Gunakan warna hitam untuk button dengan background terang
        // dan tetap hitam untuk semua button agar konsisten di dark mode
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(100, 35));
        return button;
    }

    /**
     * Membuat styled button dengan ukuran custom
     */
    public static JButton createStyledButton(String text, Color bgColor, int width, int height) {
        JButton button = createStyledButton(text, bgColor);
        button.setPreferredSize(new Dimension(width, height));
        return button;
    }

    /**
     * Membuat styled text field
     */
    public static JTextField createTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBackground(Color.WHITE);
        field.setForeground(Color.BLACK);
        field.setCaretColor(Color.BLACK);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        return field;
    }

    /**
     * Membuat styled text field dengan ukuran tertentu
     */
    public static JTextField createTextField(int columns) {
        JTextField field = createTextField();
        field.setColumns(columns);
        return field;
    }

    /**
     * Membuat styled password field
     */
    public static JPasswordField createPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBackground(Color.WHITE);
        field.setForeground(Color.BLACK);
        field.setCaretColor(Color.BLACK);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        return field;
    }

    /**
     * Membuat styled label
     */
    public static JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(TEXT_COLOR);
        return label;
    }

    /**
     * Membuat styled title label
     */
    public static JLabel createTitleLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 20));
        label.setForeground(DARK_COLOR);
        return label;
    }

    /**
     * Style JTable agar konsisten
     */
    public static void styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(35);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setBackground(Color.WHITE);
        table.setForeground(Color.BLACK);
        table.setSelectionBackground(new Color(52, 152, 219));
        table.setSelectionForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(52, 73, 94));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setPreferredSize(new Dimension(0, 40));
        table.setShowGrid(true);
        table.setGridColor(new Color(230, 230, 230));
    }

    /**
     * Style panel dengan background putih
     */
    public static void stylePanel(JPanel panel) {
        panel.setBackground(Color.WHITE);
    }
}
