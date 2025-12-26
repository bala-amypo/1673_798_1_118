package com.example.demo.util;

import org.springframework.stereotype.Component;

@Component
public class HqlQueryHelper {
    
    public String buildSupplierQuery(String criteria) {
        return "SELECT s FROM SupplierProfile s WHERE " + criteria;
    }
    
    public String buildDelayScoreQuery(String criteria) {
        return "SELECT d FROM DelayScoreRecord d WHERE " + criteria;
    }
}