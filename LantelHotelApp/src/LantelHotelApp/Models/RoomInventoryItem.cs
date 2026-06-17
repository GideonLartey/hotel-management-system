using System.ComponentModel.DataAnnotations;

namespace LantelHotelApp.Models
{
    public class RoomInventoryItem
    {
        [Key]
        public int Id { get; set; }

        [Required]
        [MaxLength(50)]
        public string ItemType { get; set; } = string.Empty;

        public int Quantity { get; set; }

        public int LowStockThreshold { get; set; }

        public bool IsLowStock => Quantity < LowStockThreshold;
    }
}
