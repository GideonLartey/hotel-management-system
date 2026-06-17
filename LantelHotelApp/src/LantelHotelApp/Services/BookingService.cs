using System;
using System.Collections.Generic;
using System.Linq;
using LantelHotelApp.Data;
using LantelHotelApp.Models;
using LantelHotelApp.Models.Enums;
using Microsoft.EntityFrameworkCore;

namespace LantelHotelApp.Services
{
    public class BookingService
    {
        private readonly HotelDbContext _context;
        private const double DefaultDiscount = 0.15;
        private const double DefaultCateringCost = 20.0;

        public BookingService(HotelDbContext context)
        {
            _context = context;
        }

        public List<Room> FindAvailableRooms(DateTime checkIn, DateTime checkOut, RoomType? roomType = null)
        {
            var query = _context.Rooms.Where(r => r.Status == RoomStatus.Available);

            if (roomType.HasValue)
                query = query.Where(r => r.RoomType == roomType.Value);

            var bookedRoomIds = _context.Bookings
                .Where(b => !b.IsCheckedOut &&
                       b.CheckInDate < checkOut &&
                       b.CheckOutDate > checkIn)
                .Select(b => b.RoomId)
                .ToList();

            return query.Where(r => !bookedRoomIds.Contains(r.Id)).ToList();
        }

        public (bool Success, string Message, Booking? Booking) CreateBooking(
            string guestId, string guestName, int guestAge, string phoneNumber, string? email,
            int roomId, DateTime checkIn, DateTime checkOut,
            GuestType guestType, SeasonType seasonType)
        {
            if (guestAge < 18)
                return (false, "Guest must be at least 18 years old.", null);

            if (checkOut <= checkIn)
                return (false, "Check-out date must be after check-in date.", null);

            var room = _context.Rooms.Find(roomId);
            if (room == null)
                return (false, "Room not found.", null);

            bool hasConflict = _context.Bookings.Any(b =>
                b.RoomId == roomId && !b.IsCheckedOut &&
                b.CheckInDate < checkOut && b.CheckOutDate > checkIn);

            if (hasConflict)
                return (false, "Room is not available for the selected dates.", null);

            var guest = _context.Guests.FirstOrDefault(g => g.GuestId == guestId);
            if (guest == null)
            {
                guest = new Guest
                {
                    GuestId = guestId,
                    Name = guestName,
                    Age = guestAge,
                    PhoneNumber = phoneNumber,
                    Email = email
                };
                _context.Guests.Add(guest);
                _context.SaveChanges();
            }

            double totalAmount = CalculateAmount(checkIn, checkOut, room.RoomType, guestType, seasonType);

            var booking = new Booking
            {
                BookingReference = Booking.GenerateReference(),
                GuestId = guest.Id,
                RoomId = room.Id,
                CheckInDate = checkIn,
                CheckOutDate = checkOut,
                RoomCostPerNight = room.RoomType.GetBasePrice(),
                TotalAmount = totalAmount,
                RoomType = room.RoomType,
                GuestType = guestType,
                SeasonType = seasonType,
                ExtraCateringCost = DefaultCateringCost,
                DiscountApplied = (guestType == GuestType.Vvip &&
                    (seasonType == SeasonType.Special || seasonType == SeasonType.SlowBusiness))
                    ? DefaultDiscount : 0,
                CreatedAt = DateTime.Now
            };

            _context.Bookings.Add(booking);
            room.Status = RoomStatus.Reserved;
            _context.SaveChanges();

            return (true, $"Booking confirmed! Reference: {booking.BookingReference}", booking);
        }

        public (bool Success, string Message) ProcessPayment(int bookingId, double amount)
        {
            var booking = _context.Bookings.Find(bookingId);
            if (booking == null) return (false, "Booking not found.");

            booking.AmountPaid += amount;
            if (booking.AmountPaid >= booking.TotalAmount)
                booking.IsPaymentCompleted = true;
            _context.SaveChanges();

            return (true, booking.IsPaymentCompleted
                ? "Payment completed successfully."
                : $"Partial payment received. Remaining: {booking.RemainingBalance:N2}");
        }

        public (bool Success, string Message) CheckIn(int bookingId)
        {
            var booking = _context.Bookings.Include(b => b.Room).FirstOrDefault(b => b.Id == bookingId);
            if (booking == null) return (false, "Booking not found.");
            if (booking.IsCheckedIn) return (false, "Guest is already checked in.");
            if (!booking.IsPaymentCompleted) return (false, "Payment must be completed before check-in.");

            booking.IsCheckedIn = true;
            if (booking.Room != null)
                booking.Room.Status = RoomStatus.Occupied;
            _context.SaveChanges();

            return (true, $"Guest checked in successfully to Room {booking.Room?.RoomNumber}.");
        }

        public (bool Success, string Message) CheckOut(int bookingId)
        {
            var booking = _context.Bookings.Include(b => b.Room).FirstOrDefault(b => b.Id == bookingId);
            if (booking == null) return (false, "Booking not found.");
            if (!booking.IsCheckedIn) return (false, "Guest hasn't checked in yet.");
            if (booking.IsCheckedOut) return (false, "Guest is already checked out.");

            booking.IsCheckedOut = true;
            if (booking.Room != null)
                booking.Room.Status = RoomStatus.ScheduledCleaning;
            _context.SaveChanges();

            return (true, "Guest checked out successfully. Thank you for staying at Lantel Hotel!");
        }

        public List<Booking> GetAllBookings()
        {
            return _context.Bookings
                .Include(b => b.Guest)
                .Include(b => b.Room)
                .OrderByDescending(b => b.CreatedAt)
                .ToList();
        }

        public List<Booking> GetActiveBookings()
        {
            return _context.Bookings
                .Include(b => b.Guest)
                .Include(b => b.Room)
                .Where(b => !b.IsCheckedOut)
                .OrderBy(b => b.CheckInDate)
                .ToList();
        }

        public Booking? GetBookingByReference(string reference)
        {
            return _context.Bookings
                .Include(b => b.Guest)
                .Include(b => b.Room)
                .FirstOrDefault(b => b.BookingReference == reference);
        }

        private double CalculateAmount(DateTime checkIn, DateTime checkOut, RoomType roomType,
            GuestType guestType, SeasonType seasonType)
        {
            int nights = (int)(checkOut.Date - checkIn.Date).TotalDays;
            double costPerNight = roomType.GetBasePrice();

            var rate = _context.RoomRates.FirstOrDefault(r => r.RoomType == roomType && r.SeasonType == seasonType);
            if (rate != null)
                costPerNight = rate.Rate;

            double flatAmount = (costPerNight * nights) + DefaultCateringCost;

            // Fixed operator precedence bug from Java version
            double discount = 0.0;
            if (guestType == GuestType.Vvip &&
                (seasonType == SeasonType.Special || seasonType == SeasonType.SlowBusiness))
            {
                discount = DefaultDiscount;
            }

            return flatAmount * (1 - discount);
        }
    }
}
