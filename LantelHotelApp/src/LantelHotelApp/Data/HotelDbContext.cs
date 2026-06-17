using System.IO;
using Microsoft.EntityFrameworkCore;
using LantelHotelApp.Models;

namespace LantelHotelApp.Data
{
    public class HotelDbContext : DbContext
    {
        public DbSet<Guest> Guests { get; set; }
        public DbSet<Room> Rooms { get; set; }
        public DbSet<Booking> Bookings { get; set; }
        public DbSet<AdminUser> AdminUsers { get; set; }
        public DbSet<CleaningRecord> CleaningRecords { get; set; }
        public DbSet<RoomInventoryItem> InventoryItems { get; set; }
        public DbSet<AppSettings> Settings { get; set; }
        public DbSet<RoomRate> RoomRates { get; set; }

        private readonly string _dbPath;

        public HotelDbContext()
        {
            var appDataPath = Path.Combine(
                Environment.GetFolderPath(Environment.SpecialFolder.ApplicationData),
                "LantelHotel");
            Directory.CreateDirectory(appDataPath);
            _dbPath = Path.Combine(appDataPath, "hotel.db");
        }

        public HotelDbContext(DbContextOptions<HotelDbContext> options) : base(options)
        {
            _dbPath = string.Empty;
        }

        protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
        {
            if (!optionsBuilder.IsConfigured)
            {
                optionsBuilder.UseSqlite($"Data Source={_dbPath}");
            }
        }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            base.OnModelCreating(modelBuilder);

            modelBuilder.Entity<Guest>()
                .HasIndex(g => g.GuestId)
                .IsUnique();

            modelBuilder.Entity<AdminUser>()
                .HasIndex(a => a.Username)
                .IsUnique();

            modelBuilder.Entity<AdminUser>()
                .HasIndex(a => a.EmployeeId)
                .IsUnique();

            modelBuilder.Entity<Booking>()
                .HasIndex(b => b.BookingReference)
                .IsUnique();

            modelBuilder.Entity<Booking>()
                .HasOne(b => b.Guest)
                .WithMany()
                .HasForeignKey(b => b.GuestId)
                .OnDelete(DeleteBehavior.Restrict);

            modelBuilder.Entity<Booking>()
                .HasOne(b => b.Room)
                .WithMany()
                .HasForeignKey(b => b.RoomId)
                .OnDelete(DeleteBehavior.Restrict);

            modelBuilder.Entity<CleaningRecord>()
                .HasOne(c => c.Room)
                .WithMany()
                .HasForeignKey(c => c.RoomId)
                .OnDelete(DeleteBehavior.Cascade);

            modelBuilder.Entity<RoomRate>()
                .HasIndex(r => new { r.RoomType, r.SeasonType })
                .IsUnique();

            modelBuilder.Entity<Room>()
                .HasIndex(r => r.RoomNumber)
                .IsUnique();
        }
    }
}
