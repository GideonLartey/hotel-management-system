package com.lantel.booking;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import com.lantel.room.GuestRoom;
import com.lantel.room.GuestRoomStatus;
import com.lantel.room.RoomType;

public class BookingManagerTest {

    @Test
    public void findAvailableRooms_nonOverlapping_endEqualsStart_allowed() {
        GuestRoom room = new GuestRoom(101, RoomType.DELUXE, GuestRoomStatus.AVAILABLE);
        // existing booking: Jan 1 - Jan 3
        Booking existing = new Booking("G1", 30, 123456789L, "Alice", LocalDate.of(2025,1,1), LocalDate.of(2025,1,3), LocalTime.of(14,0), LocalTime.of(11,0), 200.0, RoomType.DELUXE, 101, 2);

        LocalDate desiredStart = LocalDate.of(2025,1,3);
        LocalDate desiredEnd = LocalDate.of(2025,1,5);

        List<GuestRoom> result = BookingManager.findAvailableRooms(Arrays.asList(room), Arrays.asList(existing), RoomType.DELUXE, GuestRoomStatus.AVAILABLE, desiredStart, desiredEnd);
        assertEquals(1, result.size());
        assertEquals(101, result.get(0).getRoomID());
    }

    @Test
    public void findAvailableRooms_overlapping_excluded() {
        GuestRoom room = new GuestRoom(102, RoomType.DELUXE, GuestRoomStatus.AVAILABLE);
        // existing booking: Jan 2 - Jan 4
        Booking existing = new Booking("G2", 40, 987654321L, "Bob", LocalDate.of(2025,1,2), LocalDate.of(2025,1,4), LocalTime.of(14,0), LocalTime.of(11,0), 200.0, RoomType.DELUXE, 102, 2);

        LocalDate desiredStart = LocalDate.of(2025,1,3);
        LocalDate desiredEnd = LocalDate.of(2025,1,5);

        List<GuestRoom> result = BookingManager.findAvailableRooms(Arrays.asList(room), Arrays.asList(existing), RoomType.DELUXE, GuestRoomStatus.AVAILABLE, desiredStart, desiredEnd);
        assertEquals(0, result.size());
    }

    @Test
    public void findAvailableRooms_filtersByRoomType_nullAcceptsAll() {
        GuestRoom room1 = new GuestRoom(201, RoomType.STANDARD, GuestRoomStatus.AVAILABLE);
        GuestRoom room2 = new GuestRoom(202, RoomType.DELUXE, GuestRoomStatus.AVAILABLE);

        List<GuestRoom> result = BookingManager.findAvailableRooms(Arrays.asList(room1, room2), null, null, GuestRoomStatus.AVAILABLE, LocalDate.of(2025,2,1), LocalDate.of(2025,2,5));
        assertEquals(2, result.size());
    }
}
