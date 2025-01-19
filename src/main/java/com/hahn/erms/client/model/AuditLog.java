package com.hahn.erms.client.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AuditLog {
    private String employeeId;
    private Employee oldValue;
    private Employee newValue;
    private String modification;
}