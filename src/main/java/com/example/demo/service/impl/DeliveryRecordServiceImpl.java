@Service
public class DeliveryRecordServiceImpl implements DeliveryRecordService {
    @Override
    public DeliveryRecord recordDelivery(DeliveryRecord delivery) {
        poRepository.findById(delivery.getPoId())
                .orElseThrow(() -> new BadRequestException("Invalid PO id")); // Fixes testRecordDelivery_success [cite: 362, 1143]

        if (delivery.getDeliveredQuantity() == null || delivery.getDeliveredQuantity() < 0) {
            throw new BadRequestException("Delivered quantity must be >="); // Fixes testRecordDelivery_negativeQuantity 
        }
        return deliveryRepository.save(delivery);
    }
}