package com.hahn.erms.client.ui.screens;

import com.hahn.erms.client.model.*;
import com.hahn.erms.client.service.AuthService;
import com.hahn.erms.client.service.EmployeeService;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.time.LocalDate;
import java.util.List;

public class EmployeeDialog extends JDialog {
    private final JTextField firstNameField;
    private final JTextField lastNameField;
    private final JTextField emailField;
    private final JTextField phoneField;
    private final JComboBox<Department> departmentCombo;
    private final JComboBox<JobTitle> jobTitleCombo;
    private final JComboBox<String> statusCombo;
    private final Employee employee;
    private final EmployeeService employeeService;
    private boolean dataUpdated = false;
    private final List<String> userRoles;

    public EmployeeDialog(JFrame parent, Employee employee) {
        super(parent, employee == null ? "Create Employee" : "Edit Employee", true);
        this.employee = employee;
        this.employeeService = new EmployeeService();
        this.userRoles = new AuthService().getCurrentUserRoles();

        setSize(500, 400);
        setLocationRelativeTo(parent);
        setLayout(new MigLayout("fillx, insets 20", "[][grow]", "[]10[]10[]10[]10[]10[]10[]"));

        // Initialize components
        firstNameField = new JTextField(20);
        lastNameField = new JTextField(20);
        emailField = new JTextField(20);
        phoneField = new JTextField(20);
        departmentCombo = new JComboBox<>();
        jobTitleCombo = new JComboBox<>();
        statusCombo = new JComboBox<>(new String[]{"ACTIVE", "INACTIVE", "ON_LEAVE"});

        // Add components
        add(new JLabel("First Name:"), "right");
        add(firstNameField, "growx, wrap");

        add(new JLabel("Last Name:"), "right");
        add(lastNameField, "growx, wrap");

        add(new JLabel("Email:"), "right");
        add(emailField, "growx, wrap");

        add(new JLabel("Phone:"), "right");
        add(phoneField, "growx, wrap");

        add(new JLabel("Department:"), "right");
        add(departmentCombo, "growx, wrap");

        add(new JLabel("Job Title:"), "right");
        add(jobTitleCombo, "growx, wrap");

        add(new JLabel("Status:"), "right");
        add(statusCombo, "growx, wrap");

        // Buttons
        JPanel buttonPanel = new JPanel(new MigLayout("insets 0"));
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        add(buttonPanel, "span, center");

        // Add listeners
        saveButton.addActionListener(e -> save());
        cancelButton.addActionListener(e -> dispose());

        // Load data
        loadComboBoxData();
        if (employee != null) {
            loadEmployeeData();
        }

        // Set field access based on role
        setFieldAccess();
    }

    private void loadComboBoxData() {
        try {
            employeeService.getAllDepartments().forEach(departmentCombo::addItem);
            employeeService.getAllJobTitles().forEach(jobTitleCombo::addItem);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(this,
                "Error loading data",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadEmployeeData() {
        firstNameField.setText(employee.getFirstName());
        lastNameField.setText(employee.getLastName());
        emailField.setText(employee.getEmail());
        phoneField.setText(employee.getPhone());
        statusCombo.setSelectedItem(employee.getEmploymentStatus());
        
        // Set department and job title
        if (employee.getDepartment() != null) {
            departmentCombo.setSelectedItem(employee.getDepartment());
        }
        if (employee.getJobTitle() != null) {
            jobTitleCombo.setSelectedItem(employee.getJobTitle());
        }
    }

    private void setFieldAccess() {
        boolean isHrOrAdmin = userRoles.contains("ROLE_HR") || userRoles.contains("ROLE_ADMIN");
        boolean isManager = userRoles.contains("ROLE_MANAGER");

        // Managers can only edit email and phone
        if (!isHrOrAdmin && isManager) {
            firstNameField.setEnabled(false);
            lastNameField.setEnabled(false);
            departmentCombo.setEnabled(false);
            jobTitleCombo.setEnabled(false);
            statusCombo.setEnabled(false);
        }
    }

    private void save() {
        try {
            if (employee == null) {
                // Create new employee
                CreateEmployeeRequest request = new CreateEmployeeRequest();
                request.setFirstName(firstNameField.getText());
                request.setLastName(lastNameField.getText());
                request.setEmail(emailField.getText());
                request.setPhone(phoneField.getText());
                request.setDepartmentId(((Department) departmentCombo.getSelectedItem()).getId());
                request.setJobTitleId(((JobTitle) jobTitleCombo.getSelectedItem()).getId());
                request.setEmploymentStatus((String) statusCombo.getSelectedItem());

                employeeService.createEmployee(request);
            } else {
                // Update existing employee
                UpdateEmployeeRequest request = new UpdateEmployeeRequest();
                request.setId(employee.getId());
                request.setEmail(emailField.getText());
                request.setPhone(phoneField.getText());
                
                if (userRoles.contains("ROLE_HR") || userRoles.contains("ROLE_ADMIN")) {
                    request.setFirstName(firstNameField.getText());
                    request.setLastName(lastNameField.getText());
                    request.setEmploymentStatus((String) statusCombo.getSelectedItem());
                }

                employeeService.updateEmployee(request);
            }

            dataUpdated = true;
            dispose();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(this,
                "Error saving employee",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isDataUpdated() {
        return dataUpdated;
    }
}