package com.hahn.erms.client.ui.components;

import com.hahn.erms.client.model.Department;
import com.hahn.erms.client.model.JobTitle;
import com.hahn.erms.client.model.SearchRequest;
import com.hahn.erms.client.service.EmployeeService;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.time.LocalDate;
import java.util.function.Consumer;

public class SearchPanel extends JPanel {
    private final JTextField nameField;
    private final JTextField employeeIdField;
    private final JComboBox<Department> departmentCombo;
    private final JComboBox<JobTitle> jobTitleCombo;
    private final JComboBox<String> statusCombo;
    private final Consumer<SearchRequest> searchCallback;
    private final EmployeeService employeeService;

    public SearchPanel(Consumer<SearchRequest> searchCallback) {
        this.searchCallback = searchCallback;
        this.employeeService = new EmployeeService();

        setLayout(new MigLayout("fillx, insets 10", "[][grow][][grow]", "[][]"));
        setBorder(BorderFactory.createTitledBorder("Search"));

        // Initialize components
        nameField = new JTextField(20);
        employeeIdField = new JTextField(15);
        departmentCombo = new JComboBox<>();
        jobTitleCombo = new JComboBox<>();
        statusCombo = new JComboBox<>(new String[]{"All", "ACTIVE", "INACTIVE", "ON_LEAVE"});

        // Add components
        add(new JLabel("Name:"), "right");
        add(nameField, "growx");
        add(new JLabel("Employee ID:"), "right");
        add(employeeIdField, "growx, wrap");

        add(new JLabel("Department:"), "right");
        add(departmentCombo, "growx");
        add(new JLabel("Job Title:"), "right");
        add(jobTitleCombo, "growx, wrap");

        add(new JLabel("Status:"), "right");
        add(statusCombo, "growx");

        JButton searchButton = new JButton("Search");
        JButton resetButton = new JButton("Reset");

        JPanel buttonPanel = new JPanel(new MigLayout("insets 0"));
        buttonPanel.add(searchButton);
        buttonPanel.add(resetButton);

        add(buttonPanel, "skip 2, right");

        // Add listeners
        searchButton.addActionListener(e -> performSearch());
        resetButton.addActionListener(e -> resetFields());

        // Load departments and job titles
        loadComboBoxData();
    }

    private void loadComboBoxData() {
        try {
            // Load departments with custom renderer and model
            departmentCombo.addItem(null); // Add empty selection
            employeeService.getAllDepartments().forEach(departmentCombo::addItem);


            departmentCombo.setRenderer(new DefaultListCellRenderer() {
                @Override
                public java.awt.Component getListCellRendererComponent(
                        JList<?> list, Object value, int index,
                        boolean isSelected, boolean cellHasFocus) {

                    // If value is null, return an empty label
                    if (value == null) {
                        setText("");
                        return this;
                    }

                    // Cast to Department and set text to department name
                    Department dept = (Department) value;
                    setText(dept.getName());
                    return this;
                }
            });

            // Do the same for job titles
            jobTitleCombo.addItem(null); // Add empty selection
            employeeService.getAllJobTitles().forEach(jobTitleCombo::addItem);

            jobTitleCombo.setRenderer(new DefaultListCellRenderer() {
                @Override
                public java.awt.Component getListCellRendererComponent(
                        JList<?> list, Object value, int index,
                        boolean isSelected, boolean cellHasFocus) {

                    // If value is null, return an empty label
                    if (value == null) {
                        setText("");
                        return this;
                    }

                    // Cast to JobTitle and set text to job title name
                    JobTitle jobTitle = (JobTitle) value;
                    setText(jobTitle.getTitle());
                    return this;
                }
            });
        } catch (Exception e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(this,
                    "Error loading search data.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void performSearch() {
        SearchRequest request = new SearchRequest();
        request.setName(nameField.getText().trim());
        request.setEmployeeId(employeeIdField.getText().trim());
        
        Department selectedDept = (Department) departmentCombo.getSelectedItem();
        if (selectedDept != null) {
            request.setDepartmentId(selectedDept.getId());
        }

        JobTitle selectedJob = (JobTitle) jobTitleCombo.getSelectedItem();
        if (selectedJob != null) {
            request.setJobTitleId(selectedJob.getId());
        }

        String status = (String) statusCombo.getSelectedItem();
        if (!"All".equals(status)) {
            request.setEmploymentStatus(status);
        }

        searchCallback.accept(request);
    }

    private void resetFields() {
        nameField.setText("");
        employeeIdField.setText("");
        departmentCombo.setSelectedItem(null);
        jobTitleCombo.setSelectedItem(null);
        statusCombo.setSelectedIndex(0);
    }
}