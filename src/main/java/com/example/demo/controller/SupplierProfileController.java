package com.example.demo.controller;

import com.example.demo.model.SupplierProfile;
import com.example.demo.service.SupplierProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
@Tag(name = "Supplier Profile", description = "Supplier management endpoints")
@SecurityRequirement(name = "Bearer Authentication")
public class SupplierProfileController {
    
    private final SupplierProfileService supplierService;
    
    public SupplierProfileController(SupplierProfileService supplierService) {
        this.supplierService = supplierService;
    }
    
    @PostMapping
    @Operation(summary = "Create a new supplier")
    public ResponseEntity<SupplierProfile> createSupplier(@RequestBody SupplierProfile supplier) {
        return ResponseEntity.ok(supplierService.createSupplier(supplier));
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get supplier by ID")
    public ResponseEntity<SupplierProfile> getSupplierById(@PathVariable Long id) {
        return ResponseEntity.ok(supplierService.getSupplierById(id));
    }
    
    @GetMapping
    @Operation(summary = "Get all suppliers")
    public ResponseEntity<List<SupplierProfile>> getAllSuppliers() {
        return ResponseEntity.ok(supplierService.getAllSuppliers());
    }
    
    @PutMapping("/{id}/status")
    @Operation(summary = "Update supplier status")
    public ResponseEntity<SupplierProfile> updateSupplierStatus(@PathVariable Long id, @RequestParam boolean active) {
        return ResponseEntity.ok(supplierService.updateSupplierStatus(id, active));
    }
    
    @GetMapping("/lookup/{supplierCode}")
    @Operation(summary = "Lookup supplier by code")
    public ResponseEntity<SupplierProfile> lookupByCode(@PathVariable String supplierCode) {
        return ResponseEntity.ok(supplierService.getBySupplierCode(supplierCode));
    }
}