package view.panels;

import controller.AuthController;
import controller.RegistrationController;
import model.Registration;
import model.User;
import utils.PDFExporter;
import utils.StyleUtils;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.util.List;

/**
 * Panel untuk user melihat events yang diikuti
 */
public class MyRegistrationsPanel extends JPanel {
    private JTable registrationTable;
    private DefaultTableModel tableModel;
    private RegistrationController registrationController;
    private User currentUser;

    private JLabel totalLabel;
    private JLabel pendingLabel;
    private JLabel approvedLabel;

    public MyRegistrationsPanel() {
        registrationController = new RegistrationController();
        currentUser = AuthController.getCurrentUser();
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

        JLabel titleLabel = StyleUtils.createTitleLabel("Event Saya");

        JLabel subtitleLabel = new JLabel("Daftar event volunteer yang Anda ikuti");
        subtitleLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        subtitleLabel.setForeground(Color.GRAY);

        JPanel titleTextPanel = new JPanel();
        titleTextPanel.setLayout(new BoxLayout(titleTextPanel, BoxLayout.Y_AXIS));
        titleTextPanel.setBackground(Color.WHITE);
        titleTextPanel.add(titleLabel);
        titleTextPanel.add(subtitleLabel);

        titlePanel.add(titleTextPanel, BorderLayout.WEST);
        add(titlePanel, BorderLayout.NORTH);

        // Toolbar panel
        JPanel toolbarPanel = new JPanel(new BorderLayout(10, 0));
        toolbarPanel.setBackground(Color.WHITE);
        toolbarPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Info panel
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        infoPanel.setBackground(Color.WHITE);

        JLabel infoLabel = new JLabel("👤 " + currentUser.getFullName());
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        infoLabel.setForeground(StyleUtils.DARK_COLOR);
        infoPanel.add(infoLabel);

        // Action buttons
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actionPanel.setBackground(Color.WHITE);

        JButton cancelButton = StyleUtils.createStyledButton("❌ Batalkan", StyleUtils.DANGER_COLOR);
        JButton exportButton = StyleUtils.createStyledButton("📄 Export PDF", StyleUtils.PURPLE_COLOR, 110, 35);
        JButton refreshButton = StyleUtils.createStyledButton("🔄 Refresh", StyleUtils.SECONDARY_COLOR);

        cancelButton.addActionListener(e -> cancelRegistration());
        exportButton.addActionListener(e -> exportToPDF());
        refreshButton.addActionListener(e -> loadData());

        actionPanel.add(cancelButton);
        actionPanel.add(exportButton);
        actionPanel.add(refreshButton);

        toolbarPanel.add(infoPanel, BorderLayout.WEST);
        toolbarPanel.add(actionPanel, BorderLayout.EAST);

        // Table
        String[] columns = { "ID", "Event", "Lokasi", "Tanggal Event", "Status Pendaftaran", "Tanggal Daftar" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        registrationTable = new JTable(tableModel);
        StyleUtils.styleTable(registrationTable);
        registrationTable.getTableHeader().setBackground(StyleUtils.PURPLE_COLOR);

        // Column widths
        registrationTable.getColumnModel().getColumn(0).setPreferredWidth(40);
        registrationTable.getColumnModel().getColumn(1).setPreferredWidth(200);
        registrationTable.getColumnModel().getColumn(2).setPreferredWidth(150);
        registrationTable.getColumnModel().getColumn(3).setPreferredWidth(130);
        registrationTable.getColumnModel().getColumn(4).setPreferredWidth(120);
        registrationTable.getColumnModel().getColumn(5).setPreferredWidth(130);

        JScrollPane scrollPane = new JScrollPane(registrationTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        scrollPane.getViewport().setBackground(Color.WHITE);

        // Statistics panel
        JPanel statsPanel = createStatsPanel();

        // Center panel
        JPanel centerPanel = new JPanel(new BorderLayout(0, 10));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.add(toolbarPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        centerPanel.add(statsPanel, BorderLayout.SOUTH);

        add(centerPanel, BorderLayout.CENTER);
    }

    private JPanel createStatsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        panel.setBackground(new Color(248, 249, 250));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)));

        totalLabel = new JLabel("📊 Total Partisipasi: 0");
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        totalLabel.setForeground(StyleUtils.DARK_COLOR);

        pendingLabel = new JLabel("⏳ Pending: 0");
        pendingLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        pendingLabel.setForeground(new Color(180, 140, 0));

        approvedLabel = new JLabel("✅ Approved: 0");
        approvedLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        approvedLabel.setForeground(new Color(30, 130, 76));

        panel.add(totalLabel);
        panel.add(pendingLabel);
        panel.add(approvedLabel);

        return panel;
    }

    public void loadData() {
        tableModel.setRowCount(0);
        List<Registration> registrations = registrationController.getRegistrationsByUser(currentUser.getId());

        int pending = 0, approved = 0;

        for (Registration reg : registrations) {
            Object[] row = {
                    reg.getId(),
                    reg.getEventTitle() != null ? reg.getEventTitle() : "-",
                    reg.getEventLocation() != null ? reg.getEventLocation() : "-",
                    reg.getEventDate() != null ? reg.getEventDate().toString().substring(0, 16) : "-",
                    reg.getStatus() != null ? reg.getStatus().toUpperCase() : "-",
                    reg.getRegisteredAt() != null ? reg.getRegisteredAt().toString().substring(0, 16) : "-"
            };
            tableModel.addRow(row);

            if ("pending".equalsIgnoreCase(reg.getStatus()))
                pending++;
            else if ("approved".equalsIgnoreCase(reg.getStatus()))
                approved++;
        }

        // Update statistics
        totalLabel.setText("📊 Total Partisipasi: " + registrations.size());
        pendingLabel.setText("⏳ Pending: " + pending);
        approvedLabel.setText("✅ Approved: " + approved);
    }

    private void cancelRegistration() {
        int selectedRow = registrationTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this,
                    "Pilih pendaftaran yang ingin dibatalkan!",
                    "Peringatan",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int regId = (int) tableModel.getValueAt(selectedRow, 0);
        String eventTitle = (String) tableModel.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Apakah Anda yakin ingin membatalkan pendaftaran\ndi event '" + eventTitle + "'?",
                "Konfirmasi Pembatalan",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            if (registrationController.deleteRegistration(regId)) {
                JOptionPane.showMessageDialog(this,
                        "Pendaftaran berhasil dibatalkan!",
                        "Sukses",
                        JOptionPane.INFORMATION_MESSAGE);
                loadData();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Gagal membatalkan pendaftaran!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void exportToPDF() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Simpan PDF");
        fileChooser.setSelectedFile(new File("Event_Saya_" + currentUser.getFullName().replace(" ", "_") + ".pdf"));
        fileChooser.setFileFilter(new FileNameExtensionFilter("PDF Files", "pdf"));

        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
            if (!filePath.toLowerCase().endsWith(".pdf")) {
                filePath += ".pdf";
            }

            List<Registration> registrations = registrationController.getRegistrationsByUser(currentUser.getId());

            if (PDFExporter.exportUserRegistrations(registrations, currentUser.getFullName(), filePath)) {
                JOptionPane.showMessageDialog(this,
                        "PDF berhasil disimpan!\n" + filePath,
                        "Sukses",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Gagal menyimpan PDF!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
