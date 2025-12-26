package com.example.demo.controller;

import com.example.demo.model.PurchaseOrderRecord;
import com.example.demo.service.PurchaseOrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/purchase-orders")
public class PurchaseOrderController {

    private final PurchaseOrderService purchaseOrderService;

    public PurchaseOrderController(PurchaseOrderService purchaseOrderService) {
        this.purchaseOrderService = purchaseOrderService;
    }

    @PostMapping
    public ResponseEntity<PurchaseOrderRecord> createPurchaseOrder(@RequestBody PurchaseOrderRecord po) {
        PurchaseOrderRecord created = purchaseOrderService.createPurchaseOrder(po);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/supplier/{supplierId}")
    public ResponseEntity<List<PurchaseOrderRecord>> getPOsBySupplier(@PathVariable Long supplierId) {
        List<PurchaseOrderRecord> pos = purchaseOrderService.getPOsBySupplier(supplierId);
        return ResponseEntity.ok(pos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchaseOrderRecord> getPOById(@PathVariable Long id) {
        return purchaseOrderService.getPOById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
