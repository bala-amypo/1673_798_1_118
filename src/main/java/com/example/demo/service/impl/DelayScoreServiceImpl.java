package com.example.demo.service.impl;

import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.*;
import com.example.demo.repository.DelayScoreRecordRepository;
import com.example.demo.repository.DeliveryRecordRepository;
import com.example.demo.repository.PurchaseOrderRecordRepository;
import com.example.demo.repository.SupplierProfileRepository;
import com.example.demo.service.DelayScoreService;
import com.example.demo.service.SupplierRiskAlertService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Service
public class DelayScoreServiceImpl implements DelayScoreService {

    private final DelayScoreRecordRepository delayScoreRecordRepository;
    private final PurchaseOrderRecordRepository purchaseOrderRecordRepository;
    private final DeliveryRecordRepository deliveryRecordRepository;
    private final SupplierProfileRepository supplierProfileRepository;
    private final SupplierRiskAlertService supplierRiskAlertService;

    public DelayScoreServiceImpl(DelayScoreRecordRepository delayScoreRecordRepository,
                                 PurchaseOrderRecordRepository purchaseOrderRecordRepository,
                                 DeliveryRecordRepository deliveryRecordRepository,
                                 SupplierProfileRepository supplierProfileRepository,
                                 SupplierRiskAlertService supplierRiskAlertService) {
        this.delayScoreRecordRepository = delayScoreRecordRepository;
        this.purchaseOrderRecordRepository = purchaseOrderRecordRepository;
        this.deliveryRecordRepository = deliveryRecordRepository;
        this.supplierProfileRepository = supplierProfileRepository;
        this.supplierRiskAlertService = supplierRiskAlertService;
    }

    @Override
    public DelayScoreRecord computeDelayScore(Long poId) {
        PurchaseOrderRecord po = purchaseOrderRecordRepository.findById(poId)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase order not found"));

        SupplierProfile supplier = supplierProfileRepository.findById(po.getSupplierId())
                .orElseThrow(() -> new BadRequestException("Invalid supplierId"));

        if (!Boolean.TRUE.equals(supplier.getActive())) {
            throw new BadRequestException("Inactive supplier");
        }

        List<DeliveryRecord> deliveries = deliveryRecordRepository.findByPoId(poId);
        if (deliveries.isEmpty()) {
            throw new BadRequestException("No deliveries");
        }

        DeliveryRecord latest = deliveries.stream()
                .max(Comparator.comparing(DeliveryRecord::getActualDeliveryDate))
                .orElseThrow(() -> new BadRequestException("No deliveries"));

        LocalDate promised = po.getPromisedDeliveryDate();
        LocalDate actual = latest.getActualDeliveryDate();

        int delayDays = (int) (actual.toEpochDay() - promised.toEpochDay());
        if (delayDays < 0) {
            delayDays = 0;
        }

        String severity;
        if (delayDays == 0) {
            severity = "ON_TIME";
        } else if (delayDays <= 3) {
            severity = "MINOR";
        } else if (delayDays <= 7) {
            severity = "MODERATE";
        } else {
            severity = "SEVERE";
        }

        double score = 100.0 - (delayDays * 5.0);
        if (score < 0.0) {
            score = 0.0;
        }

        DelayScoreRecord record = new DelayScoreRecord(
                supplier.getId(),
                poId,
                delayDays,
                severity,
                score
        );

        DelayScoreRecord saved = delayScoreRecordRepository.save(record);

        if ("SEVERE".equals(severity)) {
            SupplierRiskAlert alert = new SupplierRiskAlert(
                    supplier.getId(),
                    "HIGH",
                    "Severe delay detected for PO " + po.getPoNumber()
            );
            supplierRiskAlertService.createAlert(alert);
        }

        return saved;
    }

    @Override
    public List<DelayScoreRecord> getScoresBySupplier(Long supplierId) {
        return delayScoreRecordRepository.findBySupplierId(supplierId);
    }

    @Override
    public List<DelayScoreRecord> getAllScores() {
        return delayScoreRecordRepository.findAll();
    }
}
