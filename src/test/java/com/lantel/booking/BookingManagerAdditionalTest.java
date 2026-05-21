package com.lantel.booking;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;

import com.lantel.room.GuestRoom;
import com.lantel.room.GuestRoomStatus;
import com.lantel.room.RoomType;

public class BookingManagerAdditionalTest {

    @Test
    public void nullAllRooms_returnsEmpty() {
        List<GuestRoom> result = BookingManager.findAvailableRooms(null, null, null, null, LocalDate.now(), LocalDate.now().plusDays(1));
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void nullBookingList_acceptsAllAvailableRooms() {
        GuestRoom r1 = new GuestRoom(301, RoomType.DELUXE, GuestRoomStatus.AVAILABLE);
        GuestRoom r2 = new GuestRoom(302, RoomType.DELUXE, GuestRoomStatus.AVAILABLE);
        List<GuestRoom> result = BookingManager.findAvailableRooms(Arrays.asList(r1, r2), null, RoomType.DELUXE, GuestRoomStatus.AVAILABLE, LocalDate.of(2025,3,1), LocalDate.of(2025,3,5));
        assertEquals(2, result.size());
    }

    @Test
    public void desiredStatus_null_defaultsToAvailable() {
        GuestRoom r1 = new GuestRoom(401, RoomType.STANDARD, GuestRoomStatus.AVAILABLE);
        GuestRoom r2 = new GuestRoom(402, RoomType.STANDARD, GuestRoomStatus.OCCUPIED);
        List<GuestRoom> result = BookingManager.findAvailableRooms(Arrays.asList(r1, r2), null, RoomType.STANDARD, null, LocalDate.of(2025,4,1), LocalDate.of(2025,4,5));
        assertEquals(1, result.size());
        assertEquals(401, result.get(0).getRoomID());
    }

    @Test
    public void occupiedRooms_notReturned() {
        GuestRoom r1 = new GuestRoom(501, RoomType.DELUXE, GuestRoomStatus.OCCUPIED);
        Booking existing = new Booking("G3", 50, 55555555L, "Carol", LocalDate.of(2025,5,1), LocalDate.of(2025,5,3), LocalTime.of(14,0), LocalTime.of(11,0), 300.0, RoomType.DELUXE, 501, 2);
        List<GuestRoom> result = BookingManager.findAvailableRooms(Arrays.asList(r1), Arrays.asList(existing), RoomType.DELUXE, GuestRoomStatus.AVAILABLE, LocalDate.of(2025,5,2), LocalDate.of(2025,5,4));
        assertEquals(0, result.size());
    }
}