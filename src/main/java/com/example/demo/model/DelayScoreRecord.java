package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "delay_score_record")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DelayScoreRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long supplierId;
    private Long poId;
    private Integer delayDays;
    private String delaySeverity;
    private Double score;
    
    @Column(name = "computed_at")
    private LocalDateTime computedAt;
    
    @PrePersist
    protected void onCreate() {
        computedAt = LocalDateTime.now();
    }
}