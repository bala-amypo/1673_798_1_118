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
                .orElseThrow(() -> new BadRequestException("Invalid PO id")); [cite: 450, 783]

        SupplierProfile supplier = supplierRepo.findById(po.getSupplierId())
                .orElseThrow(() -> new BadRequestException("Invalid supplierId")); [cite: 135, 1037]

        if (!supplier.getActive()) {
            throw new BadRequestException("Inactive supplier"); [cite: 451, 1040]
        }

        List<DeliveryRecord> deliveries = deliveryRepo.findByPoId(poId); [cite: 326]
        if (deliveries.isEmpty()) {
            throw new BadRequestException("No deliveries"); [cite: 452, 1039]
        }

        // Use the latest delivery for delay calculation [cite: 970]
        DeliveryRecord latestDelivery = deliveries.stream()
                .max((d1, d2) -> d1.getActualDeliveryDate().compareTo(d2.getActualDeliveryDate()))
                .get();

        long daysBetween = ChronoUnit.DAYS.between(po.getPromisedDeliveryDate(), latestDelivery.getActualDeliveryDate());
        int delayDays = (int) daysBetween; [cite: 971]

        DelayScoreRecord record = new DelayScoreRecord();
        record.setPoId(poId);
        record.setSupplierId(po.getSupplierId());
        record.setDelayDays(delayDays);

        // Severity and scoring logic [cite: 972, 973]
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
        updateSupplierRiskStatus(po.getSupplierId()); [cite: 551]
        return saved;
    }

    @Override
    public List<DelayScoreRecord> getScoresBySupplier(Long supplierId) {
        return delayScoreRepo.findBySupplierId(supplierId); [cite: 342, 456]
    }

    @Override
    public java.util.Optional<DelayScoreRecord> getScoreById(Long id) {
        return delayScoreRepo.findById(id); [cite: 458]
    }

    // This method resolves the "is not abstract and does not override" error
    @Override
    public List<DelayScoreRecord> getAllScores() {
        return delayScoreRepo.findAll(); [cite: 461, 463]
    }

    private void updateSupplierRiskStatus(Long supplierId) {
        List<DelayScoreRecord> scores = delayScoreRepo.findBySupplierId(supplierId);
        if (scores.isEmpty()) return;

        double avgScore = scores.stream().mapToDouble(DelayScoreRecord::getScore).average().orElse(0.0);
        String level;
        
        // Risk level thresholds [cite: 977-980]
        if (avgScore >= 75) level = "LOW";
        else if (avgScore >= 50) level = "MEDIUM";
        else level = "HIGH";

        // Corrected instantiation using default constructor
        SupplierRiskAlert alert = new SupplierRiskAlert(); [cite: 249]
        alert.setSupplierId(supplierId);
        alert.setAlertLevel(level);
        alert.setMessage("Performance alert: Average score " + avgScore);
        riskAlertService.createAlert(alert); [cite: 467]
    }
}