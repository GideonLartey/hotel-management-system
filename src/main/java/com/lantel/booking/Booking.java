package com.lantel.booking;
import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.lantel.room.GuestRoomStatus;
import com.lantel.room.GuestType;
import com.lantel.room.RoomType;
import com.lantel.room.SeasonType;

public class Booking implements Serializable {
    private static final long serialVersionUID = 1L;

    static final LocalTime CHECK_IN_TIME = LocalTime.of(14, 0);
    static final LocalTime CHECK_OUT_TIME = LocalTime.of(11, 0);

    private final String guestID;
    private final int guestAge;
    private final long guestPhoneNumber;
    private final String guestName;
    
    private LocalTime checkInTime;
    private LocalDate checkInDate;

    private LocalTime checkOutTime;
    private LocalDate checkOutDate;

    private double roomCost;
    private int roomID;
    private int daysRemaining;
    private boolean hasCheckedOut;

    
    private final GuestType guestType;
    private final SeasonType seasonType;
    private final RoomType roomType;
    private final GuestRoomStatus roomStatus;


    // constructor used in tests (without guestType/seasonType/roomStatus)
    public Booking(String guestID, int guestAge, long guestPhoneNumber, String guestName, LocalDate checkInDate,
        LocalDate checkOutDate, LocalTime checkInTime, LocalTime checkOutTime, double roomCost, RoomType roomType,
        int roomID, int daysRemaining) {
        this(guestID, guestAge, guestPhoneNumber, guestName, checkInDate, checkOutDate, checkInTime, checkOutTime,
            roomCost, roomID, daysRemaining, null, null, roomType, GuestRoomStatus.AVAILABLE);
    }

    public Booking(String guestID, int guestAge, long guestPhoneNumber, String guestName, LocalDate checkInDate, 
        LocalDate checkOutDate, LocalTime checkInTime, LocalTime checkOutTime, double roomCost, int roomID, 
        int daysRemaining, GuestType guestType, SeasonType seasonType, RoomType roomType, GuestRoomStatus roomStatus) {

        this.guestID = guestID;
        if ( guestAge < 18 ) {
            throw new IllegalArgumentException("Every guest must be at least 18 years to make room reservations");
        }
        this.guestAge = guestAge;
        

        if ((guestName == null || guestName.trim().length() == 0) && guestPhoneNumber == 0) {
                String message = " Your name and phone number is needed";
            throw new IllegalArgumentException(message);
        }

       
        this.guestPhoneNumber = guestPhoneNumber;
        this.guestName = guestName;
        
        LocalTime actualCheckInTime = (checkInTime != null) ? checkInTime : CHECK_IN_TIME;
        LocalTime actualCheckOutTime = (checkOutTime != null) ? checkOutTime : CHECK_OUT_TIME;
        LocalDateTime fullCheckIn = LocalDateTime.of(checkInDate, actualCheckInTime);
        LocalDateTime fullCheckOut = LocalDateTime.of(checkOutDate, actualCheckOutTime);

        if (fullCheckOut.isBefore(fullCheckIn)) {
            throw new IllegalArgumentException("Sorry! you cannot perform this operation. Please select/input the check in date first before your check out date");
        }

        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.checkInTime = actualCheckInTime;              
        this.checkOutTime = actualCheckOutTime;

        this.roomCost = roomCost;
        this.roomType = roomType;
        this.roomID = roomID;
        this.daysRemaining = daysRemaining;
        this.hasCheckedOut = false;
        this.guestType = (guestType == null) ? GuestType.REGULAR_GUEST : guestType;
        this.seasonType = seasonType;
        this.roomStatus = roomStatus;
      
    }

    public int decrementDays() {
        return daysRemaining--;
    }

    public boolean hasExpired() {
        return (daysRemaining == 0);
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public Duration getBookingDuration() {
        return Duration.between(checkInDate.atTime (checkInTime), checkOutDate.atTime (checkOutTime));
    }

    public long getNightsBooked() {
        return java.time.temporal.ChronoUnit.DAYS.between(checkInDate, checkOutDate);
    }

    public String getGuestID() {
        return guestID;
    }

    public String getGuestName() {
        return guestName;
    }

    public int getGuestAge() {
        return guestAge;
    }

    public double getRoomCost() {
        return roomCost;
    }

    public int getRoomID() {
        return roomID;
    }

    public com.lantel.room.GuestType getGuestType() {
        return guestType;
    }

    public com.lantel.room.SeasonType getSeasonType() {
        return seasonType;
    }

    public com.lantel.room.RoomType getRoomType() {
        return roomType;
    }

    public com.lantel.room.GuestRoomStatus getGuestRoomStatus() {
        return roomStatus;
    }

    public boolean isCheckedOut() {
        return hasCheckedOut;
    }

    public boolean hasCheckedOut() {
        return hasCheckedOut;
    }

    public void setCheckedOut(boolean checkedOut) {
        this.hasCheckedOut = checkedOut;
    }

    @Override
    public int hashCode() {
        return (guestName + guestPhoneNumber + guestID).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (!(obj instanceof Booking)) return false;
        Booking other = (Booking) obj;
        return (guestName.equals(other.guestName)) && (guestID.equals(other.guestID)) && (guestPhoneNumber == other.guestPhoneNumber);
    }

    @Override
    public String toString() {
        return """
            
            ----------BOOKING MANAGER----------
            Guest Name: %s
            Guest ID: %s
            Guest Age: %d
            Guest Phone Number: %d
            Check in Date: %s
            Check out Date: %s
            Calculated Cost: %.2f
            Room Service Type: %s
            Room Number: %d
            Length of Stay: %d
            """.formatted(guestName, guestID, guestAge, guestPhoneNumber, checkInDate, checkOutDate, roomCost, roomType, roomID, daysRemaining);
    }
}
