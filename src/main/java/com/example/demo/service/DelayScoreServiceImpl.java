package com.example.demo.service;


import com.example.demo.model.DelaySeverity;


import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Service
public class DelayScoreServiceImpl implements DelayScoreService {

    private final DelayScoreRecordRepository delayScoreRepository;
    private final PurchaseOrderRecordRepository poRepository;
    private final DeliveryRecordRepository deliveryRepository;
    private final SupplierProfileRepository supplierRepository;
    private final SupplierRiskAlertService supplierRiskAlertService;

    // constructor order important for tests
    public DelayScoreServiceImpl(DelayScoreRecordRepository delayScoreRepository,
                                 PurchaseOrderRecordRepository poRepository,
                                 DeliveryRecordRepository deliveryRepository,
                                 SupplierProfileRepository supplierRepository,
                                 SupplierRiskAlertService supplierRiskAlertService) {
        this.delayScoreRepository = delayScoreRepository;
        this.poRepository = poRepository;
        this.deliveryRepository = deliveryRepository;
        this.supplierRepository = supplierRepository;
        this.supplierRiskAlertService = supplierRiskAlertService;
    }

    @Override
    public DelayScoreRecord computeScore(Long poId) {
        PurchaseOrderRecord po = poRepository.findById(poId)
                .orElseThrow(() -> new BadRequestException("Invalid poId"));

        List<DeliveryRecord> deliveries = deliveryRepository.findByPoId(poId);
        if (deliveries.isEmpty()) {
            throw new BadRequestException("No deliveries");
        }

        DeliveryRecord latest = deliveries.stream()
                .max(Comparator.comparing(DeliveryRecord::getActualDeliveryDate))
                .orElseThrow();

        LocalDate promised = po.getPromisedDeliveryDate();
        LocalDate actual = latest.getActualDeliveryDate();
        int delayDays = (int) (actual.toEpochDay() - promised.toEpochDay());

        if (delayDays < 0) delayDays = 0;

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

        SupplierProfile supplier = supplierRepository.findById(po.getSupplierId())
                .orElseThrow(() -> new BadRequestException("Invalid supplierId"));

        DelayScoreRecord record = new DelayScoreRecord();
        record.setPoId(poId);
        record.setSupplierId(supplier.getId());
        record.setDelayDays(delayDays);
        record.setDelaySeverity(severity);
        record.setScore(score);

        DelayScoreRecord saved = delayScoreRepository.save(record);

        // simple rule: create HIGH alert for severe delays
        if (severity == DelaySeverity.SEVERE) {
            supplierRiskAlertService.createHighAlertForDelay(saved);
        }

        return saved;
    }

    @Override
    public List<DelayScoreRecord> getScoresBySupplier(Long supplierId) {
        return delayScoreRepository.findBySupplierId(supplierId);
    }

    @Override
    public DelayScoreRecord getScoreById(Long id) {
        return delayScoreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Score not found"));
    }

    @Override
    public List<DelayScoreRecord> getAllScores() {
        return delayScoreRepository.findAll();
    }
}
