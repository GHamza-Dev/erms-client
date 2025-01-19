package com.hahn.erms.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateEmployeeRequest {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String employmentStatus;
    private Boolean isAssignedToProject;
}