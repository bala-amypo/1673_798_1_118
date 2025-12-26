package com.example.demo.service;

import com.example.demo.model.DelayScoreRecord;
import java.util.List;

public interface DelayScoreService {
    DelayScoreRecord computeDelayScore(Long poId);
    List<DelayScoreRecord> getScoresBySupplier(Long supplierId);
    List<DelayScoreRecord> getAllScores();
}