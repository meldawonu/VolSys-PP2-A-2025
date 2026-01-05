package view.panels;

import controller.AuthController;
import controller.EventController;
import controller.RegistrationController;
import model.Event;
import model.Registration;
import model.User;
import utils.StyleUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Panel untuk user melihat dan mendaftar ke events
 */
public class UserEventPanel extends JPanel {
    private JTable eventTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JTextArea descriptionArea;
    private EventController eventController;
    private RegistrationController registrationController;
    private User currentUser;

    public UserEventPanel() {
        eventController = new EventController();
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

        JLabel titleLabel = StyleUtils.createTitleLabel("Daftar Event Volunteer");

        JLabel subtitleLabel = new JLabel("Pilih event dan klik 'Daftar' untuk menjadi volunteer");
        subtitleLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        subtitleLabel.setForeground(Color.GRAY);

        JPanel titleTextPanel = new JPanel();
        titleTextPanel.setLayout(new BoxLayout(titleTextPanel, BoxLayout.Y_AXIS));
        titleTextPanel.setBackground(Color.WHITE);
        titleTextPanel.add(titleLabel);
        titleTextPanel.add(subtitleLabel);

        titlePanel.add(titleTextPanel, BorderLayout.WEST);
        add(titlePanel, BorderLayout.NORTH);

        // Main content panel
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(650);
        splitPane.setResizeWeight(0.7);

        // Left panel - Table
        JPanel leftPanel = new JPanel(new BorderLayout(10, 10));
        leftPanel.setBackground(Color.WHITE);

        // Toolbar
        JPanel toolbarPanel = new JPanel(new BorderLayout(10, 0));
        toolbarPanel.setBackground(Color.WHITE);
        toolbarPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Search
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

        JButton registerButton = StyleUtils.createStyledButton("Daftar", StyleUtils.SUCCESS_COLOR, 120, 35);
        JButton refreshButton = StyleUtils.createStyledButton("Refresh", StyleUtils.SECONDARY_COLOR);

        registerButton.addActionListener(e -> registerToEvent());
        refreshButton.addActionListener(e -> loadData());

        actionPanel.add(registerButton);
        actionPanel.add(refreshButton);

        toolbarPanel.add(searchPanel, BorderLayout.WEST);
        toolbarPanel.add(actionPanel, BorderLayout.EAST);

        // Table
        String[] columns = { "ID", "Judul Event", "Lokasi", "Tanggal", "Status" };
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

        eventTable.getTableHeader().setBackground(StyleUtils.SUCCESS_COLOR);

        // Column widths
        eventTable.getColumnModel().getColumn(0).setPreferredWidth(40);
        eventTable.getColumnModel().getColumn(1).setPreferredWidth(200);
        eventTable.getColumnModel().getColumn(2).setPreferredWidth(150);
        eventTable.getColumnModel().getColumn(3).setPreferredWidth(130);
        eventTable.getColumnModel().getColumn(4).setPreferredWidth(80);

        // Selection listener to show description
        eventTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                showEventDescription();
            }
        });

        JScrollPane scrollPane = new JScrollPane(eventTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        scrollPane.getViewport().setBackground(Color.WHITE);

        leftPanel.add(toolbarPanel, BorderLayout.NORTH);
        leftPanel.add(scrollPane, BorderLayout.CENTER);

        // Right panel - Description
        JPanel rightPanel = new JPanel(new BorderLayout(10, 10));
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel descLabel = new JLabel("Deskripsi Event");
        descLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        descLabel.setForeground(StyleUtils.DARK_COLOR);

        descriptionArea = new JTextArea();
        descriptionArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setEditable(false);
        descriptionArea.setBackground(new Color(248, 249, 250));
        descriptionArea.setForeground(Color.BLACK);
        descriptionArea.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        descriptionArea.setText("Pilih event untuk melihat deskripsi...");

        JScrollPane descScrollPane = new JScrollPane(descriptionArea);
        descScrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        rightPanel.add(descLabel, BorderLayout.NORTH);
        rightPanel.add(descScrollPane, BorderLayout.CENTER);

        splitPane.setLeftComponent(leftPanel);
        splitPane.setRightComponent(rightPanel);

        add(splitPane, BorderLayout.CENTER);
    }

    public void loadData() {
        tableModel.setRowCount(0);
        List<Event> events = eventController.getAvailableEvents();

        for (Event event : events) {
            // Check if already registered
            boolean isRegistered = registrationController.isAlreadyRegistered(currentUser.getId(), event.getId());
            String status = isRegistered ? "TERDAFTAR" : event.getStatus().toUpperCase();

            Object[] row = {
                    event.getId(),
                    event.getTitle(),
                    event.getLocation() != null ? event.getLocation() : "-",
                    event.getEventDate() != null ? event.getEventDate().toString().substring(0, 16) : "-",
                    status
            };
            tableModel.addRow(row);
        }
    }

    private void searchData() {
        String keyword = searchField.getText().trim().toLowerCase();
        tableModel.setRowCount(0);
        List<Event> events = eventController.getAvailableEvents();

        for (Event event : events) {
            if (event.getTitle().toLowerCase().contains(keyword) ||
                    (event.getLocation() != null && event.getLocation().toLowerCase().contains(keyword))) {
                boolean isRegistered = registrationController.isAlreadyRegistered(currentUser.getId(), event.getId());
                String status = isRegistered ? "TERDAFTAR" : event.getStatus().toUpperCase();

                Object[] row = {
                        event.getId(),
                        event.getTitle(),
                        event.getLocation() != null ? event.getLocation() : "-",
                        event.getEventDate() != null ? event.getEventDate().toString().substring(0, 16) : "-",
                        status
                };
                tableModel.addRow(row);
            }
        }
    }

    private void showEventDescription() {
        int selectedRow = eventTable.getSelectedRow();
        if (selectedRow >= 0) {
            int eventId = (int) tableModel.getValueAt(selectedRow, 0);
            Event event = eventController.getEventById(eventId);

            if (event != null) {
                StringBuilder sb = new StringBuilder();
                sb.append("Sematkan").append(event.getTitle()).append("\n\n");
                sb.append("Lokasi: ").append(event.getLocation() != null ? event.getLocation() : "-").append("\n\n");
                sb.append("Tanggal: ")
                        .append(event.getEventDate() != null ? event.getEventDate().toString().substring(0, 16) : "-")
                        .append("\n\n");
                sb.append("Deskripsi:\n")
                        .append(event.getDescription() != null ? event.getDescription() : "Tidak ada deskripsi");

                descriptionArea.setText(sb.toString());
                descriptionArea.setCaretPosition(0);
            }
        }
    }

    private void registerToEvent() {
        int selectedRow = eventTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this,
                    "Pilih event yang ingin Anda ikuti!",
                    "Peringatan",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int eventId = (int) tableModel.getValueAt(selectedRow, 0);
        String eventTitle = (String) tableModel.getValueAt(selectedRow, 1);
        String status = (String) tableModel.getValueAt(selectedRow, 4);

        if ("TERDAFTAR".equals(status)) {
            JOptionPane.showMessageDialog(this,
                    "Anda sudah terdaftar di event ini!",
                    "Info",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Apakah Anda yakin ingin mendaftar sebagai volunteer\ndi event '" + eventTitle + "'?",
                "Konfirmasi Pendaftaran",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            Registration registration = new Registration(currentUser.getId(), eventId, "pending");

            if (registrationController.createRegistration(registration)) {
                JOptionPane.showMessageDialog(this,
                        "Berhasil mendaftar ke event!\nStatus: PENDING (menunggu persetujuan admin)",
                        "Sukses",
                        JOptionPane.INFORMATION_MESSAGE);
                loadData();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Gagal mendaftar ke event!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
