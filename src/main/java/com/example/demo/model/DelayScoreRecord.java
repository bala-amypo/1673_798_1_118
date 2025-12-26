package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "delay_score_records")
public class DelayScoreRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long poId;
    private Long supplierId;
    private Integer delayDays;
    private String delaySeverity;
    private Double score;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getPoId() { return poId; }
    public void setPoId(Long poId) { this.poId = poId; }
    
    public Long getSupplierId() { return supplierId; }
    public void setSupplierId(Long supplierId) { this.supplierId = supplierId; }
    
    public Integer getDelayDays() { return delayDays; }
    public void setDelayDays(Integer delayDays) { this.delayDays = delayDays; }
    
    public String getDelaySeverity() { return delaySeverity; }
    public void setDelaySeverity(String delaySeverity) { this.delaySeverity = delaySeverity; }
    
    public Double getScore() { return score; }
    public void setScore(Double score) { this.score = score; }
}