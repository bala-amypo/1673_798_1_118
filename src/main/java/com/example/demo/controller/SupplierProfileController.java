package com.example.demo.controller;

import com.example.demo.model.SupplierProfile;
import com.example.demo.service.SupplierProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
public class SupplierProfileController {

    private final SupplierProfileService supplierProfileService;

    public SupplierProfileController(SupplierProfileService supplierProfileService) {
        this.supplierProfileService = supplierProfileService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupplierProfile> getSupplierById(@PathVariable Long id) {
        SupplierProfile supplier = supplierProfileService.getSupplierById(id);
        return ResponseEntity.ok(supplier);
    }

    @PostMapping
    public ResponseEntity<SupplierProfile> createSupplier(@RequestBody SupplierProfile supplier) {
        SupplierProfile created = supplierProfileService.createSupplier(supplier);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<SupplierProfile> updateStatus(@PathVariable Long id, @RequestParam boolean active) {
        SupplierProfile updated = supplierProfileService.updateSupplierStatus(id, active);
        return ResponseEntity.ok(updated);
    }

    @GetMapping
    public ResponseEntity<List<SupplierProfile>> getAllSuppliers() {
        List<SupplierProfile> suppliers = supplierProfileService.getAllSuppliers();
        return ResponseEntity.ok(suppliers);
    }
}
