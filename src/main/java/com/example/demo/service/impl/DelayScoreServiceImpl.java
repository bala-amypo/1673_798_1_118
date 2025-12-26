package com.example.demo.service.impl;

import com.example.demo.exception.BadRequestException;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.service.DelayScoreService;
import com.example.demo.service.SupplierRiskAlertService;
import org.springframework.stereotype.Service;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class DelayScoreServiceImpl implements DelayScoreService {

    private final DelayScoreRecordRepository delayScoreRepo;
    private final PurchaseOrderRecordRepository poRepo;
    private final DeliveryRecordRepository deliveryRepo;
    private final SupplierProfileRepository supplierRepo;
    private final SupplierRiskAlertService riskAlertService;

    public DelayScoreServiceImpl(DelayScoreRecordRepository delayScoreRepo, 
                                PurchaseOrderRecordRepository poRepo,
                                DeliveryRecordRepository deliveryRepo, 
                                SupplierProfileRepository supplierRepo,
                                SupplierRiskAlertService riskAlertService) {
        this.delayScoreRepo = delayScoreRepo;
        this.poRepo = poRepo;
        this.deliveryRepo = deliveryRepo;
        this.supplierRepo = supplierRepo;
        this.riskAlertService = riskAlertService;
    }

    @Override
    public DelayScoreRecord computeDelayScore(Long poId) {
        PurchaseOrderRecord po = poRepo.findById(poId)
                .orElseThrow(() -> new BadRequestException("Invalid PO id"));

        SupplierProfile supplier = supplierRepo.findById(po.getSupplierId())
                .orElseThrow(() -> new BadRequestException("Invalid supplierId"));

        if (!supplier.getActive()) {
            throw new BadRequestException("Inactive supplier");
        }

        List<DeliveryRecord> deliveries = deliveryRepo.findByPoId(poId);
        if (deliveries.isEmpty()) {
            throw new BadRequestException("No deliveries");
        }

        // Logic: Use the latest delivery date for delay calculation
        DeliveryRecord latestDelivery = deliveries.stream()
                .max((d1, d2) -> d1.getActualDeliveryDate().compareTo(d2.getActualDeliveryDate()))
                .get();

        long daysBetween = ChronoUnit.DAYS.between(po.getPromisedDeliveryDate(), latestDelivery.getActualDeliveryDate());
        int delayDays = (int) Math.max(0, daysBetween);

        DelayScoreRecord record = new DelayScoreRecord();
        record.setPoId(poId);
        record.setSupplierId(po.getSupplierId());
        record.setDelayDays(delayDays);

        // Scoring rules 
        if (delayDays <= 0) {
            record.setDelaySeverity("ON_TIME");
            record.setScore(100.0);
        } else if (delayDays <= 3) {
            record.setDelaySeverity("MINOR");
            record.setScore(75.0);
        } else if (delayDays <= 7) {
            record.setDelaySeverity("MODERATE");
            record.setScore(50.0);
        } else {
            record.setDelaySeverity("SEVERE");
            record.setScore(0.0);
        }

        DelayScoreRecord saved = delayScoreRepo.save(record);
        updateSupplierRiskStatus(po.getSupplierId());
        return saved;
    }

    private void updateSupplierRiskStatus(Long supplierId) {
        List<DelayScoreRecord> scores = delayScoreRepo.findBySupplierId(supplierId);
        if (scores.isEmpty()) return;

        double avgScore = scores.stream().mapToDouble(DelayScoreRecord::getScore).average().orElse(0.0);
        String level;
        
        // Risk levels 
        if (avgScore >= 75) level = "LOW";
        else if (avgScore >= 50) level = "MEDIUM";
        else level = "HIGH";

        SupplierRiskAlert alert = new SupplierRiskAlert();
        alert.setSupplierId(supplierId);
        alert.setAlertLevel(level);
        alert.setMessage("Performance alert: Average score " + avgScore);
        riskAlertService.createAlert(alert);
    }
}