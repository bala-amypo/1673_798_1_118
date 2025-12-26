package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "supplier_risk_alerts")
public class SupplierRiskAlert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long supplierId;

    @Column(nullable = false)
    private String alertLevel;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private LocalDateTime alertDate;

    @Column(nullable = false)
    private Boolean resolved = false; // Initialize to match test expectations [cite: 930]

    public SupplierRiskAlert() {}

    @PrePersist
    protected void onCreate() {
        if (alertDate == null) alertDate = LocalDateTime.now();
        if (resolved == null) resolved = false; // Fixes testAlertCreationDefaultResolvedFalse [cite: 931]
    }

    // Standard Getters and Setters...
    public Boolean getResolved() { return resolved; }
    public void setResolved(Boolean resolved) { this.resolved = resolved; }
    public Long getSupplierId() { return supplierId; }
    public void setSupplierId(Long supplierId) { this.supplierId = supplierId; }
}