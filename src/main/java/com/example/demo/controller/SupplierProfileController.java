package com.example.demo.controller;

import com.example.demo.model.SupplierProfile;
import com.example.demo.service.SupplierProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
public class SupplierProfileController {
    
    @Autowired
    private SupplierProfileService service;

    @PostMapping
    public ResponseEntity<SupplierProfile> createSupplier(@RequestBody SupplierProfile supplier) {
        return ResponseEntity.ok(service.createSupplier(supplier));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupplierProfile> getSupplier(@PathVariable Long id) {
        return ResponseEntity.ok(service.getSupplierById(id));
    }

    @GetMapping
    public ResponseEntity<List<SupplierProfile>> getAllSuppliers() {
        return ResponseEntity.ok(service.getAllSuppliers());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<SupplierProfile> updateStatus(@PathVariable Long id, @RequestParam Boolean active) {
        return ResponseEntity.ok(service.updateSupplierStatus(id, active));
    }
}