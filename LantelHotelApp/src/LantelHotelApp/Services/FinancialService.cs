using System;
using System.Collections.Generic;
using System.Linq;
using LantelHotelApp.Data;
using LantelHotelApp.Models;

namespace LantelHotelApp.Services
{
    public class FinancialService
    {
        private readonly HotelDbContext _context;

        public FinancialService(HotelDbContext context)
        {
            _context = context;
        }

        public double GetTotalRevenue(DateTime? from = null, DateTime? to = null)
        {
            var query = _context.Bookings.Where(b => b.IsCheckedOut);
            if (from.HasValue) query = query.Where(b => b.CheckOutDate >= from.Value);
            if (to.HasValue) query = query.Where(b => b.CheckOutDate <= to.Value);
            return query.Any() ? query.Sum(b => b.AmountPaid) : 0;
        }

        public double GetAverageRoomRate(DateTime? from = null, DateTime? to = null)
        {
            var query = _context.Bookings.Where(b => b.IsCheckedOut);
            if (from.HasValue) query = query.Where(b => b.CheckOutDate >= from.Value);
            if (to.HasValue) query = query.Where(b => b.CheckOutDate <= to.Value);

            if (!query.Any()) return 0;
            return query.Average(b => b.RoomCostPerNight);
        }

        public int GetOccupiedRoomsCount()
        {
            return _context.Bookings.Count(b => b.IsCheckedIn && !b.IsCheckedOut);
        }

        public double GetOccupancyRate()
        {
            int totalRooms = _context.Rooms.Count();
            if (totalRooms == 0) return 0;
            int occupied = GetOccupiedRoomsCount();
            return (double)occupied / totalRooms * 100;
        }

        public int GetTodayCheckIns()
        {
            var today = DateTime.Today;
            return _context.Bookings.Count(b => b.CheckInDate.Date == today && b.IsCheckedIn);
        }

        public int GetTodayCheckOuts()
        {
            var today = DateTime.Today;
            return _context.Bookings.Count(b => b.CheckOutDate.Date == today && b.IsCheckedOut);
        }

        public int GetTotalBookings()
        {
            return _context.Bookings.Count();
        }

        public int GetActiveBookings()
        {
            return _context.Bookings.Count(b => !b.IsCheckedOut);
        }

        public List<Booking> GetRevenueBookings(DateTime? from = null, DateTime? to = null)
        {
            var query = _context.Bookings.Where(b => b.IsCheckedOut);
            if (from.HasValue) query = query.Where(b => b.CheckOutDate >= from.Value);
            if (to.HasValue) query = query.Where(b => b.CheckOutDate <= to.Value);
            return query.OrderByDescending(b => b.CheckOutDate).ToList();
        }
    }
}
