package view.panels;

import controller.UserController;
import model.User;
import utils.StyleUtils;
import view.dialogs.UserFormDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Panel untuk mengelola users (Admin only)
 */
public class UserManagementPanel extends JPanel {
    private JTable userTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private UserController userController;

    public UserManagementPanel() {
        userController = new UserController();
        initComponents();
        loadData();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Color.WHITE);

        JLabel titleLabel = StyleUtils.createTitleLabel("Kelola Users");
        titlePanel.add(titleLabel, BorderLayout.WEST);

        add(titlePanel, BorderLayout.NORTH);

        // Toolbar panel
        JPanel toolbarPanel = new JPanel(new BorderLayout(10, 0));
        toolbarPanel.setBackground(Color.WHITE);
        toolbarPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Search field
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        searchPanel.setBackground(Color.WHITE);

        searchField = StyleUtils.createTextField(20);
        searchField.setPreferredSize(new Dimension(200, 35));

        JButton searchButton = StyleUtils.createStyledButton("Cari", StyleUtils.PRIMARY_COLOR);
        searchButton.addActionListener(e -> searchData());

        searchPanel.add(searchField);
        searchPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        searchPanel.add(searchButton);

        // Action buttons
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actionPanel.setBackground(Color.WHITE);

        JButton addButton = StyleUtils.createStyledButton("Tambah", StyleUtils.SUCCESS_COLOR);
        JButton editButton = StyleUtils.createStyledButton("Edit", StyleUtils.WARNING_COLOR);
        JButton deleteButton = StyleUtils.createStyledButton("Hapus", StyleUtils.DANGER_COLOR);
        JButton refreshButton = StyleUtils.createStyledButton("Refresh", StyleUtils.SECONDARY_COLOR);

        addButton.addActionListener(e -> showAddDialog());
        editButton.addActionListener(e -> showEditDialog());
        deleteButton.addActionListener(e -> deleteSelected());
        refreshButton.addActionListener(e -> loadData());

        actionPanel.add(addButton);
        actionPanel.add(editButton);
        actionPanel.add(deleteButton);
        actionPanel.add(refreshButton);

        toolbarPanel.add(searchPanel, BorderLayout.WEST);
        toolbarPanel.add(actionPanel, BorderLayout.EAST);

        // Table
        String[] columns = { "ID", "Nama Lengkap", "Email", "Role", "Tanggal Daftar" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        userTable = new JTable(tableModel);
        StyleUtils.styleTable(userTable);

        userTable.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBackground(Color.lightGray);
                setForeground(Color.BLACK);
                setHorizontalAlignment(JLabel.CENTER);
                return this;
            }
        });

        userTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : Color.LIGHT_GRAY);
                } else {
                    c.setBackground(Color.BLUE);
                }
                return c;
            }
        });

        // Column widths
        userTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        userTable.getColumnModel().getColumn(1).setPreferredWidth(200);
        userTable.getColumnModel().getColumn(2).setPreferredWidth(200);
        userTable.getColumnModel().getColumn(3).setPreferredWidth(80);
        userTable.getColumnModel().getColumn(4).setPreferredWidth(150);

        JScrollPane scrollPane = new JScrollPane(userTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        scrollPane.getViewport().setBackground(Color.WHITE);

        // Center panel
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);
        centerPanel.add(toolbarPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);
    }

    public void loadData() {
        tableModel.setRowCount(0);
        List<User> users = userController.getAllUsers();

        for (User user : users) {
            Object[] row = {
                    user.getId(),
                    user.getFullName(),
                    user.getEmail(),
                    user.getRole().toUpperCase(),
                    user.getCreatedAt() != null ? user.getCreatedAt().toString().substring(0, 16) : "-"
            };
            tableModel.addRow(row);
        }
    }

    private void searchData() {
        String keyword = searchField.getText().trim().toLowerCase();
        tableModel.setRowCount(0);
        List<User> users = userController.getAllUsers();

        for (User user : users) {
            if (user.getFullName().toLowerCase().contains(keyword) ||
                    user.getEmail().toLowerCase().contains(keyword)) {
                Object[] row = {
                        user.getId(),
                        user.getFullName(),
                        user.getEmail(),
                        user.getRole().toUpperCase(),
                        user.getCreatedAt() != null ? user.getCreatedAt().toString().substring(0, 16) : "-"
                };
                tableModel.addRow(row);
            }
        }
    }

    private void showAddDialog() {
        UserFormDialog dialog = new UserFormDialog(
                (JFrame) SwingUtilities.getWindowAncestor(this),
                "Tambah User Baru",
                null);
        dialog.setVisible(true);

        if (dialog.isSaved()) {
            loadData();
        }
    }

    private void showEditDialog() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this,
                    "Pilih user yang ingin diedit!",
                    "Peringatan",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int userId = (int) tableModel.getValueAt(selectedRow, 0);
        User user = userController.getUserById(userId);

        if (user != null) {
            UserFormDialog dialog = new UserFormDialog(
                    (JFrame) SwingUtilities.getWindowAncestor(this),
                    "Edit User",
                    user);
            dialog.setVisible(true);

            if (dialog.isSaved()) {
                loadData();
            }
        }
    }

    private void deleteSelected() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this,
                    "Pilih user yang ingin dihapus!",
                    "Peringatan",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int userId = (int) tableModel.getValueAt(selectedRow, 0);
        String userName = (String) tableModel.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Apakah Anda yakin ingin menghapus user '" + userName + "'?",
                "Konfirmasi Hapus",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            if (userController.deleteUser(userId)) {
                JOptionPane.showMessageDialog(this,
                        "User berhasil dihapus!",
                        "Sukses",
                        JOptionPane.INFORMATION_MESSAGE);
                loadData();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Gagal menghapus user!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
