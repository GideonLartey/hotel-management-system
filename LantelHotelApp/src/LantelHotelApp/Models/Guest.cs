using System;
using System.ComponentModel.DataAnnotations;

namespace LantelHotelApp.Models
{
    public class Guest
    {
        [Key]
        public int Id { get; set; }

        [Required]
        [MaxLength(50)]
        public string GuestId { get; set; } = string.Empty;

        [Required]
        [MaxLength(100)]
        public string Name { get; set; } = string.Empty;

        [Range(18, 150)]
        public int Age { get; set; }

        [Required]
        [MaxLength(20)]
        public string PhoneNumber { get; set; } = string.Empty;

        [MaxLength(100)]
        public string? Email { get; set; }

        public DateTime CreatedAt { get; set; } = DateTime.Now;
    }
}
