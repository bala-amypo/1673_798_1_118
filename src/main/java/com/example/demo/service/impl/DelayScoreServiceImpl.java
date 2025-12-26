package com.example.demo.service.impl;

import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.service.DelayScoreService;
import com.example.demo.service.SupplierRiskAlertService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Service
public class DelayScoreServiceImpl implements DelayScoreService {

    private final DelayScoreRecordRepository delayScoreRecordRepository;
    private final PurchaseOrderRecordRepository poRepository;
    private final DeliveryRecordRepository deliveryRepository;
    private final SupplierProfileRepository supplierProfileRepository;
    private final SupplierRiskAlertService riskAlertService;

    public DelayScoreServiceImpl(DelayScoreRecordRepository delayScoreRecordRepository,
                                 PurchaseOrderRecordRepository poRepository,
                                 DeliveryRecordRepository deliveryRepository,
                                 SupplierProfileRepository supplierProfileRepository,
                                 SupplierRiskAlertService riskAlertService) {
        this.delayScoreRecordRepository = delayScoreRecordRepository;
        this.poRepository = poRepository;
        this.deliveryRepository = deliveryRepository;
        this.supplierProfileRepository = supplierProfileRepository;
        this.riskAlertService = riskAlertService;
    }

    @Override
    public DelayScoreRecord computeDelayScore(Long poId) {
        PurchaseOrderRecord po = poRepository.findById(poId)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase order not found"));

        SupplierProfile supplier = supplierProfileRepository.findById(po.getSupplierId())
                .orElseThrow(() -> new BadRequestException("Invalid supplierId"));

        if (!Boolean.TRUE.equals(supplier.getActive())) {
            throw new BadRequestException("Inactive supplier");
        }

        List<DeliveryRecord> deliveries = deliveryRepository.findByPoId(poId);
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

        DelayScoreRecord record =
                new DelayScoreRecord(supplier.getId(), poId, delayDays, severity, score);

        DelayScoreRecord saved = delayScoreRecordRepository.save(record);

        if ("SEVERE".equals(severity)) {
            SupplierRiskAlert alert = new SupplierRiskAlert(
                    supplier.getId(),
                    "HIGH",
                    "Severe delay detected for PO " + po.getPoNumber()
            );
            riskAlertService.createAlert(alert);
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
