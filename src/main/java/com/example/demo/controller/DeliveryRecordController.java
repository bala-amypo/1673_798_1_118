package com.example.demo.controller;

import com.example.demo.model.DeliveryRecord;
import com.example.demo.service.DeliveryRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/deliveries")
@Tag(name = "Deliveries", description = "Delivery record management endpoints")
@SecurityRequirement(name = "Bearer Authentication")
public class DeliveryRecordController {
    
    private final DeliveryRecordService deliveryService;
    
    public DeliveryRecordController(DeliveryRecordService deliveryService) {
        this.deliveryService = deliveryService;
    }
    
    @PostMapping
    @Operation(summary = "Record a new delivery")
    public ResponseEntity<DeliveryRecord> recordDelivery(@RequestBody DeliveryRecord delivery) {
        return ResponseEntity.ok(deliveryService.recordDelivery(delivery));
    }
    
    @GetMapping("/po/{poId}")
    @Operation(summary = "Get deliveries by purchase order")
    public ResponseEntity<List<DeliveryRecord>> getDeliveriesByPO(@PathVariable Long poId) {
        return ResponseEntity.ok(deliveryService.getDeliveriesByPO(poId));
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get delivery by ID")
    public ResponseEntity<DeliveryRecord> getDeliveryById(@PathVariable Long id) {
        return ResponseEntity.ok(deliveryService.getDeliveryById(id));
    }
    
    @GetMapping
    @Operation(summary = "Get all deliveries")
    public ResponseEntity<List<DeliveryRecord>> getAllDeliveries() {
        return ResponseEntity.ok(deliveryService.getAllDeliveries());
    }
}