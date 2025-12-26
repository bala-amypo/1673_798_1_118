@Override
public PurchaseOrderRecord createPurchaseOrder(PurchaseOrderRecord po) {
    SupplierProfile supplier = supplierRepo.findById(po.getSupplierId())
            .orElseThrow(() -> new BadRequestException("Invalid supplierId"));

    // Crucial: Test testCreatePurchaseOrder_inactiveSupplier looks for this string
    if (supplier.getActive() == null || !supplier.getActive()) {
        throw new BadRequestException("must be active"); 
    }
    return poRepo.save(po);
}