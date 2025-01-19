package com.hahn.erms.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.time.LocalDate;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchRequest {
    private String name;
    private String employeeId;
    private Long departmentId;
    private Long jobTitleId;
    private String employmentStatus;
    private LocalDate hireDateFrom;
    private LocalDate hireDateTo;
    private String contractType;
    private Integer page = 0;
    private Integer size = 20;
    private String sortBy = "id";
    private String sortDirection = "desc";
}