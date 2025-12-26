@Override
public DeliveryRecord recordDelivery(DeliveryRecord delivery) {
    poRepo.findById(delivery.getPoId())
            .orElseThrow(() -> new BadRequestException("Invalid PO id"));

    if (delivery.getDeliveredQuantity() == null || delivery.getDeliveredQuantity() < 0) {
        throw new BadRequestException("Delivered quantity must be >="); 
    }
    return deliveryRepo.save(delivery);
}