package com.example.demo.service.impl;

import com.example.demo.exception.BadRequestException;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.service.DelayScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class DelayScoreServiceImpl implements DelayScoreService {
    
    @Autowired
    private DelayScoreRecordRepository repository;
    
    @Autowired
    private PurchaseOrderRecordRepository poRepository;
    
    @Autowired
    private DeliveryRecordRepository deliveryRepository;
    
    @Autowired
    private SupplierProfileRepository supplierRepository;
    
    @Autowired
    private SupplierRiskAlertServiceImpl riskAlertService;

    public DelayScoreServiceImpl(DelayScoreRecordRepository repository,
                               PurchaseOrderRecordRepository poRepository,
                               DeliveryRecordRepository deliveryRepository,
                               SupplierProfileRepository supplierRepository,
                               SupplierRiskAlertServiceImpl riskAlertService) {
        this.repository = repository;
        this.poRepository = poRepository;
        this.deliveryRepository = deliveryRepository;
        this.supplierRepository = supplierRepository;
        this.riskAlertService = riskAlertService;
    }

    @Override
    public DelayScoreRecord computeDelayScore(Long poId) {
        PurchaseOrderRecord po = poRepository.findById(poId)
                .orElseThrow(() -> new BadRequestException("PO not found"));
        
        SupplierProfile supplier = supplierRepository.findById(po.getSupplierId())
                .orElseThrow(() -> new BadRequestException("Supplier not found"));
        
        if (!supplier.getActive()) {
            throw new BadRequestException("Inactive supplier");
        }
        
        List<DeliveryRecord> deliveries = deliveryRepository.findByPoId(poId);
        if (deliveries.isEmpty()) {
            throw new BadRequestException("No deliveries found for PO: " + poId);
        }
        
        // Use first delivery for calculation
        DeliveryRecord delivery = deliveries.get(0);
        long delayDays = ChronoUnit.DAYS.between(po.getPromisedDeliveryDate(), delivery.getActualDeliveryDate());
        
        DelayScoreRecord record = new DelayScoreRecord();
        record.setPoId(poId);
        record.setSupplierId(po.getSupplierId());
        record.setDelayDays((int) delayDays);
        
        if (delayDays <= 0) {
            record.setDelaySeverity("ON_TIME");
            record.setScore(100.0);
        } else if (delayDays <= 3) {
            record.setDelaySeverity("MINOR");
            record.setScore(80.0);
        } else if (delayDays <= 7) {
            record.setDelaySeverity("MODERATE");
            record.setScore(60.0);
        } else {
            record.setDelaySeverity("SEVERE");
            record.setScore(30.0);
        }
        
        return repository.save(record);
    }

    @Override
    public List<DelayScoreRecord> getScoresBySupplier(Long supplierId) {
        return repository.findBySupplierId(supplierId);
    }

    @Override
    public List<DelayScoreRecord> getAllScores() {
        return repository.findAll();
    }
}