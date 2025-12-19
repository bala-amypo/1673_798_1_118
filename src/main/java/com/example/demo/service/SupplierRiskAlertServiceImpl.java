package com.example.demo.service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.DelayScoreRecord;
import com.example.demo.model.SupplierRiskAlert;
import com.example.demo.repository.SupplierRiskAlertRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupplierRiskAlertServiceImpl implements SupplierRiskAlertService {

    private final SupplierRiskAlertRepository alertRepository;

    public SupplierRiskAlertServiceImpl(SupplierRiskAlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    @Override
    public SupplierRiskAlert createAlert(SupplierRiskAlert alert) {
        return alertRepository.save(alert);
    }

    @Override
    public SupplierRiskAlert resolveAlert(Long id) {
        SupplierRiskAlert alert = getAlertById(id);
        alert.setResolved(true);
        return alertRepository.save(alert);
    }

    @Override
    public List<SupplierRiskAlert> getAlertsBySupplier(Long supplierId) {
        return alertRepository.findBySupplierId(supplierId);
    }

    @Override
    public SupplierRiskAlert getAlertById(Long id) {
        return alertRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alert not found"));
    }

    @Override
    public List<SupplierRiskAlert> getAllAlerts() {
        return alertRepository.findAll();
    }

    @Override
    public void createHighAlertForDelay(DelayScoreRecord scoreRecord) {
        SupplierRiskAlert alert = new SupplierRiskAlert();
        alert.setSupplierId(scoreRecord.getSupplierId());
        alert.setAlertLevel("HIGH");
        alert.setMessage("Severe delay detected for PO " + scoreRecord.getPoId());
        alertRepository.save(alert);
    }
}
