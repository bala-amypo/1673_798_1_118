package com.example.demo.service;

import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.PurchaseOrderRecord;
import com.example.demo.model.SupplierProfile;
import com.example.demo.repository.PurchaseOrderRecordRepository;
import com.example.demo.repository.SupplierProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PurchaseOrderServiceImpl implements PurchaseOrderService {
    
    private final PurchaseOrderRecordRepository poRepository;
    private final SupplierProfileRepository supplierRepository;
    
    public PurchaseOrderServiceImpl(PurchaseOrderRecordRepository poRepository, 
                                   SupplierProfileRepository supplierRepository) {
        this.poRepository = poRepository;
        this.supplierRepository = supplierRepository;
    }
    
    @Override
    public PurchaseOrderRecord createPurchaseOrder(PurchaseOrderRecord po) {
        // Validate quantity
        if (po.getQuantity() == null || po.getQuantity() <= 0) {
            throw new BadRequestException("Quantity must be greater than 0");
        }
        
        // Validate supplier ID exists
        SupplierProfile supplier = supplierRepository.findById(po.getSupplierId())
                .orElseThrow(() -> new BadRequestException("Invalid supplierId: " + po.getSupplierId()));
        
        // Validate supplier is active
        if (!supplier.getActive()) {
            throw new BadRequestException("Supplier must be active to create purchase order");
        }
        
        return poRepository.save(po);
    }
    
    @Override
    public List<PurchaseOrderRecord> getPOsBySupplier(Long supplierId) {
        return poRepository.findBySupplierId(supplierId);
    }
    
    @Override
    public PurchaseOrderRecord getPOById(Long id) {
        return poRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase Order not found with id: " + id));
    }
    
    @Override
    public List<PurchaseOrderRecord> getAllPurchaseOrders() {
        return poRepository.findAll();
    }
}