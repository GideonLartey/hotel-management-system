using System;
using System.Collections.Generic;
using System.Linq;
using LantelHotelApp.Data;
using LantelHotelApp.Models;
using LantelHotelApp.Models.Enums;

namespace LantelHotelApp.Services
{
    public class RoomService
    {
        private readonly HotelDbContext _context;

        public RoomService(HotelDbContext context)
        {
            _context = context;
        }

        public List<Room> GetAllRooms()
        {
            return _context.Rooms.OrderBy(r => r.RoomNumber).ToList();
        }

        public List<Room> GetRoomsByStatus(RoomStatus status)
        {
            return _context.Rooms.Where(r => r.Status == status).OrderBy(r => r.RoomNumber).ToList();
        }

        public List<Room> GetRoomsByType(RoomType type)
        {
            return _context.Rooms.Where(r => r.RoomType == type).OrderBy(r => r.RoomNumber).ToList();
        }

        public (bool Success, string Message) AddRoom(int roomNumber, int floor, RoomType type)
        {
            if (_context.Rooms.Any(r => r.RoomNumber == roomNumber))
                return (false, $"Room {roomNumber} already exists.");

            _context.Rooms.Add(new Room
            {
                RoomNumber = roomNumber,
                FloorNumber = floor,
                RoomType = type,
                Status = RoomStatus.Available,
                LastCleaned = DateTime.Now
            });
            _context.SaveChanges();
            return (true, $"Room {roomNumber} added successfully.");
        }

        public (bool Success, string Message) SetRoomStatus(int roomId, RoomStatus status)
        {
            var room = _context.Rooms.Find(roomId);
            if (room == null) return (false, "Room not found.");

            room.Status = status;
            if (status == RoomStatus.Available)
                room.LastCleaned = DateTime.Now;
            _context.SaveChanges();
            return (true, $"Room {room.RoomNumber} status updated to {status}.");
        }

        public int GetAvailableCount() => _context.Rooms.Count(r => r.Status == RoomStatus.Available);
        public int GetOccupiedCount() => _context.Rooms.Count(r => r.Status == RoomStatus.Occupied);
        public int GetTotalCount() => _context.Rooms.Count();

        public List<RoomInventoryItem> GetInventory()
        {
            return _context.InventoryItems.ToList();
        }

        public (bool Success, string Message) AddInventoryStock(string itemType, int quantity)
        {
            var item = _context.InventoryItems.FirstOrDefault(i => i.ItemType == itemType);
            if (item == null) return (false, "Item type not found.");
            if (quantity <= 0) return (false, "Quantity must be positive.");

            item.Quantity += quantity;
            _context.SaveChanges();
            return (true, $"Added {quantity} {itemType}. New total: {item.Quantity}");
        }

        public (bool Success, string Message) UseInventoryStock(string itemType, int quantity)
        {
            var item = _context.InventoryItems.FirstOrDefault(i => i.ItemType == itemType);
            if (item == null) return (false, "Item type not found.");
            if (quantity > item.Quantity) return (false, $"Insufficient stock. Available: {item.Quantity}");

            item.Quantity -= quantity;
            _context.SaveChanges();
            return (true, $"Used {quantity} {itemType}. Remaining: {item.Quantity}");
        }
    }
}
