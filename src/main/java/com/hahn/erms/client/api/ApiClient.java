package com.hahn.erms.client.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hahn.erms.client.config.AppConfig;
import okhttp3.*;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class ApiClient {
    private final OkHttpClient client;
    private final ObjectMapper objectMapper;
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    public ApiClient() {
        this.client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();

        this.objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule()); // For LocalDate serialization
    }

    public <T> T get(String url, Class<T> responseType) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "Bearer " + AppConfig.getJwtToken())
                .get()
                .build();

        return executeRequest(request, responseType);
    }

    public <T> T get(String url, TypeReference<T> responseType) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "Bearer " + AppConfig.getJwtToken())
                .get()
                .build();

        return executeRequest(request, responseType);
    }

    public <T> T post(String url, Object requestBody, Class<T> responseType) throws IOException {
        RequestBody body = RequestBody.create(
                objectMapper.writeValueAsString(requestBody),
                JSON
        );

        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "Bearer " + AppConfig.getJwtToken())
                .post(body)
                .build();

        return executeRequest(request, responseType);
    }

    public <T> T post(String url, Object requestBody, TypeReference<T> responseType) throws IOException {
        RequestBody body = RequestBody.create(
                objectMapper.writeValueAsString(requestBody),
                JSON
        );

        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "Bearer " + AppConfig.getJwtToken())
                .post(body)
                .build();

        return executeRequest(request, responseType);
    }

    public <T> T put(String url, Object requestBody, Class<T> responseType) throws IOException {
        RequestBody body = RequestBody.create(
                objectMapper.writeValueAsString(requestBody),
                JSON
        );

        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "Bearer " + AppConfig.getJwtToken())
                .put(body)
                .build();

        return executeRequest(request, responseType);
    }

    public void delete(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "Bearer " + AppConfig.getJwtToken())
                .delete()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected response " + response);
            }
        }
    }

    private <T> T executeRequest(Request request, Class<T> responseType) throws IOException {
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected response " + response);
            }

            String responseString = response.body().string();
            return objectMapper.readValue(responseString, responseType);
        }
    }

    private <T> T executeRequest(Request request, TypeReference<T> responseType) throws IOException {
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected response " + response);
            }

            String responseString = response.body().string();
            return objectMapper.readValue(responseString, responseType);
        }
    }

    // Method for authentication (doesn't require token)
    public <T> T authenticate(String url, Object requestBody, Class<T> responseType) throws IOException {
        RequestBody body = RequestBody.create(
                objectMapper.writeValueAsString(requestBody),
                JSON
        );

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        return executeRequest(request, responseType);
    }
}