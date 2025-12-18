package com.example.demo.service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.SupplierRiskAlert;
import com.example.demo.repository.SupplierRiskAlertRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SupplierRiskAlertServiceImpl implements SupplierRiskAlertService {
    
    private final SupplierRiskAlertRepository alertRepository;
    
    public SupplierRiskAlertServiceImpl(SupplierRiskAlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }
    
    @Override
    public SupplierRiskAlert createAlert(SupplierRiskAlert alert) {
        if (alert.getResolved() == null) {
            alert.setResolved(false);
        }
        return alertRepository.save(alert);
    }
    
    @Override
    public SupplierRiskAlert resolveAlert(Long id) {
        SupplierRiskAlert alert = alertRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Risk Alert not found with id: " + id));
        alert.setResolved(true);
        return alertRepository.save(alert);
    }
    
    @Override
    public List<SupplierRiskAlert> getAlertsBySupplier(Long supplierId) {
        return alertRepository.findBySupplierId(supplierId);
    }
    
    @Override
    public List<SupplierRiskAlert> getAllAlerts() {
        return alertRepository.findAll();
    }
}