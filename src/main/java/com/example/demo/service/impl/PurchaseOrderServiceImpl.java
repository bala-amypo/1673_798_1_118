package com.example.demo.service.impl;

import com.example.demo.exception.BadRequestException;
import com.example.demo.model.PurchaseOrderRecord;
import com.example.demo.model.SupplierProfile;
import com.example.demo.repository.PurchaseOrderRecordRepository;
import com.example.demo.repository.SupplierProfileRepository;
import com.example.demo.service.PurchaseOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PurchaseOrderServiceImpl implements PurchaseOrderService {
    
    @Autowired
    private PurchaseOrderRecordRepository repository;
    
    @Autowired
    private SupplierProfileRepository supplierRepository;

    @Override
    public PurchaseOrderRecord createPurchaseOrder(PurchaseOrderRecord po) {
        Optional<SupplierProfile> supplier = supplierRepository.findById(po.getSupplierId());
        if (supplier.isEmpty()) {
            throw new BadRequestException("Invalid supplierId: " + po.getSupplierId());
        }
        if (!supplier.get().getActive()) {
            throw new BadRequestException("Supplier must be active");
        }
        return repository.save(po);
    }

    @Override
    public List<PurchaseOrderRecord> getPOsBySupplier(Long supplierId) {
        return repository.findBySupplierId(supplierId);
    }

    @Override
    public Optional<PurchaseOrderRecord> getPOById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<PurchaseOrderRecord> getAllPurchaseOrders() {
        return repository.findAll();
    }
}