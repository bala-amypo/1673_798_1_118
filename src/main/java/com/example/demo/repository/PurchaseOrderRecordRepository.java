package com.example.demo.repository;

import com.example.demo.model.PurchaseOrderRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PurchaseOrderRecordRepository extends JpaRepository<PurchaseOrderRecord, Long> {

    List<PurchaseOrderRecord> findBySupplierId(Long supplierId);
}
