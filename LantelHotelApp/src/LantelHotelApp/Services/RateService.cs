using System.Collections.Generic;
using System.Linq;
using LantelHotelApp.Data;
using LantelHotelApp.Models;
using LantelHotelApp.Models.Enums;

namespace LantelHotelApp.Services
{
    public class RateService
    {
        private readonly HotelDbContext _context;

        public RateService(HotelDbContext context)
        {
            _context = context;
        }

        public double GetRate(RoomType roomType, SeasonType seasonType)
        {
            var rate = _context.RoomRates
                .FirstOrDefault(r => r.RoomType == roomType && r.SeasonType == seasonType);
            return rate?.Rate ?? roomType.GetBasePrice();
        }

        public (bool Success, string Message) UpdateRate(RoomType roomType, SeasonType seasonType, double newRate)
        {
            if (newRate <= 0)
                return (false, "Rate must be positive.");

            var rate = _context.RoomRates
                .FirstOrDefault(r => r.RoomType == roomType && r.SeasonType == seasonType);

            if (rate != null)
            {
                rate.Rate = newRate;
            }
            else
            {
                _context.RoomRates.Add(new RoomRate
                {
                    RoomType = roomType,
                    SeasonType = seasonType,
                    Rate = newRate
                });
            }

            _context.SaveChanges();
            return (true, $"Rate updated: {roomType.GetDisplayName()} ({seasonType}) = {newRate:N2}");
        }

        public List<RoomRate> GetAllRates()
        {
            return _context.RoomRates
                .OrderBy(r => r.RoomType)
                .ThenBy(r => r.SeasonType)
                .ToList();
        }
    }
}
