package com.example.demo.controller;

import com.example.demo.model.SupplierRiskAlert;
import com.example.demo.service.SupplierRiskAlertService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/risk-alerts")
public class SupplierRiskAlertController {

    private final SupplierRiskAlertService supplierRiskAlertService;

    public SupplierRiskAlertController(SupplierRiskAlertService supplierRiskAlertService) {
        this.supplierRiskAlertService = supplierRiskAlertService;
    }

    @PostMapping
    public SupplierRiskAlert create(@RequestBody SupplierRiskAlert alert) {
        return supplierRiskAlertService.createAlert(alert);
    }

    @PutMapping("/{id}/resolve")
    public SupplierRiskAlert resolve(@PathVariable Long id) {
        return supplierRiskAlertService.resolveAlert(id);
    }

    @GetMapping("/supplier/{supplierId}")
    public List<SupplierRiskAlert> getBySupplier(@PathVariable Long supplierId) {
        return supplierRiskAlertService.getAlertsBySupplier(supplierId);
    }

    @GetMapping("/{id}")
    public SupplierRiskAlert getById(@PathVariable Long id) {
        return supplierRiskAlertService.getAlertById(id);
    }

    @GetMapping
    public List<SupplierRiskAlert> getAll() {
        return supplierRiskAlertService.getAllAlerts();
    }
}
