package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "supplier_risk_alert")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupplierRiskAlert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long supplierId;
    private String alertLevel;
    private String message;
    
    @Column(name = "alert_date")
    private LocalDateTime alertDate;
    
    private Boolean resolved = false;
    
    @PrePersist
    protected void onCreate() {
        if (alertDate == null) {
            alertDate = LocalDateTime.now();
        }
    }
}