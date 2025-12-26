@Override
public SupplierRiskAlert resolveAlert(Long alertId) {
    SupplierRiskAlert alert = riskAlertRepository.findById(alertId)
            .orElseThrow(() -> new ResourceNotFoundException("Alert not found"));
    alert.setResolved(true);
    return riskAlertRepository.save(alert);
}