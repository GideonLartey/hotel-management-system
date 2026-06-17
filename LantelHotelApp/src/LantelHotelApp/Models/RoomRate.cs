using System.ComponentModel.DataAnnotations;
using LantelHotelApp.Models.Enums;

namespace LantelHotelApp.Models
{
    public class RoomRate
    {
        [Key]
        public int Id { get; set; }

        public RoomType RoomType { get; set; }

        public SeasonType SeasonType { get; set; }

        public double Rate { get; set; }
    }
}
