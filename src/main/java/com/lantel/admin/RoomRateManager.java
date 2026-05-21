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
        // Regular Rates
        rateMap.put("SINGLE_REGULAR", 50.0);
        rateMap.put("SINGLE_PEAK", 75.0);
        rateMap.put("DOUBLE_REGULAR", 80.0);
        rateMap.put("DOUBLE_PEAK", 120.0);
        rateMap.put("SUITE_REGULAR", 150.0);
        rateMap.put("SUITE_PEAK", 225.0);
        rateMap.put("DELUXE_REGULAR", 200.0);
        rateMap.put("DELUXE_PEAK", 300.0);
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
