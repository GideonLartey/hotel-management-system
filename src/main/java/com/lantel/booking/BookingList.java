package com.lantel.booking;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.lantel.guest.GuestBook;

public class BookingList implements Serializable {
    private static final long serialVersionUID = 1L;

    private final List<Booking> bookingList;
    private final double revenueAmount;
    private final double taxesOwed;

    public BookingList(GuestBook guests, double revenueAmount, double taxesOwed) {
        this.revenueAmount = revenueAmount;
        this.bookingList = new ArrayList<>();
        this.taxesOwed = taxesOwed;
    }

    // constructor for int taxesOwed
    public BookingList(GuestBook guests, double revenueAmount, int taxesOwed) {
        this(guests, revenueAmount, (double) taxesOwed);
    }

    public void addBooking(Booking booking) {
        bookingList.add(booking);
    }

    public void removeBooking(int index) {
        if (index >= 0 && index < bookingList.size()) {
            bookingList.remove(index);
        }
    }

    public void displayBookings (List<Booking> bookingList) {
       System.out.println("===== Booking List =====");
       for (Booking booking : bookingList) {
        System.out.println(booking);
       }
    }

    public void run() {
    }

    public List<Booking> getBookingList() {
        return bookingList;
    }

    public double getRevenueAmount() {
        return revenueAmount;
    }

    public double getTaxesOwed() {
        return taxesOwed;
    }

}
