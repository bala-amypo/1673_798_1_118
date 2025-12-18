package com.example.demo.service;

import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.DeliveryRecord;
import com.example.demo.repository.DeliveryRecordRepository;
import com.example.demo.repository.PurchaseOrderRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DeliveryRecordServiceImpl implements DeliveryRecordService {
    
    private final DeliveryRecordRepository deliveryRepository;
    private final PurchaseOrderRecordRepository poRepository;
    
    public DeliveryRecordServiceImpl(DeliveryRecordRepository deliveryRepository,
                                    PurchaseOrderRecordRepository poRepository) {
        this.deliveryRepository = deliveryRepository;
        this.poRepository = poRepository;
    }
    
    @Override
    public DeliveryRecord recordDelivery(DeliveryRecord delivery) {
        // Validate delivered quantity
        if (delivery.getDeliveredQuantity() == null || delivery.getDeliveredQuantity() < 0) {
            throw new BadRequestException("Delivered quantity must be non-negative");
        }
        
        // Validate PO exists
        if (!poRepository.existsById(delivery.getPoId())) {
            throw new ResourceNotFoundException("Purchase Order not found with id: " + delivery.getPoId());
        }
        
        return deliveryRepository.save(delivery);
    }
    
    @Override
    public List<DeliveryRecord> getDeliveriesByPO(Long poId) {
        return deliveryRepository.findByPoId(poId);
    }
    
    @Override
    public DeliveryRecord getDeliveryById(Long id) {
        return deliveryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery Record not found with id: " + id));
    }
    
    @Override
    public List<DeliveryRecord> getAllDeliveries() {
        return deliveryRepository.findAll();
    }
}