package view.panels;

import controller.EventController;
import model.Event;
import utils.PDFExporter;
import utils.StyleUtils;
import view.dialogs.EventFormDialog;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.util.List;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Panel untuk mengelola events (Admin only)
 */
public class EventManagementPanel extends JPanel {
    private JTable eventTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private EventController eventController;

    public EventManagementPanel() {
        eventController = new EventController();
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

        JLabel titleLabel = StyleUtils.createTitleLabel("Kelola Events");
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
        JButton exportButton = StyleUtils.createStyledButton("Export PDF", StyleUtils.PURPLE_COLOR, 110, 35);
        JButton refreshButton = StyleUtils.createStyledButton("Refresh", StyleUtils.SECONDARY_COLOR);

        addButton.addActionListener(e -> showAddDialog());
        editButton.addActionListener(e -> showEditDialog());
        deleteButton.addActionListener(e -> deleteSelected());
        exportButton.addActionListener(e -> exportToPDF());
        refreshButton.addActionListener(e -> loadData());

        actionPanel.add(addButton);
        actionPanel.add(editButton);
        actionPanel.add(deleteButton);
        actionPanel.add(exportButton);
        actionPanel.add(refreshButton);

        toolbarPanel.add(searchPanel, BorderLayout.WEST);
        toolbarPanel.add(actionPanel, BorderLayout.EAST);

        // Table
        String[] columns = { "ID", "Judul Event", "Lokasi", "Tanggal Event", "Status", "Dibuat Oleh" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        eventTable = new JTable(tableModel);
        StyleUtils.styleTable(eventTable);


        eventTable.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBackground(Color.lightGray);
                setForeground(Color.BLACK);
                setHorizontalAlignment(JLabel.CENTER);
                return this;
            }
        });

        eventTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
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
        eventTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        eventTable.getColumnModel().getColumn(1).setPreferredWidth(200);
        eventTable.getColumnModel().getColumn(2).setPreferredWidth(150);
        eventTable.getColumnModel().getColumn(3).setPreferredWidth(150);
        eventTable.getColumnModel().getColumn(4).setPreferredWidth(80);
        eventTable.getColumnModel().getColumn(5).setPreferredWidth(120);

        JScrollPane scrollPane = new JScrollPane(eventTable);
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
        List<Event> events = eventController.getAllEvents();

        for (Event event : events) {
            Object[] row = {
                    event.getId(),
                    event.getTitle(),
                    event.getLocation() != null ? event.getLocation() : "-",
                    event.getEventDate() != null ? event.getEventDate().toString().substring(0, 16) : "-",
                    event.getStatus() != null ? event.getStatus().toUpperCase() : "-",
                    event.getCreatorName() != null ? event.getCreatorName() : "-"
            };
            tableModel.addRow(row);
        }
    }

    private void searchData() {
        String keyword = searchField.getText().trim().toLowerCase();
        tableModel.setRowCount(0);
        List<Event> events = eventController.getAllEvents();

        for (Event event : events) {
            if (event.getTitle().toLowerCase().contains(keyword) ||
                    (event.getLocation() != null && event.getLocation().toLowerCase().contains(keyword))) {
                Object[] row = {
                        event.getId(),
                        event.getTitle(),
                        event.getLocation() != null ? event.getLocation() : "-",
                        event.getEventDate() != null ? event.getEventDate().toString().substring(0, 16) : "-",
                        event.getStatus() != null ? event.getStatus().toUpperCase() : "-",
                        event.getCreatorName() != null ? event.getCreatorName() : "-"
                };
                tableModel.addRow(row);
            }
        }
    }

    private void showAddDialog() {
        EventFormDialog dialog = new EventFormDialog(
                (JFrame) SwingUtilities.getWindowAncestor(this),
                "Tambah Event Baru",
                null);
        dialog.setVisible(true);

        if (dialog.isSaved()) {
            loadData();
        }
    }

    private void showEditDialog() {
        int selectedRow = eventTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this,
                    "Pilih event yang ingin diedit!",
                    "Peringatan",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int eventId = (int) tableModel.getValueAt(selectedRow, 0);
        Event event = eventController.getEventById(eventId);

        if (event != null) {
            EventFormDialog dialog = new EventFormDialog(
                    (JFrame) SwingUtilities.getWindowAncestor(this),
                    "Edit Event",
                    event);
            dialog.setVisible(true);

            if (dialog.isSaved()) {
                loadData();
            }
        }
    }

    private void deleteSelected() {
        int selectedRow = eventTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this,
                    "Pilih event yang ingin dihapus!",
                    "Peringatan",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int eventId = (int) tableModel.getValueAt(selectedRow, 0);
        String eventTitle = (String) tableModel.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Apakah Anda yakin ingin menghapus event '" + eventTitle + "'?",
                "Konfirmasi Hapus",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            if (eventController.deleteEvent(eventId)) {
                JOptionPane.showMessageDialog(this,
                        "Event berhasil dihapus!",
                        "Sukses",
                        JOptionPane.INFORMATION_MESSAGE);
                loadData();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Gagal menghapus event!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void exportToPDF() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Simpan PDF");
        fileChooser.setSelectedFile(new File("Daftar_Events.pdf"));
        fileChooser.setFileFilter(new FileNameExtensionFilter("PDF Files", "pdf"));

        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
            if (!filePath.toLowerCase().endsWith(".pdf")) {
                filePath += ".pdf";
            }

            List<Event> events = eventController.getAllEvents();

            if (PDFExporter.exportEventsList(events, filePath)) {
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
