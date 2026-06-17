using System.ComponentModel.DataAnnotations;

namespace LantelHotelApp.Models
{
    public class AppSettings
    {
        [Key]
        public int Id { get; set; }

        [Required]
        [MaxLength(10)]
        public string CurrencyCode { get; set; } = "GHS";

        [Required]
        [MaxLength(5)]
        public string CurrencySymbol { get; set; } = "GH₵";

        [MaxLength(100)]
        public string HotelName { get; set; } = "Lantel Hotel";

        public bool IsFirstLaunch { get; set; } = true;
    }
}
