package com.example.demo.service.impl;

import com.example.demo.exception.BadRequestException;
import com.example.demo.model.DeliveryRecord;
import com.example.demo.repository.DeliveryRecordRepository;
import com.example.demo.repository.PurchaseOrderRecordRepository;
import com.example.demo.service.DeliveryRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DeliveryRecordServiceImpl implements DeliveryRecordService {
    
    @Autowired
    private DeliveryRecordRepository repository;
    
    @Autowired
    private PurchaseOrderRecordRepository poRepository;

    @Override
    public DeliveryRecord recordDelivery(DeliveryRecord delivery) {
        if (poRepository.findById(delivery.getPoId()).isEmpty()) {
            throw new BadRequestException("Invalid PO id: " + delivery.getPoId());
        }
        if (delivery.getDeliveredQuantity() < 0) {
            throw new BadRequestException("Delivered quantity must be >= 0");
        }
        return repository.save(delivery);
    }

    @Override
    public List<DeliveryRecord> getDeliveriesByPO(Long poId) {
        return repository.findByPoId(poId);
    }

    @Override
    public List<DeliveryRecord> getAllDeliveries() {
        return repository.findAll();
    }
}