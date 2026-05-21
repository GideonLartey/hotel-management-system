package com.lantel.guest;

import java.io.Serializable;

public class Guest implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String guestID;
    private final String guestName;
    private final int guestAge;
    private final long guestPhoneNumber;

    public Guest(String guestID, String guestName, int guestAge, long guestPhoneNumber) {
        if (guestID == null || guestID.trim().isEmpty()) {
            throw new IllegalArgumentException("Guest ID cannot be empty.");
        }
        if (guestName == null || guestName.trim().isEmpty()) {
            throw new IllegalArgumentException("Guest name cannot be empty.");
        }
        if (guestAge < 18) {
            throw new IllegalArgumentException("Guest must be at least 18 years old.");
        }
        this.guestID = guestID;
        this.guestName = guestName;
        this.guestAge = guestAge;
        this.guestPhoneNumber = guestPhoneNumber;
    }

    public String getGuestID() { return guestID; }
    public String getGuestName() { return guestName; }
    public int getGuestAge() { return guestAge; }
    public long getGuestPhoneNumber() { return guestPhoneNumber; }

    @Override
    public String toString() {
        return "Guest{" +
                "ID='" + guestID + '\'' +
                ", name='" + guestName + '\'' +
                ", age=" + guestAge +
                ", phone=" + guestPhoneNumber +
                '}';
    }
}
