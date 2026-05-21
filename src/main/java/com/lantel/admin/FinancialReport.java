package com.lantel.admin;

import java.util.List;

import com.lantel.booking.Booking;

/**
 * Generates financial reports for the hotel
 */
public class FinancialReport {
    
    private final List<Booking> bookings;

    public FinancialReport(List<Booking> bookings) {
        this.bookings = bookings;
    }

    public double calculateTotalRevenue() {
        double total = 0;
        for (Booking booking : bookings) {
            if (booking.hasCheckedOut()) {
                total += booking.getRoomCost();
            }
        }
        return total;
    }

    public double calculateAverageRoomRate() {
        if (bookings.isEmpty()) return 0;
        return calculateTotalRevenue() / bookings.size();
    }

    public int getOccupiedRoomsCount() {
        int count = 0;
        for (Booking booking : bookings) {
            if (!booking.hasCheckedOut()) {
                count++;
            }
        }
        return count;
    }

    public double getOccupancyRate(int totalRooms) {
        if (totalRooms == 0) return 0;
        return (getOccupiedRoomsCount() / (double) totalRooms) * 100;
    }
    // Display financial summary report
    public void displayFinancialSummary(int totalRooms) {
        System.out.println("\n========== FINANCIAL REPORT ==========");
        System.out.printf("Total Revenue: GH₵ %.2f%n", calculateTotalRevenue());
        System.out.printf("Average Room Rate: GH₵ %.2f%n", calculateAverageRoomRate());
        System.out.printf("Total Bookings: %d%n", bookings.size());
        System.out.printf("Occupied Rooms: %d%n", getOccupiedRoomsCount());
        System.out.printf("Occupancy Rate: %.2f%%%n", getOccupancyRate(totalRooms));
        System.out.printf("Checked Out: %d%n", (int) bookings.stream().filter(Booking::hasCheckedOut).count());
        System.out.println("=====================================");
    }
}
