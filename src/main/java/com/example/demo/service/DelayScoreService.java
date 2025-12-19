package com.example.demo.service;

import com.example.demo.model.DelayScoreRecord;

import java.util.List;

public interface DelayScoreService {

    DelayScoreRecord computeScore(Long poId);

    List<DelayScoreRecord> getScoresBySupplier(Long supplierId);

    DelayScoreRecord getScoreById(Long id);

    List<DelayScoreRecord> getAllScores();
}
