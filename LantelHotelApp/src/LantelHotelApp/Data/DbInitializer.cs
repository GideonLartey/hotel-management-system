using LantelHotelApp.Models;
using LantelHotelApp.Models.Enums;

namespace LantelHotelApp.Data
{
    public static class DbInitializer
    {
        public static void Initialize(HotelDbContext context)
        {
            context.Database.EnsureCreated();

            // Seed rooms (25 rooms: 5 per type, matching Java initializeRooms())
            if (!context.Rooms.Any())
            {
                int roomId = 101;
                foreach (RoomType type in Enum.GetValues<RoomType>())
                {
                    int floor = (int)type + 1;
                    for (int i = 0; i < 5; i++)
                    {
                        context.Rooms.Add(new Room
                        {
                            RoomNumber = roomId,
                            FloorNumber = floor,
                            RoomType = type,
                            Status = RoomStatus.Available,
                            LastCleaned = DateTime.Now
                        });
                        roomId++;
                    }
                }
                context.SaveChanges();
            }

            // Seed default admin (matches Java StaffManager defaults)
            if (!context.AdminUsers.Any())
            {
                context.AdminUsers.Add(new AdminUser
                {
                    EmployeeId = "ADM001",
                    Username = "admin",
                    PasswordHash = BCrypt.Net.BCrypt.HashPassword("admin123"),
                    FullName = "System Administrator",
                    Email = "admin@lantelhotel.com",
                    Role = StaffRole.Manager,
                    IsActive = true,
                    MustChangePassword = false
                });
                context.SaveChanges();
            }

            // Seed room rates (matching all RoomType x SeasonType combinations)
            if (!context.RoomRates.Any())
            {
                var rates = new Dictionary<(RoomType, SeasonType), double>
                {
                    { (RoomType.Standard, SeasonType.HighDemand), 100.0 },
                    { (RoomType.Standard, SeasonType.SlowBusiness), 65.0 },
                    { (RoomType.Standard, SeasonType.Special), 80.0 },
                    { (RoomType.Deluxe, SeasonType.HighDemand), 200.0 },
                    { (RoomType.Deluxe, SeasonType.SlowBusiness), 120.0 },
                    { (RoomType.Deluxe, SeasonType.Special), 150.0 },
                    { (RoomType.ExecutiveSuite, SeasonType.HighDemand), 400.0 },
                    { (RoomType.ExecutiveSuite, SeasonType.SlowBusiness), 240.0 },
                    { (RoomType.ExecutiveSuite, SeasonType.Special), 300.0 },
                    { (RoomType.VipSuite, SeasonType.HighDemand), 650.0 },
                    { (RoomType.VipSuite, SeasonType.SlowBusiness), 400.0 },
                    { (RoomType.VipSuite, SeasonType.Special), 500.0 },
                    { (RoomType.Penthouse, SeasonType.HighDemand), 1300.0 },
                    { (RoomType.Penthouse, SeasonType.SlowBusiness), 800.0 },
                    { (RoomType.Penthouse, SeasonType.Special), 1000.0 },
                };

                foreach (var kvp in rates)
                {
                    context.RoomRates.Add(new RoomRate
                    {
                        RoomType = kvp.Key.Item1,
                        SeasonType = kvp.Key.Item2,
                        Rate = kvp.Value
                    });
                }
                context.SaveChanges();
            }

            // Seed inventory (matching Java CLI defaults: 200, 150, 100, 250)
            if (!context.InventoryItems.Any())
            {
                context.InventoryItems.AddRange(
                    new RoomInventoryItem { ItemType = "Linens", Quantity = 200, LowStockThreshold = 50 },
                    new RoomInventoryItem { ItemType = "Toiletries", Quantity = 150, LowStockThreshold = 30 },
                    new RoomInventoryItem { ItemType = "Supplies", Quantity = 100, LowStockThreshold = 40 },
                    new RoomInventoryItem { ItemType = "Towels", Quantity = 250, LowStockThreshold = 60 }
                );
                context.SaveChanges();
            }

            // Seed default settings
            if (!context.Settings.Any())
            {
                context.Settings.Add(new AppSettings
                {
                    CurrencyCode = "GHS",
                    CurrencySymbol = "GH₵",
                    HotelName = "Lantel Hotel",
                    IsFirstLaunch = true
                });
                context.SaveChanges();
            }
        }
    }
}
