package com.lantel.admin;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Tracks housekeeping activities for rooms
 */
public class HousekeepingTracker {
    
    public static class CleaningRecord {
        private final int roomID;
        private final String housekeeperName;
        private final LocalDateTime cleaningDate;
        private String status; // CLEANED, NEEDS_CLEANING, OR IN_PROGRESS
        private final String notes;

        public CleaningRecord(int roomID, String housekeeperName, LocalDateTime cleaningDate, 
                            String status, String notes) {
            this.roomID = roomID;
            this.housekeeperName = housekeeperName;
            this.cleaningDate = cleaningDate;
            this.status = status;
            this.notes = notes;
        }

        // Getters
        public int getRoomID() { return roomID; }
        public String getHousekeeperName() { return housekeeperName; }
        public LocalDateTime getCleaningDate() { return cleaningDate; }
        public String getStatus() { return status; }
        public String getNotes() { return notes; }

        public void setStatus(String status) { this.status = status; }
    }

    private final List<CleaningRecord> cleaningLog;

    public HousekeepingTracker() {
        this.cleaningLog = new ArrayList<>();
    }

    public void recordCleaning(int roomID, String housekeeperName, String notes) {
        cleaningLog.add(new CleaningRecord(roomID, housekeeperName, LocalDateTime.now(), 
                                           "CLEANED", notes));
        System.out.println("Cleaning recorded for Room #" + roomID);
    }

    public void markAsNeedsCleaning(int roomID) {
        cleaningLog.add(new CleaningRecord(roomID, "N/A", LocalDateTime.now(), 
                                           "NEEDS_CLEANING", "Room marked for cleaning"));
        System.out.println("Room #" + roomID + " marked as needing cleaning");
    }
    // display cleaning log in a readable format
    public void displayCleaningLog() {
        if (cleaningLog.isEmpty()) {
            System.out.println("No cleaning records found.");
            return;
        }
        System.out.println("\n========== CLEANING LOG ==========");
        System.out.printf("%-8s | %-20s | %-20s | %-15s | %-30s%n",
            "Room", "Housekeeper", "Date/Time", "Status", "Notes");
        System.out.println("==================================");
        for (CleaningRecord record : cleaningLog) {
            System.out.printf("%-8d | %-20s | %-20s | %-15s | %-30s%n",
                record.roomID, record.housekeeperName, 
                record.cleaningDate.toString(), record.status, record.notes);
        }
    }

    // Get cleaning history for a specific room
    public List<CleaningRecord> getRoomCleaningHistory(int roomID) {
        List<CleaningRecord> history = new ArrayList<>();
        for (CleaningRecord record : cleaningLog) {
            if (record.roomID == roomID) {
                history.add(record);
            }
        }
        return history;
    }

    public List<CleaningRecord> getCleaningLog() {
        return new ArrayList<>(cleaningLog);
    }
}
