package com.lantel.booking;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;

import com.lantel.room.GuestType;
import com.lantel.room.RoomType;
import com.lantel.room.SeasonType;

public class BookingManagerPaymentTest {

    @Test
    public void vvipInSpecialSeason_getsDiscount() {
        RoomType.DELUXE.setCostPerNight(100.0);
        BookingList list = new BookingList(null, 0.0, 0);
        BookingManager bm = new BookingManager(list, "1", null, RoomType.DELUXE, 0.2, 10.0, 0.0, 0.0);

        LocalDate in = LocalDate.of(2025, 6, 1);
        LocalDate out = LocalDate.of(2025, 6, 3); // 2 nights

        bm.calculateAmountToPay(in, out, GuestType.VVIP_GUEST, SeasonType.SPECIAL);
        double expected = (100.0 * 2 + 10.0) * (1 - 0.2); // (200+10)*0.8 = 168.0
        assertEquals(expected, bm.getAmountToPay(), 0.001);
    }

    @Test
    public void regularGuest_noDiscount() {
        RoomType.DELUXE.setCostPerNight(100.0);
        BookingList list = new BookingList(null, 0.0, 0);
        BookingManager bm = new BookingManager(list, "1", null, RoomType.DELUXE, 0.2, 10.0, 0.0, 0.0);

        LocalDate in = LocalDate.of(2025, 6, 1);
        LocalDate out = LocalDate.of(2025, 6, 3); // 2 nights

        bm.calculateAmountToPay(in, out, GuestType.REGULAR_GUEST, SeasonType.SPECIAL);
        double expected = (100.0 * 2 + 10.0); // no discount
        assertEquals(expected, bm.getAmountToPay(), 0.001);
    }

    @Test
    public void paymentCompletes_andBookingNumberAssigned() {
        RoomType.DELUXE.setCostPerNight(100.0);
        BookingList list = new BookingList(null, 0.0, 0);
        BookingManager bm = new BookingManager(list, "1", null, RoomType.DELUXE, 0.1, 5.0, 0.0, 0.0);

        LocalDate in = LocalDate.of(2025, 7, 1);
        LocalDate out = LocalDate.of(2025, 7, 4); // 3 nights

        bm.calculateAmountToPay(in, out, GuestType.VVIP_GUEST, SeasonType.SPECIAL);
        double due = bm.getAmountToPay();
        bm.makePayments(due);
        assertNotNull(bm.getBookingNumber());
    }

    @Test
    public void createReservation_storesGuestTypeAndSeason() {
        RoomType.DELUXE.setCostPerNight(100.0);
        BookingList list = new BookingList(null, 0.0, 0);
        BookingManager bm = new BookingManager(list, "1", null, RoomType.DELUXE, 0.1, 5.0, 0.0, 0.0);

        LocalDate in = LocalDate.of(2025, 8, 1);
        LocalDate out = LocalDate.of(2025, 8, 3);
        bm.createANewReservation("Eve", 35, 44444444L, "ID-123", in, out, 250.0, RoomType.DELUXE, "1", GuestType.VVIP_GUEST, SeasonType.SPECIAL);
        assertEquals(1, list.getBookingList().size());
        Booking b = list.getBookingList().get(0);
        assertEquals(GuestType.VVIP_GUEST, b.getGuestType());
        assertEquals(SeasonType.SPECIAL, b.getSeasonType());
    }
}
