package com.example.demo.controller;

import com.example.demo.model.PurchaseOrderRecord;
import com.example.demo.service.PurchaseOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/purchase-orders")
public class PurchaseOrderController {
    
    @Autowired
    private PurchaseOrderService service;

    @PostMapping
    public ResponseEntity<PurchaseOrderRecord> createPO(@RequestBody PurchaseOrderRecord po) {
        return ResponseEntity.ok(service.createPurchaseOrder(po));
    }

    @GetMapping("/supplier/{supplierId}")
    public ResponseEntity<List<PurchaseOrderRecord>> getPOsBySupplier(@PathVariable Long supplierId) {
        return ResponseEntity.ok(service.getPOsBySupplier(supplierId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchaseOrderRecord> getPO(@PathVariable Long id) {
        return service.getPOById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}