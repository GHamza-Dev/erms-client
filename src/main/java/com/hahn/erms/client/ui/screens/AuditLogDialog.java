package com.hahn.erms.client.ui.screens;

import com.hahn.erms.client.model.AuditLog;
import com.hahn.erms.client.model.Employee;
import com.hahn.erms.client.service.AuditService;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Objects;

public class AuditLogDialog extends JDialog {
    private final AuditService auditService;
    private final String employeeId;
    private final JTable auditTable;
    private final JTextArea detailsArea;

    public AuditLogDialog(JFrame parent, String employeeId) {
        super(parent, "Audit Log", true);
        this.employeeId = employeeId;
        this.auditService = new AuditService();

        setSize(1000, 600);
        setLocationRelativeTo(parent);

        // Create split pane for table and details
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

        // Create top panel with table
        JPanel topPanel = new JPanel(new MigLayout("fill, insets 10", "[grow]", "[grow]"));
        auditTable = createAuditTable();
        topPanel.add(new JScrollPane(auditTable), "grow");

        // Create bottom panel with details
        JPanel bottomPanel = new JPanel(new MigLayout("fill, insets 10", "[grow]", "[][grow]"));
        bottomPanel.add(new JLabel("Change Details:"), "wrap");
        detailsArea = new JTextArea();
        detailsArea.setEditable(false);
        detailsArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        bottomPanel.add(new JScrollPane(detailsArea), "grow");

        // Add panels to split pane
        splitPane.setTopComponent(topPanel);
        splitPane.setBottomComponent(bottomPanel);
        splitPane.setDividerLocation(300);

        // Add to dialog
        add(splitPane);

        // Load audit data
        loadAuditData();
    }

    private JTable createAuditTable() {
        String[] columns = {"Modification"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        table.getColumnModel().getColumn(0).setPreferredWidth(800);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Add selection listener to show details
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                showSelectedAuditDetails();
            }
        });

        return table;
    }

    private void loadAuditData() {
        try {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            List<AuditLog> auditLogs = auditService.getAuditLogs(employeeId);
            DefaultTableModel model = (DefaultTableModel) auditTable.getModel();

            // Clear existing rows
            model.setRowCount(0);

            // Add new rows
            for (AuditLog log : auditLogs) {
                model.addRow(new Object[]{log.getModification()});
            }

            // Store audit logs for details view
            auditTable.putClientProperty("auditLogs", auditLogs);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error loading audit logs: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        } finally {
            setCursor(Cursor.getDefaultCursor());
        }
    }

    private void showSelectedAuditDetails() {
        int selectedRow = auditTable.getSelectedRow();
        if (selectedRow >= 0) {
            @SuppressWarnings("unchecked")
            List<AuditLog> auditLogs = (List<AuditLog>) auditTable.getClientProperty("auditLogs");
            AuditLog selectedLog = auditLogs.get(selectedRow);

            StringBuilder details = new StringBuilder();
            details.append("Change Description:\n");
            details.append(selectedLog.getModification()).append("\n\n");

            details.append("Changes:\n");
            if (selectedLog.getOldValue() != null && selectedLog.getNewValue() != null) {
                compareEmployees(selectedLog.getOldValue(), selectedLog.getNewValue(), details);
            }

            detailsArea.setText(details.toString());
            detailsArea.setCaretPosition(0);
        }
    }

    private void compareEmployees(Employee oldValue, Employee newValue, StringBuilder details) {
        compareField("First Name", oldValue.getFirstName(), newValue.getFirstName(), details);
        compareField("Last Name", oldValue.getLastName(), newValue.getLastName(), details);
        compareField("Email", oldValue.getEmail(), newValue.getEmail(), details);
        compareField("Phone", oldValue.getPhone(), newValue.getPhone(), details);
        compareField("Status", oldValue.getEmploymentStatus(), newValue.getEmploymentStatus(), details);

        if (oldValue.getDepartment() != null && newValue.getDepartment() != null) {
            compareField("Department", oldValue.getDepartment().getName(),
                    newValue.getDepartment().getName(), details);
        }

        if (oldValue.getJobTitle() != null && newValue.getJobTitle() != null) {
            compareField("Job Title", oldValue.getJobTitle().getTitle(),
                    newValue.getJobTitle().getTitle(), details);
        }
    }

    private void compareField(String fieldName, String oldValue, String newValue, StringBuilder details) {
        // Using Objects.equals instead of StringUtils
        if (!Objects.equals(oldValue, newValue)) {
            details.append(String.format("%-15s: %s → %s\n",
                    fieldName,
                    oldValue != null ? oldValue : "null",
                    newValue != null ? newValue : "null"));
        }
    }
}