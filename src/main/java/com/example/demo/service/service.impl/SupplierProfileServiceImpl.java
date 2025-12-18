package com.example.demo.service.impl;

import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.SupplierProfile;
import com.example.demo.repository.SupplierProfileRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupplierProfileServiceImpl implements SupplierProfileService {

    private final SupplierProfileRepository supplierProfileRepository;

    public SupplierProfileServiceImpl(SupplierProfileRepository supplierProfileRepository) {
        this.supplierProfileRepository = supplierProfileRepository;
    }

    @Override
    public SupplierProfile createSupplier(SupplierProfile supplier) {
        supplierProfileRepository.findBySupplierCode(supplier.getSupplierCode())
                .ifPresent(s -> { throw new BadRequestException("supplierCode must be unique"); });
        return supplierProfileRepository.save(supplier);
    }

    @Override
    public SupplierProfile getSupplierById(Long id) {
        return supplierProfileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found"));
    }

    @Override
    public SupplierProfile getBySupplierCode(String supplierCode) {
        return supplierProfileRepository.findBySupplierCode(supplierCode)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found"));
    }

    @Override
    public List<SupplierProfile> getAllSuppliers() {
        return supplierProfileRepository.findAll();
    }

    @Override
    public SupplierProfile updateSupplierStatus(Long id, boolean active) {
        SupplierProfile supplier = getSupplierById(id);
        supplier.setActive(active);
        return supplierProfileRepository.save(supplier);
    }
}
