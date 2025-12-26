package com.example.demo.service.impl;

import com.example.demo.exception.BadRequestException;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.service.DelayScoreService;
import com.example.demo.service.SupplierRiskAlertService;
import org.springframework.stereotype.Service;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class DelayScoreServiceImpl implements DelayScoreService {

    private final DelayScoreRecordRepository delayScoreRepo;
    private final PurchaseOrderRecordRepository poRepo;
    private final DeliveryRecordRepository deliveryRepo;
    private final SupplierProfileRepository supplierRepo;
    private final SupplierRiskAlertService riskAlertService;

    // Constructor Injection
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
        // Validation: Invalid PO id
        PurchaseOrderRecord po = poRepo.findById(poId)
                .orElseThrow(() -> new BadRequestException("Invalid PO id"));

        // Validation: Invalid supplierId
        SupplierProfile supplier = supplierRepo.findById(po.getSupplierId())
                .orElseThrow(() -> new BadRequestException("Invalid supplierId"));

        // Validation: Inactive supplier
        if (supplier.getActive() == null || !supplier.getActive()) {
            throw new BadRequestException("Inactive supplier");
        }

        // Validation: No deliveries
        List<DeliveryRecord> deliveries = deliveryRepo.findByPoId(poId);
        if (deliveries.isEmpty()) {
            throw new BadRequestException("No deliveries");
        }

        // Logic: Use latest delivery for delay calculation
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

    @Override
    public List<DelayScoreRecord> getScoresBySupplier(Long supplierId) {
        return delayScoreRepo.findBySupplierId(supplierId);
    }

    @Override
    public Optional<DelayScoreRecord> getScoreById(Long id) {
        return delayScoreRepo.findById(id);
    }

    @Override
    public List<DelayScoreRecord> getAllScores() {
        return delayScoreRepo.findAll();
    }

    private void updateSupplierRiskStatus(Long supplierId) {
        List<DelayScoreRecord> scores = delayScoreRepo.findBySupplierId(supplierId);
        if (scores.isEmpty()) return;

        double avgScore = scores.stream().mapToDouble(DelayScoreRecord::getScore).average().orElse(0.0);
        
        // Risk levels per SRS
        String level;
        if (avgScore >= 75) level = "LOW";
        else if (avgScore >= 50) level = "MEDIUM";
        else level = "HIGH";

        // Resolved previous constructor error by using setters
        SupplierRiskAlert alert = new SupplierRiskAlert();
        alert.setSupplierId(supplierId);
        alert.setAlertLevel(level);
        alert.setMessage("Performance alert: Average score " + avgScore);
        riskAlertService.createAlert(alert);
    }
}