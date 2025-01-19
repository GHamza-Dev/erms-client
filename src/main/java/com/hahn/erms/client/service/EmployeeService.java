package com.hahn.erms.client.service;

import com.hahn.erms.client.api.ApiClient;
import com.hahn.erms.client.config.AppConfig;
import com.hahn.erms.client.model.*;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;

public class EmployeeService {
    private final ApiClient apiClient;

    public EmployeeService() {
        this.apiClient = new ApiClient();
    }

    public PagedResponse<Employee> searchEmployees(SearchRequest request) throws Exception {
        String url = AppConfig.getApiBaseUrl() + AppConfig.getEmployeesPath() + "/search";
        return apiClient.post(url, request, new TypeReference<PagedResponse<Employee>>() {});
    }

    public Employee getEmployee(Long id) throws Exception {
        String url = AppConfig.getApiBaseUrl() + AppConfig.getEmployeesPath() + "/" + id;
        return apiClient.get(url, Employee.class);
    }

    public Employee createEmployee(CreateEmployeeRequest request) throws Exception {
        String url = AppConfig.getApiBaseUrl() + AppConfig.getEmployeesPath();
        return apiClient.post(url, request, Employee.class);
    }

    public Employee updateEmployee(UpdateEmployeeRequest request) throws Exception {
        String url = AppConfig.getApiBaseUrl() + AppConfig.getEmployeesPath();
        return apiClient.put(url, request, Employee.class);
    }

    public void deleteEmployee(Long id) throws Exception {
        String url = AppConfig.getApiBaseUrl() + AppConfig.getEmployeesPath() + "/" + id;
        apiClient.delete(url);
    }

    public List<Department> getAllDepartments() throws Exception {
        String url = AppConfig.getApiBaseUrl() + "/departments";
        return apiClient.get(url, new TypeReference<List<Department>>() {});
    }

    public List<JobTitle> getAllJobTitles() throws Exception {
        String url = AppConfig.getApiBaseUrl() + "/job-titles";
        return apiClient.get(url, new TypeReference<List<JobTitle>>() {});
    }
}