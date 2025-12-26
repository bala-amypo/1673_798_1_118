@Service
public class SupplierProfileServiceImpl implements SupplierProfileService {
    @Override
    public SupplierProfile createSupplier(SupplierProfile supplier) {
        return supplierProfileRepository.save(supplier); // Ensure result is returned for IoC tests [cite: 231, 412]
    }

    @Override
    public SupplierProfile getSupplierById(Long id) {
        return supplierProfileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found")); // Exact message required [cite: 222]
    }

    @Override
    public SupplierProfile updateSupplierStatus(Long id, boolean active) {
        SupplierProfile supplier = getSupplierById(id);
        supplier.setActive(active);
        return supplierProfileRepository.save(supplier); // Fixes testControllerToggleStatus [cite: 251]
    }
}