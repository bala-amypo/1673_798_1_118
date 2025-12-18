package com.example.demo.service.impl;

import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.DelayScoreRecord;
import com.example.demo.model.DelaySeverity;
import com.example.demo.model.DeliveryRecord;
import com.example.demo.model.PurchaseOrderRecord;
import com.example.demo.model.SupplierProfile;
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

    // constructor order exactly as required
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

    // tests call this on the implementation; keep Long and provide a long overload
    @Override
    public DelayScoreRecord computeDelayScore(Long poId) {
        if (poId == null) {
            throw new BadRequestException("Invalid poId");
        }

        PurchaseOrderRecord po = purchaseOrderRecordRepository.findById(poId)
                .orElseThrow(() -> new BadRequestException("Invalid supplierId"));

        SupplierProfile supplier = supplierProfileRepository.findById(po.getSupplierId())
                .orElseThrow(() -> new BadRequestException("Invalid supplierId"));

        if (!Boolean.TRUE.equals(supplier.getActive())) {
            throw new BadRequestException("must be active");
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

        DelaySeverity severity;
        double score;

        if (delayDays == 0) {
            severity = DelaySeverity.ON_TIME;
            score = 0.0;
        } else if (delayDays <= 3) {
            severity = DelaySeverity.MINOR;
            score = 1.0;
        } else if (delayDays <= 7) {
            severity = DelaySeverity.MODERATE;
            score = 2.0;
        } else {
            severity = DelaySeverity.SEVERE;
            score = 3.0;
        }

        // one score per PO – if exists, overwrite
        DelayScoreRecord record = delayScoreRecordRepository.findByPoId(poId)
                .orElse(new DelayScoreRecord());
        record.setPoId(poId);
        record.setSupplierId(supplier.getId());
        record.setDelayDays(delayDays);
        record.setDelaySeverity(severity);
        record.setScore(score);

        DelayScoreRecord saved = delayScoreRecordRepository.save(record);

        // simple hook into alert service for severe delays
        if (severity == DelaySeverity.SEVERE) {
            supplierRiskAlertService.createHighAlertForDelay(saved);
        }

        return saved;
    }

    // overload for tests that pass primitive long
    public DelayScoreRecord computeDelayScore(long poId) {
        return computeDelayScore(Long.valueOf(poId));
    }

    @Override
    public List<DelayScoreRecord> getScoresBySupplier(Long supplierId) {
        return delayScoreRecordRepository.findBySupplierId(supplierId);
    }

    @Override
    public DelayScoreRecord getScoreById(Long id) {
        return delayScoreRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Score not found"));
    }

    @Override
    public List<DelayScoreRecord> getAllScores() {
        return delayScoreRecordRepository.findAll();
    }
}
