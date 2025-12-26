@Service
public class SupplierRiskAlertServiceImpl implements SupplierRiskAlertService {
    @Override
    public SupplierRiskAlert resolveAlert(Long alertId) {
        SupplierRiskAlert alert = riskAlertRepository.findById(alertId)
                .orElseThrow(() -> new ResourceNotFoundException("Alert not found")); // Fixes testResolveAlertChangesFlag
        alert.setResolved(true);
        return riskAlertRepository.save(alert);
    }
    
    @Override
    public SupplierRiskAlert createAlert(SupplierRiskAlert alert) {
        if (alert.getResolved() == null) alert.setResolved(false);
        return riskAlertRepository.save(alert); // Fixes NullPointer in testIoCBehaviorOnRiskAlertService [cite: 420]
    }
}