using System;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using LantelHotelApp.Models.Enums;

namespace LantelHotelApp.Models
{
    public class Booking
    {
        public static readonly TimeSpan CheckInTime = new TimeSpan(14, 0, 0);
        public static readonly TimeSpan CheckOutTime = new TimeSpan(11, 0, 0);

        [Key]
        public int Id { get; set; }

        [Required]
        [MaxLength(20)]
        public string BookingReference { get; set; } = string.Empty;

        public int GuestId { get; set; }
        [ForeignKey(nameof(GuestId))]
        public Guest? Guest { get; set; }

        public int RoomId { get; set; }
        [ForeignKey(nameof(RoomId))]
        public Room? Room { get; set; }

        public DateTime CheckInDate { get; set; }
        public DateTime CheckOutDate { get; set; }

        public double RoomCostPerNight { get; set; }
        public double TotalAmount { get; set; }
        public double AmountPaid { get; set; }
        public double ExtraCateringCost { get; set; }
        public double DiscountApplied { get; set; }

        public RoomType RoomType { get; set; }
        public GuestType GuestType { get; set; }
        public SeasonType SeasonType { get; set; }

        public bool IsCheckedIn { get; set; }
        public bool IsCheckedOut { get; set; }
        public bool IsPaymentCompleted { get; set; }

        public DateTime CreatedAt { get; set; } = DateTime.Now;

        [NotMapped]
        public int NightsBooked => (int)(CheckOutDate.Date - CheckInDate.Date).TotalDays;

        [NotMapped]
        public double RemainingBalance => Math.Max(0, TotalAmount - AmountPaid);

        public static string GenerateReference()
        {
            return "BMP-" + Guid.NewGuid().ToString()[..6].ToUpper();
        }
    }
}
