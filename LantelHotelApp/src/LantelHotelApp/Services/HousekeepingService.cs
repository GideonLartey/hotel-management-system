using System;
using System.Collections.Generic;
using System.Linq;
using LantelHotelApp.Data;
using LantelHotelApp.Models;
using LantelHotelApp.Models.Enums;
using Microsoft.EntityFrameworkCore;

namespace LantelHotelApp.Services
{
    public class HousekeepingService
    {
        private readonly HotelDbContext _context;

        public HousekeepingService(HotelDbContext context)
        {
            _context = context;
        }

        public (bool Success, string Message) RecordCleaning(int roomId, string housekeeperName, string? notes)
        {
            var room = _context.Rooms.Find(roomId);
            if (room == null) return (false, "Room not found.");

            var record = new CleaningRecord
            {
                RoomId = roomId,
                HousekeeperName = housekeeperName,
                CleaningDate = DateTime.Now,
                Status = CleaningStatus.Cleaned,
                Notes = notes
            };

            _context.CleaningRecords.Add(record);
            room.Status = RoomStatus.Available;
            room.LastCleaned = DateTime.Now;
            _context.SaveChanges();

            return (true, $"Cleaning recorded for Room {room.RoomNumber}.");
        }

        public (bool Success, string Message) MarkForCleaning(int roomId)
        {
            var room = _context.Rooms.Find(roomId);
            if (room == null) return (false, "Room not found.");

            var record = new CleaningRecord
            {
                RoomId = roomId,
                HousekeeperName = "System",
                CleaningDate = DateTime.Now,
                Status = CleaningStatus.NeedsCleaning
            };

            _context.CleaningRecords.Add(record);
            room.Status = RoomStatus.ScheduledCleaning;
            _context.SaveChanges();

            return (true, $"Room {room.RoomNumber} marked for cleaning.");
        }

        public List<CleaningRecord> GetCleaningLog()
        {
            return _context.CleaningRecords
                .Include(c => c.Room)
                .OrderByDescending(c => c.CleaningDate)
                .ToList();
        }

        public List<CleaningRecord> GetRoomCleaningHistory(int roomId)
        {
            return _context.CleaningRecords
                .Where(c => c.RoomId == roomId)
                .OrderByDescending(c => c.CleaningDate)
                .ToList();
        }

        public List<Room> GetRoomsNeedingCleaning()
        {
            return _context.Rooms
                .Where(r => r.Status == RoomStatus.ScheduledCleaning)
                .OrderBy(r => r.RoomNumber)
                .ToList();
        }
    }
}
