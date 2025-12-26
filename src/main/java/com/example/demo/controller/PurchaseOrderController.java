package com.example.demo.controller;

import com.example.demo.model.PurchaseOrderRecord;
import com.example.demo.service.PurchaseOrderService;
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
    public PurchaseOrderRecord create(@RequestBody PurchaseOrderRecord po) {
        return purchaseOrderService.createPurchaseOrder(po);
    }

    @GetMapping("/supplier/{supplierId}")
    public List<PurchaseOrderRecord> getBySupplier(@PathVariable Long supplierId) {
        return purchaseOrderService.getPOsBySupplier(supplierId);
    }

    @GetMapping("/{id}")
    public PurchaseOrderRecord getById(@PathVariable Long id) {
        return purchaseOrderService.getPOById(id).orElse(null);
    }

    @GetMapping
    public List<PurchaseOrderRecord> getAll() {
        return purchaseOrderService.getAllPurchaseOrders();
    }
}
