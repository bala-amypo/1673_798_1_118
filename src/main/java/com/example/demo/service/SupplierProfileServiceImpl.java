package com.example.demo.service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.SupplierProfile;
import com.example.demo.repository.SupplierProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SupplierProfileServiceImpl implements SupplierProfileService {
    
    private final SupplierProfileRepository supplierRepository;
    
    public SupplierProfileServiceImpl(SupplierProfileRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }
    
    @Override
    public SupplierProfile createSupplier(SupplierProfile supplier) {
        return supplierRepository.save(supplier);
    }
    
    @Override
    public SupplierProfile getSupplierById(Long id) {
        return supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with id: " + id));
    }
    
    @Override
    public SupplierProfile getBySupplierCode(String supplierCode) {
        return supplierRepository.findBySupplierCode(supplierCode)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with code: " + supplierCode));
    }
    
    @Override
    public List<SupplierProfile> getAllSuppliers() {
        return supplierRepository.findAll();
    }
    
    @Override
    public SupplierProfile updateSupplierStatus(Long id, boolean active) {
        SupplierProfile supplier = getSupplierById(id);
        supplier.setActive(active);
        return supplierRepository.save(supplier);
    }
}