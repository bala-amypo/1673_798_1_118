package com.example.demo.service.impl;

import com.example.demo.exception.BadRequestException;
import com.example.demo.model.DeliveryRecord;
import com.example.demo.repository.DeliveryRecordRepository;
import com.example.demo.repository.PurchaseOrderRecordRepository;
import com.example.demo.service.DeliveryRecordService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DeliveryRecordServiceImpl implements DeliveryRecordService {

    private final DeliveryRecordRepository deliveryRepo;
    private final PurchaseOrderRecordRepository poRepo;

    public DeliveryRecordServiceImpl(DeliveryRecordRepository deliveryRepo, PurchaseOrderRecordRepository poRepo) {
        this.deliveryRepo = deliveryRepo;
        this.poRepo = poRepo;
    }

    @Override
    public DeliveryRecord recordDelivery(DeliveryRecord delivery) {
        poRepo.findById(delivery.getPoId())
                .orElseThrow(() -> new BadRequestException("Invalid PO id"));

        if (delivery.getDeliveredQuantity() == null || delivery.getDeliveredQuantity() < 0) {
            throw new BadRequestException("Delivered quantity must be >=");
        }
        return deliveryRepo.save(delivery);
    }

    @Override
    public List<DeliveryRecord> getDeliveriesByPO(Long poId) {
        return deliveryRepo.findByPoId(poId);
    }

    @Override
    public List<DeliveryRecord> getAllDeliveries() {
        return deliveryRepo.findAll();
    }
}