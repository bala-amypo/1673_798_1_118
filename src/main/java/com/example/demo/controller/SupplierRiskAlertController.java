package com.example.demo.controller;

import com.example.demo.model.SupplierRiskAlert;
import com.example.demo.service.SupplierRiskAlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/risk-alerts")
public class SupplierRiskAlertController {
    
    @Autowired
    private SupplierRiskAlertService service;

    @PostMapping
    public ResponseEntity<SupplierRiskAlert> createAlert(@RequestBody SupplierRiskAlert alert) {
        return ResponseEntity.ok(service.createAlert(alert));
    }

    @GetMapping("/supplier/{supplierId}")
    public ResponseEntity<List<SupplierRiskAlert>> getAlertsBySupplier(@PathVariable Long supplierId) {
        return ResponseEntity.ok(service.getAlertsBySupplier(supplierId));
    }

    @PutMapping("/{alertId}/resolve")
    public ResponseEntity<SupplierRiskAlert> resolveAlert(@PathVariable Long alertId) {
        return ResponseEntity.ok(service.resolveAlert(alertId));
    }

    @GetMapping
    public ResponseEntity<List<SupplierRiskAlert>> getAllAlerts() {
        return ResponseEntity.ok(service.getAllAlerts());
    }
}