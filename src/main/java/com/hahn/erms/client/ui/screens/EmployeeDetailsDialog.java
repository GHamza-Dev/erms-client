package com.hahn.erms.client.ui.screens;

import com.hahn.erms.client.model.Employee;
import net.miginfocom.swing.MigLayout;
import javax.swing.*;
import java.awt.*;

public class EmployeeDetailsDialog extends JDialog {
    private final Employee employee;

    public EmployeeDetailsDialog(JFrame parent, Employee employee) {
        super(parent, "Employee Details", true);
        this.employee = employee;
        
        setSize(600, 400);
        setLocationRelativeTo(parent);
        
        initializeUI();
    }

    private void initializeUI() {
        JPanel mainPanel = new JPanel(new MigLayout("fillx, insets 20", "[][grow]", "[]10[]10[]10[]10[]10[]10[]"));
        
        // Basic Information Section
        mainPanel.add(createSectionLabel("Basic Information"), "span 2, gapbottom 10, wrap");
        
        addField(mainPanel, "Employee ID:", employee.getEmployeeId());
        addField(mainPanel, "Full Name:", employee.getFirstName() + " " + employee.getLastName());
        addField(mainPanel, "Email:", employee.getEmail());
        addField(mainPanel, "Phone:", employee.getPhone());
        
        // Employment Information
        mainPanel.add(createSectionLabel("Employment Information"), "span 2, gapbottom 10, wrap");
        
        addField(mainPanel, "Department:", employee.getDepartment().getName());
        addField(mainPanel, "Job Title:", employee.getJobTitle().getTitle());
        addField(mainPanel, "Status:", employee.getEmploymentStatus());
        addField(mainPanel, "Hire Date:", employee.getContract().getHireDate().toString());
        
        // Contact Information
        mainPanel.add(createSectionLabel("Contact Information"), "span 2, gapbottom 10, wrap");
        addField(mainPanel, "Address:", employee.getAddress());

        // Add Close button
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        mainPanel.add(closeButton, "span 2, center, gaptop 20");

        add(new JScrollPane(mainPanel));
    }

    private JLabel createSectionLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(label.getFont().deriveFont(Font.BOLD, 14));
        return label;
    }

    private void addField(JPanel panel, String label, String value) {
        panel.add(new JLabel(label), "right");
        JTextField field = new JTextField(value != null ? value : "");
        field.setEditable(false);
        field.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
        field.setBackground(Color.WHITE);
        panel.add(field, "growx, wrap");
    }
}