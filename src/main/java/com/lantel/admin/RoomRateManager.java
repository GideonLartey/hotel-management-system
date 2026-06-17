package com.lantel.admin;

import java.util.HashMap;
import java.util.Map;

import com.lantel.room.RoomType;
import com.lantel.room.SeasonType;

/**
 * Manages room rates for different room types and seasons
 */
public class RoomRateManager {
    private final Map<String, Double> rateMap; // "SINGLE_REGULAR", "SINGLE_PEAK", etc.

    public RoomRateManager() {
        this.rateMap = new HashMap<>();
        initializeDefaultRates();
    }

    private void initializeDefaultRates() {
        // Standard Room Rates
        rateMap.put("STANDARD_HIGH_DEMAND", 100.0);
        rateMap.put("STANDARD_SLOW_BUSINESS_DAYS", 65.0);
        rateMap.put("STANDARD_SPECIAL", 80.0);

        // Deluxe Room Rates
        rateMap.put("DELUXE_HIGH_DEMAND", 200.0);
        rateMap.put("DELUXE_SLOW_BUSINESS_DAYS", 120.0);
        rateMap.put("DELUXE_SPECIAL", 150.0);

        // Executive Suite Rates
        rateMap.put("EXECUTIVE_SUITE_HIGH_DEMAND", 400.0);
        rateMap.put("EXECUTIVE_SUITE_SLOW_BUSINESS_DAYS", 240.0);
        rateMap.put("EXECUTIVE_SUITE_SPECIAL", 300.0);

        // VIP Suite Rates
        rateMap.put("VIP_SUITE_HIGH_DEMAND", 650.0);
        rateMap.put("VIP_SUITE_SLOW_BUSINESS_DAYS", 400.0);
        rateMap.put("VIP_SUITE_SPECIAL", 500.0);

        // VVIP Penthouse Suite Rates
        rateMap.put("VVIP_PENTHOUSE_SUITE_HIGH_DEMAND", 1300.0);
        rateMap.put("VVIP_PENTHOUSE_SUITE_SLOW_BUSINESS_DAYS", 800.0);
        rateMap.put("VVIP_PENTHOUSE_SUITE_SPECIAL", 1000.0);
    }

    public double getRate(RoomType roomType, SeasonType seasonType) {
        String key = roomType + "_" + (seasonType == null ? "REGULAR" : seasonType);
        return rateMap.getOrDefault(key, 50.0);
    }

    public void updateRate(RoomType roomType, SeasonType seasonType, double newRate) {
        if (newRate <= 0) {
            System.out.println("Error: Rate must be positive.");
            return;
        }
        String key = roomType + "_" + (seasonType == null ? "REGULAR" : seasonType);
        rateMap.put(key, newRate);
        System.out.println("Rate updated for " + key + ": GH₵ " + newRate);
    }

    public void displayAllRates() {
        System.out.println("\n========== ROOM RATES ==========");
        System.out.printf("%-25s | %-15s%n", "Room Type", "Rate (GH₵)");
        System.out.println("================================");
        for (String key : rateMap.keySet()) {
            System.out.printf("%-25s | GH₵ %.2f%n", key, rateMap.get(key));
        }
    }

    public Map<String, Double> getAllRates() {
        return new HashMap<>(rateMap);
    }
}
