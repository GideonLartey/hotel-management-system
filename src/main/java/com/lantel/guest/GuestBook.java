package com.lantel.guest;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class GuestBook implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Map<String, Guest> guestMap = new HashMap<>();

    public void addGuest(Guest guest) {
        if (guest == null) throw new IllegalArgumentException("Guest cannot be null.");
        guestMap.put(guest.getGuestID(), guest);
    }

    public Guest getGuest(String guestID) {
        return guestMap.get(guestID);
    }

    public boolean removeGuest(String guestID) {
        return guestMap.remove(guestID) != null;
    }

    public boolean hasGuest(String guestID) {
        return guestMap.containsKey(guestID);
    }

    public Collection<Guest> getAllGuests() {
        return guestMap.values();
    }

    public int size() {
        return guestMap.size();
    }

    public void displayGuests() {
        if (guestMap.isEmpty()) {
            System.out.println("No guests registered.");
            return;
        }
        System.out.println("===== Guest Book =====");
        for (Guest g : guestMap.values()) {
            System.out.println(g);
        }
    }
}
