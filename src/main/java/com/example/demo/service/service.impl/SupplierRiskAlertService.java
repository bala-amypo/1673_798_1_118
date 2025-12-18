package com.example.demo.service.impl;

import com.example.demo.model.DelayScoreRecord;
import com.example.demo.model.SupplierRiskAlert;

import java.util.List;

public interface SupplierRiskAlertService {

    SupplierRiskAlert createAlert(SupplierRiskAlert alert);

    SupplierRiskAlert resolveAlert(Long id);

    List<SupplierRiskAlert> getAlertsBySupplier(Long supplierId);

    SupplierRiskAlert getAlertById(Long id);

    List<SupplierRiskAlert> getAllAlerts();

    void createHighAlertForDelay(DelayScoreRecord scoreRecord);
}
