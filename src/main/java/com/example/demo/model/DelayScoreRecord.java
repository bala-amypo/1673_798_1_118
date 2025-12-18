package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "delay_scores")
public class DelayScoreRecord {
    // ...
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DelaySeverity delaySeverity;
    // ...
}
