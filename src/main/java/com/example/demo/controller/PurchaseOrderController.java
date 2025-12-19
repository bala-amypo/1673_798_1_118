package com.example.demo.controller;

import com.example.demo.model.PurchaseOrderRecord;
import com.example.demo.service.PurchaseOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/purchase-orders")
@Tag(name = "Purchase Orders", description = "Purchase order management endpoints")
@SecurityRequirement(name = "Bearer Authentication")
public class PurchaseOrderController {
    
    private final PurchaseOrderService poService;
    
    public PurchaseOrderController(PurchaseOrderService poService) {
        this.poService = poService;
    }
    
    @PostMapping
    @Operation(summary = "Create a new purchase order")
    public ResponseEntity<PurchaseOrderRecord> createPO(@RequestBody PurchaseOrderRecord po) {
        return ResponseEntity.ok(poService.createPurchaseOrder(po));
    }
    
    @GetMapping("/supplier/{supplierId}")
    @Operation(summary = "Get purchase orders by supplier")
    public ResponseEntity<List<PurchaseOrderRecord>> getPOsBySupplier(@PathVariable Long supplierId) {
        return ResponseEntity.ok(poService.getPOsBySupplier(supplierId));
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get purchase order by ID")
    public ResponseEntity<PurchaseOrderRecord> getPOById(@PathVariable Long id) {
        return ResponseEntity.ok(poService.getPOById(id));
    }
    
    @GetMapping
    @Operation(summary = "Get all purchase orders")
    public ResponseEntity<List<PurchaseOrderRecord>> getAllPOs() {
        return ResponseEntity.ok(poService.getAllPurchaseOrders());
    }
}