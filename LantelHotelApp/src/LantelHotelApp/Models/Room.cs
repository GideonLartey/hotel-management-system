using System;
using System.ComponentModel.DataAnnotations;
using LantelHotelApp.Models.Enums;

namespace LantelHotelApp.Models
{
    public class Room
    {
        [Key]
        public int Id { get; set; }

        public int RoomNumber { get; set; }

        public int FloorNumber { get; set; }

        public RoomType RoomType { get; set; }

        public RoomStatus Status { get; set; } = RoomStatus.Available;

        public DateTime? LastCleaned { get; set; } = DateTime.Now;

        public string DisplayName => $"Room {RoomNumber}";
    }
}
