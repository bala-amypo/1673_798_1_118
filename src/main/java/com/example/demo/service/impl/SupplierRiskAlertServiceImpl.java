package com.example.demo.service.impl;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.SupplierRiskAlert;
import com.example.demo.repository.SupplierRiskAlertRepository;
import com.example.demo.service.SupplierRiskAlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SupplierRiskAlertServiceImpl implements SupplierRiskAlertService {
    
    @Autowired
    private SupplierRiskAlertRepository repository;

    @Override
    public SupplierRiskAlert createAlert(SupplierRiskAlert alert) {
        if (alert.getResolved() == null) {
            alert.setResolved(false);
        }
        return repository.save(alert);
    }

    @Override
    public List<SupplierRiskAlert> getAlertsBySupplier(Long supplierId) {
        return repository.findBySupplierId(supplierId);
    }

    @Override
    public SupplierRiskAlert resolveAlert(Long alertId) {
        SupplierRiskAlert alert = repository.findById(alertId)
                .orElseThrow(() -> new ResourceNotFoundException("Alert not found"));
        alert.setResolved(true);
        return repository.save(alert);
    }

    @Override
    public List<SupplierRiskAlert> getAllAlerts() {
        return repository.findAll();
    }
}