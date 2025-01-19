package com.hahn.erms.client.service;

import com.hahn.erms.client.api.ApiClient;
import com.hahn.erms.client.config.AppConfig;
import com.hahn.erms.client.model.AuthRequest;
import com.hahn.erms.client.model.AuthResponse;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

public class AuthService {
    private final ApiClient apiClient;

    public AuthService() {
        this.apiClient = new ApiClient();
    }

    public boolean login(String username, String password) {
        try {
            // Validate input
            if (username == null || username.trim().isEmpty() ||
                    password == null || password.trim().isEmpty()) {
                return false;
            }

            AuthRequest request = new AuthRequest(username, password);

            String url = AppConfig.getApiBaseUrl() + AppConfig.getAuthPath();
            AuthResponse response = apiClient.post(url, request, AuthResponse.class);

            if (response != null && response.getAccessToken() != null) {
                // Store the token
                AppConfig.setJwtToken(response.getAccessToken());
                return true;
            }
            return false;
        } catch (Exception e) {
            // Log the error (consider using a proper logging framework)
            System.err.println("Login failed: " + e.getMessage());
            return false;
        }
    }

    public List<String> getCurrentUserRoles() {
        try {
            String token = AppConfig.getJwtToken();
            if (token == null || token.isEmpty()) {
                return Collections.emptyList();
            }

            // Decode token without signature verification
            String[] parts = token.split("\\.");
            if (parts.length < 2) {
                return Collections.emptyList();
            }

            // Base64 decode the payload
            Base64.Decoder decoder = java.util.Base64.getUrlDecoder();
            String payloadJson = new String(decoder.decode(parts[1]));

            // Parse JSON payload
            JSONObject payload = new JSONObject(payloadJson);

            // Extract roles
            List<String> roles = new ArrayList<>();

            // Common JWT role claim formats
            if (payload.has("roles")) {
                // If roles is a JSON array
                if (payload.get("roles") instanceof org.json.JSONArray) {
                    org.json.JSONArray rolesArray = payload.getJSONArray("roles");
                    for (int i = 0; i < rolesArray.length(); i++) {
                        roles.add(rolesArray.getString(i));
                    }
                } else {
                    // If roles is a comma-separated string
                    String rolesString = payload.getString("roles");
                    String[] rolesArray = rolesString.split(",");
                    for (String role : rolesArray) {
                        roles.add(role.trim());
                    }
                }
            } else if (payload.has("authorities")) {
                // Alternative claim name
                org.json.JSONArray authoritiesArray = payload.getJSONArray("authorities");
                for (int i = 0; i < authoritiesArray.length(); i++) {
                    roles.add(authoritiesArray.getString(i));
                }
            }

            return roles;
        } catch (Exception e) {
            System.err.println("Error decoding roles: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public String getCurrentUsername() {
        try {
            String token = AppConfig.getJwtToken();
            if (token == null || token.isEmpty()) {
                return null;
            }

            // Decode token without signature verification
            String[] parts = token.split("\\.");
            if (parts.length < 2) {
                return null;
            }

            // Base64 decode the payload
            Base64.Decoder decoder = java.util.Base64.getUrlDecoder();
            String payloadJson = new String(decoder.decode(parts[1]));

            // Parse JSON payload
            JSONObject payload = new JSONObject(payloadJson);

            // Common username claim names
            if (payload.has("sub")) {
                return payload.getString("sub");
            } else if (payload.has("username")) {
                return payload.getString("username");
            }

            return null;
        } catch (Exception e) {
            System.err.println("Error decoding username: " + e.getMessage());
            return null;
        }
    }

    public void logout() {
        AppConfig.setJwtToken(null);
    }

    public boolean isLoggedIn() {
        String token = AppConfig.getJwtToken();
        return token != null && !token.isEmpty();
    }
}