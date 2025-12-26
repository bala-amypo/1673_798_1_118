package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "supplier_risk_alerts")
public class SupplierRiskAlert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long supplierId;
    private String alertLevel;
    private Boolean resolved = false;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getSupplierId() { return supplierId; }
    public void setSupplierId(Long supplierId) { this.supplierId = supplierId; }
    
    public String getAlertLevel() { return alertLevel; }
    public void setAlertLevel(String alertLevel) { this.alertLevel = alertLevel; }
    
    public Boolean getResolved() { return resolved; }
    public void setResolved(Boolean resolved) { this.resolved = resolved; }
}