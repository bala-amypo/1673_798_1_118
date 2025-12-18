package com.example.demo.service;

import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.DelayScoreRecord;
import com.example.demo.model.DeliveryRecord;
import com.example.demo.model.PurchaseOrderRecord;
import com.example.demo.model.SupplierProfile;
import com.example.demo.repository.DelayScoreRecordRepository;
import com.example.demo.repository.DeliveryRecordRepository;
import com.example.demo.repository.PurchaseOrderRecordRepository;
import com.example.demo.repository.SupplierProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;

@Service
@Transactional
public class DelayScoreServiceImpl implements DelayScoreService {
    
    private final DelayScoreRecordRepository delayScoreRepository;
    private final PurchaseOrderRecordRepository poRepository;
    private final DeliveryRecordRepository deliveryRepository;
    private final SupplierProfileRepository supplierRepository;
    private final SupplierRiskAlertService riskAlertService;
    
    public DelayScoreServiceImpl(DelayScoreRecordRepository delayScoreRepository,
                                PurchaseOrderRecordRepository poRepository,
                                DeliveryRecordRepository deliveryRepository,
                                SupplierProfileRepository supplierRepository,
                                SupplierRiskAlertService riskAlertService) {
        this.delayScoreRepository = delayScoreRepository;
        this.poRepository = poRepository;
        this.deliveryRepository = deliveryRepository;
        this.supplierRepository = supplierRepository;
        this.riskAlertService = riskAlertService;
    }
    
    @Override
    public DelayScoreRecord computeDelayScore(Long poId) {
        // Fetch PO
        PurchaseOrderRecord po = poRepository.findById(poId)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase Order not found with id: " + poId));
        
        // Fetch deliveries
        List<DeliveryRecord> deliveries = deliveryRepository.findByPoId(poId);
        if (deliveries.isEmpty()) {
            throw new BadRequestException("No deliveries found for PO id: " + poId);
        }
        
        // Check supplier exists
        SupplierProfile supplier = supplierRepository.findById(po.getSupplierId())
                .orElseThrow(() -> new BadRequestException("Invalid supplierId: " + po.getSupplierId()));
        
        // Check supplier is active
        if (!supplier.getActive()) {
            throw new BadRequestException("Supplier must be active");
        }
        
        // Get the latest delivery date
        LocalDate actualDeliveryDate = deliveries.stream()
                .map(DeliveryRecord::getActualDeliveryDate)
                .max(Comparator.naturalOrder())
                .orElseThrow(() -> new BadRequestException("No valid delivery dates found"));
        
        // Calculate delay
        LocalDate promisedDate = po.getPromisedDeliveryDate();
        int delayDays = (int) ChronoUnit.DAYS.between(promisedDate, actualDeliveryDate);
        
        // Determine severity and score
        String severity;
        double score;
        
        if (delayDays <= 0) {
            severity = "ON_TIME";
            score = 0.0;
        } else if (delayDays <= 3) {
            severity = "MINOR";
            score = 1.0;
        } else if (delayDays <= 7) {
            severity = "MODERATE";
            score = 2.0;
        } else {
            severity = "SEVERE";
            score = 3.0;
        }
        
        // Create and save delay score record
        DelayScoreRecord delayScore = new DelayScoreRecord();
        delayScore.setSupplierId(po.getSupplierId());
        delayScore.setPoId(poId);
        delayScore.setDelayDays(delayDays);
        delayScore.setDelaySeverity(severity);
        delayScore.setScore(score);
        
        return delayScoreRepository.save(delayScore);
    }
    
    @Override
    public List<DelayScoreRecord> getScoresBySupplier(Long supplierId) {
        return delayScoreRepository.findBySupplierId(supplierId);
    }
    
    @Override
    public DelayScoreRecord getScoreById(Long id) {
        return delayScoreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delay Score not found with id: " + id));
    }
    
    @Override
    public List<DelayScoreRecord> getAllScores() {
        return delayScoreRepository.findAll();
    }
}