package com.example.demo.controller;

import com.example.demo.model.DeliveryRecord;
import com.example.demo.service.DeliveryRecordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/deliveries")
public class DeliveryRecordController {

    private final DeliveryRecordService deliveryRecordService;

    public DeliveryRecordController(DeliveryRecordService deliveryRecordService) {
        this.deliveryRecordService = deliveryRecordService;
    }

    @PostMapping
    public ResponseEntity<DeliveryRecord> recordDelivery(@RequestBody DeliveryRecord delivery) {
        DeliveryRecord recorded = deliveryRecordService.recordDelivery(delivery);
        return ResponseEntity.ok(recorded);
    }

    @GetMapping("/po/{poId}")
    public ResponseEntity<List<DeliveryRecord>> getDeliveriesByPO(@PathVariable Long poId) {
        List<DeliveryRecord> deliveries = deliveryRecordService.getDeliveriesByPO(poId);
        return ResponseEntity.ok(deliveries);
    }

    @GetMapping
    public ResponseEntity<List<DeliveryRecord>> getAllDeliveries() {
        List<DeliveryRecord> deliveries = deliveryRecordService.getAllDeliveries();
        return ResponseEntity.ok(deliveries);
    }
}
