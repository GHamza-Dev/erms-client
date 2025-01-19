package com.hahn.erms.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class JobTitle {
    private Long id;
    private String title;
    private String description;
}