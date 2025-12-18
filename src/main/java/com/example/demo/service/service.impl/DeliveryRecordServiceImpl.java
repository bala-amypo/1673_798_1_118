package com.example.demo.service.impl;


import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.DeliveryRecord;
import com.example.demo.repository.DeliveryRecordRepository;
import com.example.demo.repository.PurchaseOrderRecordRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
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
        poRepository.findById(delivery.getPoId())
                .orElseThrow(() -> new BadRequestException("Invalid poId"));

        if (delivery.getDeliveredQuantity() == null || delivery.getDeliveredQuantity() < 0) {
            throw new BadRequestException("deliveredQuantity must be >= 0");
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
                .orElseThrow(() -> new ResourceNotFoundException("Delivery not found"));
    }

    @Override
    public List<DeliveryRecord> getAllDeliveries() {
        return deliveryRepository.findAll();
    }
}
