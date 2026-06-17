package com.lantel.booking;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.lantel.room.GuestRoom;
import com.lantel.room.GuestRoomStatus;
import com.lantel.room.GuestType;
import com.lantel.room.RoomType;
import com.lantel.room.SeasonType;


public class BookingManager {

    private static final String BOOKING_MANAGER_PREFIX = "BMP-";

    BookingList bookingList;
    private final String receptionistID;
    private String bookingNumber;
    private final double discount;
    private final double costPerNight;
    private final double costWithExtraCatering;
    private final RoomType roomType;

    private double amountToPay;
    private double amountPaid;

    private boolean isCheckedIn;
    private boolean isPaymentCompleted;
    private boolean isVerifiedID;
    
           
    public BookingManager(BookingList bookingList, String receptionistID, String bookingNumber, RoomType roomType,
        double discount, double costWithExtraCatering, double amountToPay, double amountPaid) {
        
        this.bookingList = bookingList;
        this.receptionistID = receptionistID;      
        this.bookingNumber = bookingNumber;
        this.roomType = roomType;
        this.discount = discount;
        this.costPerNight = (roomType != null) ? roomType.getCostPerNight() : 0.0;
        this.costWithExtraCatering = costWithExtraCatering;
           
        this.amountToPay = amountToPay;
        this.amountPaid = amountPaid;
    }

    // Legacy constructor (backwards compatibility)
    public BookingManager(BookingList bookingList, String receptionistID, String bookingNumber, double discount, 
        double costPerNight, double costWithExtraCatering, double amountToPay, double amountPaid) {
        
        this.bookingList = bookingList;
        this.receptionistID = receptionistID;      
        this.bookingNumber = bookingNumber;
        this.roomType = null;
        this.discount = discount;
        this.costPerNight = costPerNight;
        this.costWithExtraCatering = costWithExtraCatering;
           
        this.amountToPay = amountToPay;
        this.amountPaid = amountPaid;
    }

    public static List<GuestRoom> findAvailableRooms(List<GuestRoom> allRooms, List<Booking> bookingList, RoomType roomType, GuestRoomStatus roomStatus,
         LocalDate CheckInDate2, LocalDate CheckOutDate2) {
        if (allRooms == null) return Collections.emptyList();

        GuestRoomStatus desiredStatus = (roomStatus == null) ? GuestRoomStatus.AVAILABLE : roomStatus;

        return allRooms.stream()
            .filter(room -> room.getRoomStatus() == desiredStatus)
            // Optional: filter by roomType if specified
            .filter(room -> roomType == null || room.getRoomType() == roomType)
            .filter(room -> bookingList == null || bookingList.stream().noneMatch(booking ->
                    booking.getRoomID() == room.getRoomID()
                    && conflictingDates(
                        booking.getCheckInDate(), 
                        booking.getCheckOutDate(),
                        CheckInDate2, 
                        CheckOutDate2 
                    )
            ))
            .collect(Collectors.toList());
    }

  
    public BookingList getBookingList() {
        return bookingList;
    }

    public String getReceptionistID() {
        return receptionistID;
    }

    public String getBookingNumber() {
        return bookingNumber;
    }

    public double getDiscount() {
        return discount;    
    }

    public double getAmountToPay() {
        return amountToPay;
    }

    public double getCostWithExtraCatering() {
        return costWithExtraCatering;
    }

    public double getCostPerNight() {
        return costPerNight;
    }
 
    public void verifyIdentification(Booking booking) {
        if (booking.getGuestAge() >= 18) {
            this.isVerifiedID = true;
        } else {
            throw new IllegalArgumentException("The hotel's regulations cannot allow people below 18 years of age to make room reservations, unless accompanied/in care of by a parent or guardian.");
        }     
    }

    private double computeAmount(LocalDate checkInDate, LocalDate checkOutDate, GuestType guestType, SeasonType seasonType) {
        long nights = java.time.temporal.ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        double effectiveCostPerNight = (roomType != null) ? roomType.getCostPerNight() : costPerNight;
        double flatAmount = (effectiveCostPerNight * nights) + costWithExtraCatering;
        double appliedDiscount = 0.0;
        if (guestType == GuestType.VVIP_GUEST && (seasonType == SeasonType.SPECIAL || seasonType == SeasonType.SLOW_BUSINESS_DAYS)) {
            appliedDiscount = discount;
        }
        return flatAmount * (1 - appliedDiscount);
    }

    public void calculateAmountToPay(LocalDate checkInDate, LocalDate checkOutDate, GuestType guestType, SeasonType seasonType) {
        this.amountToPay = computeAmount(checkInDate, checkOutDate, guestType, seasonType);
    }

    // generate booking number after payment is made
    public void makePayments(double payment) {
        double targetAmount = (this.amountToPay > 0.0) ? this.amountToPay : payment;
        this.amountPaid += payment;
       
        if (amountPaid >= targetAmount && bookingNumber == null) {
            bookingNumber = generateBookingNumber();
            this.isPaymentCompleted = true;
            System.out.println("Payment has been completed successfully. The booking number for this reservation is: " + bookingNumber);
        } else {
            this.isPaymentCompleted = false;
            this.isCheckedIn = false;
            System.err.println("Payment is incomplete. Please check again and allow guest initiate payment again.");
        }
    }

   // check remaining balance
    public void makePayments() {
        double remainingBalance = Math.max(0.0, this.amountToPay - this.amountPaid);
        if (remainingBalance > 0.0) {
            makePayments(remainingBalance);
        } else {
            // nothing to pay 
            if (this.amountPaid >= this.amountToPay) {
                this.isPaymentCompleted = true;
            }
        }
    }

    private String generateBookingNumber() {
        return BOOKING_MANAGER_PREFIX + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }

    private static boolean conflictingDates(LocalDate checkInDate1, LocalDate checkOutDate1, LocalDate checkInDate2,
         LocalDate checkOutDate2) {
        if (checkInDate1 == null || checkOutDate1 == null || checkInDate2 == null || checkOutDate2 == null) return false;
        return checkInDate1.isBefore(checkOutDate2) && checkInDate2.isBefore(checkOutDate1);
    }

    public void checkInConfirmation() {
        this.isCheckedIn = true;
    }

    public boolean isAddedToBookingList() {
        return isVerifiedID && isCheckedIn && isPaymentCompleted;
    }
 
    //Create a new reservation using a precomputed amountToPay
    public void createANewReservation(String guestName, int guestAge, long guestPhoneNumber, String guestID,
        LocalDate checkInDate, LocalDate checkOutDate,
        double amountToPay, RoomType roomType, String receptionistRef, GuestType guestType, SeasonType seasonType) {
        
        int daysRemaining = (int) java.time.temporal.ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        createANewReservation(guestName, guestAge, guestPhoneNumber, guestID, checkInDate, checkOutDate,
            amountToPay, 0, daysRemaining, guestType, seasonType, roomType, GuestRoomStatus.AVAILABLE);
    }

    public void createANewReservation(String guestName, int guestAge, long guestPhoneNumber, String guestID, 
        LocalDate checkInDate, LocalDate checkOutDate, 
        double amountToPay, int roomID, int daysRemaining, GuestType guestType, SeasonType seasonType, RoomType roomType, 
        GuestRoomStatus roomStatus) {
  
        daysRemaining = (int) java.time.temporal.ChronoUnit.DAYS.between(checkInDate, checkOutDate);

        // roomID = 0 (not yet assigned when creating a reservation)
        Booking booking = new Booking(
            guestID,
            guestAge,
            guestPhoneNumber,
            guestName,
            checkInDate,
            checkOutDate,
            java.time.LocalTime.of(14, 0),
            java.time.LocalTime.of(11, 0),
            amountToPay,
            /*roomID*/ 0,
            daysRemaining,
            guestType,
            seasonType,
            roomType,
            roomStatus
        );
        bookingList.addBooking(booking);       
    }

    // information on the booking which is relevant to the hotel's management and operation 
    public String toCSVString() {
        return receptionistID + "," +
               bookingNumber + "," +
               amountPaid + "," +
               isCheckedIn + "," +
               isPaymentCompleted + "," +
               isVerifiedID;
    }
}
