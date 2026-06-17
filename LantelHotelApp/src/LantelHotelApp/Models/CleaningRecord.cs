using System;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using LantelHotelApp.Models.Enums;

namespace LantelHotelApp.Models
{
    public class CleaningRecord
    {
        [Key]
        public int Id { get; set; }

        public int RoomId { get; set; }
        [ForeignKey(nameof(RoomId))]
        public Room? Room { get; set; }

        [MaxLength(100)]
        public string HousekeeperName { get; set; } = string.Empty;

        public DateTime CleaningDate { get; set; } = DateTime.Now;

        public CleaningStatus Status { get; set; }

        [MaxLength(500)]
        public string? Notes { get; set; }
    }
}
