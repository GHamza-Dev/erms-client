package com.hahn.erms.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.time.LocalDate;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmployeeProjection {
    private Long id;
    private String employeeId;
    private String firstName;
    private String lastName;
    private String employmentStatus;
    private String email;
    private String phone;
    private DepartmentInfo department;
    private JobTitleInfo jobTitle;
    private ContractInfo contract;

    @Data
    public static class DepartmentInfo {
        private String name;
    }

    @Data
    public static class JobTitleInfo {
        private String title;
    }

    @Data
    public static class ContractInfo {
        private LocalDate hireDate;
    }
}