package com.hahn.erms.client.ui.screens;

import com.hahn.erms.client.model.Employee;
import com.hahn.erms.client.model.PagedResponse;
import com.hahn.erms.client.model.SearchRequest;
import com.hahn.erms.client.security.Role;
import com.hahn.erms.client.service.AuthService;
import com.hahn.erms.client.service.EmployeeService;
import com.hahn.erms.client.ui.components.EmployeeTable;
import com.hahn.erms.client.ui.components.SearchPanel;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MainScreen extends JFrame {
    private final EmployeeService employeeService;
    private final EmployeeTable employeeTable;
    private final SearchPanel searchPanel;
    private final List<String> userRoles;

    public MainScreen() {
        this.employeeService = new EmployeeService();
        this.userRoles = new AuthService().getCurrentUserRoles();
        
        // Frame setup
        setTitle("Employee Records Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        // Create main panel
        JPanel mainPanel = new JPanel(new MigLayout("fill", "[grow]", "[][]20[grow][]"));
        
        // Create toolbar
        JToolBar toolbar = createToolbar();
        mainPanel.add(toolbar, "growx, wrap");

        // Create search panel
        searchPanel = new SearchPanel(this::performSearch);
        mainPanel.add(searchPanel, "growx, wrap");

        // Create table
        employeeTable = new EmployeeTable(userRoles);
        JScrollPane scrollPane = new JScrollPane(employeeTable);
        mainPanel.add(scrollPane, "grow, wrap");

        // Create status bar
        JPanel statusBar = createStatusBar();
        mainPanel.add(statusBar, "growx");

        add(mainPanel);

        // Load initial data
        refreshData();
    }

    private JToolBar createToolbar() {
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);

        JButton addButton = new JButton("Add Employee");
        JButton editButton = new JButton("Edit");
        JButton deleteButton = new JButton("Delete");
        JButton refreshButton = new JButton("Refresh");
        JButton logoutButton = new JButton("Logout");


        boolean canModify = userRoles.contains(Role.ROLE_ADMIN.name()) || userRoles.contains(Role.ROLE_HR.name());

        addButton.setVisible(canModify);
        deleteButton.setVisible(canModify);

        addButton.addActionListener(e -> showEmployeeDialog(null));
        editButton.addActionListener(e -> editSelectedEmployee());
        deleteButton.addActionListener(e -> deleteSelectedEmployee());
        refreshButton.addActionListener(e -> refreshData());
        logoutButton.addActionListener(e -> logout());

        toolbar.add(addButton);
        toolbar.add(editButton);
        toolbar.add(deleteButton);
        toolbar.addSeparator();
        toolbar.add(refreshButton);
        toolbar.add(Box.createHorizontalGlue());
        toolbar.add(logoutButton);

        if (userRoles.contains(Role.ROLE_ADMIN.name())) {
            JButton auditButton = new JButton("View Audit Log");
            auditButton.addActionListener(e -> viewAuditLog());
            toolbar.add(auditButton);
        }

        return toolbar;
    }

    private JPanel createStatusBar() {
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBorder(BorderFactory.createEtchedBorder());
        JLabel statusLabel = new JLabel(" Ready");
        statusBar.add(statusLabel, BorderLayout.WEST);
        return statusBar;
    }

    private void showEmployeeDialog(Employee employee) {
        EmployeeDialog dialog = new EmployeeDialog(this, employee);
        dialog.setVisible(true);
        if (dialog.isDataUpdated()) {
            refreshData();
        }
    }

    private void editSelectedEmployee() {
        Employee selected = employeeTable.getSelectedEmployee();
        if (selected != null) {
            showEmployeeDialog(selected);
        } else {
            JOptionPane.showMessageDialog(this, 
                "Please select an employee to edit", 
                "No Selection", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void deleteSelectedEmployee() {
        Employee selected = employeeTable.getSelectedEmployee();
        if (selected != null) {
            int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this employee?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    employeeService.deleteEmployee(selected.getId());
                    refreshData();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    JOptionPane.showMessageDialog(this,
                        "Error deleting employee",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void performSearch(SearchRequest searchRequest) {
        try {
            PagedResponse<Employee> result = employeeService.searchEmployees(searchRequest);
            employeeTable.setData(result.getContent());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(this,
                "Error searching employees",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshData() {
        SearchRequest defaultSearch = new SearchRequest();
        performSearch(defaultSearch);
    }

    private void logout() {
        new AuthService().logout();
        new LoginScreen().setVisible(true);
        this.dispose();
    }

    private void viewAuditLog() {
        Employee selected = employeeTable.getSelectedEmployee();
        if (selected != null) {
            AuditLogDialog dialog = new AuditLogDialog(this, selected.getEmployeeId());
            dialog.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Please select an employee to view audit log",
                    "No Selection",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }
}