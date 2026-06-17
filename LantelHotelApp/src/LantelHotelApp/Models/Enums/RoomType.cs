namespace LantelHotelApp.Models.Enums
{
    public enum RoomType
    {
        Standard,
        Deluxe,
        ExecutiveSuite,
        VipSuite,
        Penthouse
    }

    public static class RoomTypeExtensions
    {
        public static double GetBasePrice(this RoomType roomType) => roomType switch
        {
            RoomType.Standard => 80.0,
            RoomType.Deluxe => 150.0,
            RoomType.ExecutiveSuite => 300.0,
            RoomType.VipSuite => 500.0,
            RoomType.Penthouse => 1000.0,
            _ => 80.0
        };

        public static string GetDisplayName(this RoomType roomType) => roomType switch
        {
            RoomType.Standard => "Standard Room",
            RoomType.Deluxe => "Deluxe Room",
            RoomType.ExecutiveSuite => "Executive Suite",
            RoomType.VipSuite => "VIP Suite",
            RoomType.Penthouse => "VVIP Penthouse Suite",
            _ => roomType.ToString()
        };
    }
}
