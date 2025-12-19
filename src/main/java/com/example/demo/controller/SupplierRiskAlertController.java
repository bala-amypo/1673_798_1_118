package com.example.demo.controller;

import com.example.demo.model.SupplierRiskAlert;
import com.example.demo.service.SupplierRiskAlertService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/risk-alerts")
@Tag(name = "Risk Alerts", description = "Supplier risk alert management endpoints")
@SecurityRequirement(name = "Bearer Authentication")
public class SupplierRiskAlertController {
    
    private final SupplierRiskAlertService alertService;
    
    public SupplierRiskAlertController(SupplierRiskAlertService alertService) {
        this.alertService = alertService;
    }
    
    @PostMapping
    @Operation(summary = "Create a new risk alert")
    public ResponseEntity<SupplierRiskAlert> createAlert(@RequestBody SupplierRiskAlert alert) {
        return ResponseEntity.ok(alertService.createAlert(alert));
    }
    
    @PutMapping("/{id}/resolve")
    @Operation(summary = "Resolve a risk alert")
    public ResponseEntity<SupplierRiskAlert> resolveAlert(@PathVariable Long id) {
        return ResponseEntity.ok(alertService.resolveAlert(id));
    }
    
    @GetMapping("/supplier/{supplierId}")
    @Operation(summary = "Get risk alerts by supplier")
    public ResponseEntity<List<SupplierRiskAlert>> getAlertsBySupplier(@PathVariable Long supplierId) {
        return ResponseEntity.ok(alertService.getAlertsBySupplier(supplierId));
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get risk alert by ID")
    public ResponseEntity<SupplierRiskAlert> getAlertById(@PathVariable Long id) {
        SupplierRiskAlert alert = alertService.getAllAlerts().stream()
                .filter(a -> a.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new com.example.demo.exception.ResourceNotFoundException("Alert not found"));
        return ResponseEntity.ok(alert);
    }
    
    @GetMapping
    @Operation(summary = "Get all risk alerts")
    public ResponseEntity<List<SupplierRiskAlert>> getAllAlerts() {
        return ResponseEntity.ok(alertService.getAllAlerts());
    }
}