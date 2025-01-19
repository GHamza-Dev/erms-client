package com.hahn.erms.client.service;

import com.hahn.erms.client.api.ApiClient;
import com.hahn.erms.client.config.AppConfig;
import com.hahn.erms.client.model.AuditLog;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.List;

public class AuditService {
    private final ApiClient apiClient;

    public AuditService() {
        this.apiClient = new ApiClient();
    }

    public List<AuditLog> getAuditLogs(String employeeId) throws Exception {
        String url = AppConfig.getApiBaseUrl() + "/audit/employees/" + employeeId;
        return apiClient.get(url, new TypeReference<List<AuditLog>>() {});
    }
}