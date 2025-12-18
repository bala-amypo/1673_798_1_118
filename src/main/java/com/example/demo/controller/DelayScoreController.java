package com.example.demo.controller;

import com.example.demo.model.DelayScoreRecord;
import com.example.demo.service.DelayScoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/delay-scores")
@Tag(name = "Delay Scores", description = "Delay score computation and management endpoints")
@SecurityRequirement(name = "Bearer Authentication")
public class DelayScoreController {
    
    private final DelayScoreService delayScoreService;
    
    public DelayScoreController(DelayScoreService delayScoreService) {
        this.delayScoreService = delayScoreService;
    }
    
    @PostMapping("/compute/{poId}")
    @Operation(summary = "Compute delay score for a purchase order")
    public ResponseEntity<DelayScoreRecord> computeDelayScore(@PathVariable Long poId) {
        return ResponseEntity.ok(delayScoreService.computeDelayScore(poId));
    }
    
    @GetMapping("/supplier/{supplierId}")
    @Operation(summary = "Get delay scores by supplier")
    public ResponseEntity<List<DelayScoreRecord>> getScoresBySupplier(@PathVariable Long supplierId) {
        return ResponseEntity.ok(delayScoreService.getScoresBySupplier(supplierId));
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get delay score by ID")
    public ResponseEntity<DelayScoreRecord> getScoreById(@PathVariable Long id) {
        return ResponseEntity.ok(delayScoreService.getScoreById(id));
    }
    
    @GetMapping
    @Operation(summary = "Get all delay scores")
    public ResponseEntity<List<DelayScoreRecord>> getAllScores() {
        return ResponseEntity.ok(delayScoreService.getAllScores());
    }
}