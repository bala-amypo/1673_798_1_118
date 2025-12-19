package com.example.demo.controller;

import com.example.demo.model.SupplierProfile;
import com.example.demo.service.SupplierProfileService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
public class SupplierProfileController {

    private final SupplierProfileService supplierProfileService;

    public SupplierProfileController(SupplierProfileService supplierProfileService) {
        this.supplierProfileService = supplierProfileService;
    }

    @PostMapping
    public SupplierProfile create(@RequestBody SupplierProfile supplier) {
        return supplierProfileService.createSupplier(supplier);
    }

    @GetMapping("/{id}")
    public SupplierProfile getById(@PathVariable Long id) {
        return supplierProfileService.getSupplierById(id);
    }

    @GetMapping
    public List<SupplierProfile> getAll() {
        return supplierProfileService.getAllSuppliers();
    }

    @PutMapping("/{id}/status")
    public SupplierProfile updateStatus(@PathVariable Long id,
                                        @RequestParam boolean active) {
        return supplierProfileService.updateSupplierStatus(id, active);
    }
}
