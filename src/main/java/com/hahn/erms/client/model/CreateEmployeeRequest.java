package com.hahn.erms.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.time.LocalDate;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateEmployeeRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private LocalDate hireDate;
    private String contractType;
    private String employmentStatus;
    private Long departmentId;
    private Long jobTitleId;
    private String address;
    private Boolean isAssignedToProject;
}