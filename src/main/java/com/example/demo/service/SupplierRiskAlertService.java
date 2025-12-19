package com.example.demo.service;

import com.example.demo.model.SupplierRiskAlert;
import java.util.List;

public interface SupplierRiskAlertService {
    SupplierRiskAlert createAlert(SupplierRiskAlert alert);
    SupplierRiskAlert resolveAlert(Long id);
    List<SupplierRiskAlert> getAlertsBySupplier(Long supplierId);
    List<SupplierRiskAlert> getAllAlerts();
}