package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "delay_scores")
public class DelayScoreRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long supplierId;

    @Column(nullable = false)
    private Long poId;

    @Column(nullable = false)
    private Integer delayDays;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DelaySeverity delaySeverity;

    @Column(nullable = false)
    private Double score;

    @Column(nullable = false)
    private LocalDateTime computedAt;

    public DelayScoreRecord() {
    }

    @PrePersist
    public void prePersist() {
        if (computedAt == null) {
            computedAt = LocalDateTime.now();
        }
    }

    // getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getSupplierId() { return supplierId; }
    public void setSupplierId(Long supplierId) { this.supplierId = supplierId; }

    public Long getPoId() { return poId; }
    public void setPoId(Long poId) { this.poId = poId; }

    public Integer getDelayDays() { return delayDays; }
    public void setDelayDays(Integer delayDays) { this.delayDays = delayDays; }

    public DelaySeverity getDelaySeverity() { return delaySeverity; }
    public void setDelaySeverity(DelaySeverity delaySeverity) { this.delaySeverity = delaySeverity; }

    public Double getScore() { return score; }
    public void setScore(Double score) { this.score = score; }

    public LocalDateTime getComputedAt() { return computedAt; }
    public void setComputedAt(LocalDateTime computedAt) { this.computedAt = computedAt; }
}
