package view.panels;

import controller.RegistrationController;
import model.Registration;
import utils.StyleUtils;
import view.dialogs.RegistrationFormDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Panel untuk mengelola registrations (Admin only)
 */
public class RegistrationManagementPanel extends JPanel {
    private JTable registrationTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private RegistrationController registrationController;

    public RegistrationManagementPanel() {
        registrationController = new RegistrationController();
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

        JLabel titleLabel = StyleUtils.createTitleLabel("Kelola Registrations");
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
        String[] columns = { "ID", "Nama Volunteer", "Email", "Event", "Tanggal Event", "Status", "Tanggal Daftar" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        registrationTable = new JTable(tableModel);
        StyleUtils.styleTable(registrationTable);

        registrationTable.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBackground(Color.lightGray);
                setForeground(Color.BLACK);
                setHorizontalAlignment(JLabel.CENTER);
                return this;
            }
        });

        registrationTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : Color.LIGHT_GRAY);
                } else {
                    c.setBackground(Color.blue);
                }
                return c;
            }
        });

        // Column widths
        registrationTable.getColumnModel().getColumn(0).setPreferredWidth(40);
        registrationTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        registrationTable.getColumnModel().getColumn(2).setPreferredWidth(150);
        registrationTable.getColumnModel().getColumn(3).setPreferredWidth(150);
        registrationTable.getColumnModel().getColumn(4).setPreferredWidth(130);
        registrationTable.getColumnModel().getColumn(5).setPreferredWidth(80);
        registrationTable.getColumnModel().getColumn(6).setPreferredWidth(130);

        JScrollPane scrollPane = new JScrollPane(registrationTable);
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
        List<Registration> registrations = registrationController.getAllRegistrations();

        for (Registration reg : registrations) {
            Object[] row = {
                    reg.getId(),
                    reg.getUserName() != null ? reg.getUserName() : "-",
                    reg.getUserEmail() != null ? reg.getUserEmail() : "-",
                    reg.getEventTitle() != null ? reg.getEventTitle() : "-",
                    reg.getEventDate() != null ? reg.getEventDate().toString().substring(0, 16) : "-",
                    reg.getStatus() != null ? reg.getStatus().toUpperCase() : "-",
                    reg.getRegisteredAt() != null ? reg.getRegisteredAt().toString().substring(0, 16) : "-"
            };
            tableModel.addRow(row);
        }
    }

    private void searchData() {
        String keyword = searchField.getText().trim().toLowerCase();
        tableModel.setRowCount(0);
        List<Registration> registrations = registrationController.getAllRegistrations();

        for (Registration reg : registrations) {
            if ((reg.getUserName() != null && reg.getUserName().toLowerCase().contains(keyword)) ||
                    (reg.getEventTitle() != null && reg.getEventTitle().toLowerCase().contains(keyword))) {
                Object[] row = {
                        reg.getId(),
                        reg.getUserName() != null ? reg.getUserName() : "-",
                        reg.getUserEmail() != null ? reg.getUserEmail() : "-",
                        reg.getEventTitle() != null ? reg.getEventTitle() : "-",
                        reg.getEventDate() != null ? reg.getEventDate().toString().substring(0, 16) : "-",
                        reg.getStatus() != null ? reg.getStatus().toUpperCase() : "-",
                        reg.getRegisteredAt() != null ? reg.getRegisteredAt().toString().substring(0, 16) : "-"
                };
                tableModel.addRow(row);
            }
        }
    }

    private void showAddDialog() {
        RegistrationFormDialog dialog = new RegistrationFormDialog(
                (JFrame) SwingUtilities.getWindowAncestor(this),
                "Tambah Registration Baru",
                null);
        dialog.setVisible(true);

        if (dialog.isSaved()) {
            loadData();
        }
    }

    private void showEditDialog() {
        int selectedRow = registrationTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this,
                    "Pilih registration yang ingin diedit!",
                    "Peringatan",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int regId = (int) tableModel.getValueAt(selectedRow, 0);
        Registration registration = registrationController.getRegistrationById(regId);

        if (registration != null) {
            RegistrationFormDialog dialog = new RegistrationFormDialog(
                    (JFrame) SwingUtilities.getWindowAncestor(this),
                    "Edit Registration",
                    registration);
            dialog.setVisible(true);

            if (dialog.isSaved()) {
                loadData();
            }
        }
    }

    private void deleteSelected() {
        int selectedRow = registrationTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this,
                    "Pilih registration yang ingin dihapus!",
                    "Peringatan",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int regId = (int) tableModel.getValueAt(selectedRow, 0);
        String userName = (String) tableModel.getValueAt(selectedRow, 1);
        String eventTitle = (String) tableModel.getValueAt(selectedRow, 3);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Apakah Anda yakin ingin menghapus registration\n'" + userName + "' dari event '" + eventTitle + "'?",
                "Konfirmasi Hapus",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            if (registrationController.deleteRegistration(regId)) {
                JOptionPane.showMessageDialog(this,
                        "Registration berhasil dihapus!",
                        "Sukses",
                        JOptionPane.INFORMATION_MESSAGE);
                loadData();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Gagal menghapus registration!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
