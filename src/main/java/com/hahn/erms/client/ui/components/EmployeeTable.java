package com.hahn.erms.client.ui.components;

import com.hahn.erms.client.model.Employee;
import com.hahn.erms.client.ui.screens.EmployeeDetailsDialog;

import javax.swing.table.AbstractTableModel;
import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class EmployeeTable extends JTable {
    private final EmployeeTableModel model;
    private final List<String> userRoles;

    public EmployeeTable(List<String> userRoles) {
        this.userRoles = userRoles;
        this.model = new EmployeeTableModel();
        setModel(model);
        
        // Table settings
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setAutoCreateRowSorter(true);
        getTableHeader().setReorderingAllowed(false);
        setupDoubleClickHandler();
    }

    public void setData(List<Employee> employees) {
        model.setData(employees);
    }

    public Employee getSelectedEmployee() {
        int row = getSelectedRow();
        if (row >= 0) {
            row = convertRowIndexToModel(row);
            return model.getEmployeeAt(row);
        }
        return null;
    }

    private class EmployeeTableModel extends AbstractTableModel {
        private final String[] columnNames = {
            "ID", "Name", "Email", "Department", "Job Title", "Status"
        };
        private List<Employee> data = new ArrayList<>();

        public void setData(List<Employee> data) {
            this.data = new ArrayList<>(data);
            fireTableDataChanged();
        }

        @Override
        public int getRowCount() {
            return data.size();
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public String getColumnName(int column) {
            return columnNames[column];
        }

        @Override
        public Object getValueAt(int row, int column) {
            Employee employee = data.get(row);
            return switch (column) {
                case 0 -> employee.getEmployeeId();
                case 1 -> employee.getFirstName() + " " + employee.getLastName();
                case 2 -> employee.getEmail();
                case 3 -> employee.getDepartment().getName();
                case 4 -> employee.getJobTitle().getTitle();
                case 5 -> employee.getEmploymentStatus();
                default -> null;
            };
        }

        public Employee getEmployeeAt(int row) {
            return data.get(row);
        }
    }

    private void setupDoubleClickHandler() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    Employee selected = getSelectedEmployee();
                    if (selected != null) {
                        EmployeeDetailsDialog dialog = new EmployeeDetailsDialog(
                                (JFrame) SwingUtilities.getWindowAncestor(EmployeeTable.this),
                                selected
                        );
                        dialog.setVisible(true);
                    }
                }
            }
        });
    }
}