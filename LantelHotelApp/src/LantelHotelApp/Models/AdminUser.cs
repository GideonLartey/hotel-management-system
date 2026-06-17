using System;
using System.ComponentModel.DataAnnotations;
using LantelHotelApp.Models.Enums;

namespace LantelHotelApp.Models
{
    public class AdminUser
    {
        [Key]
        public int Id { get; set; }

        [Required]
        [MaxLength(20)]
        public string EmployeeId { get; set; } = string.Empty;

        [Required]
        [MaxLength(50)]
        public string Username { get; set; } = string.Empty;

        [Required]
        public string PasswordHash { get; set; } = string.Empty;

        [Required]
        [MaxLength(100)]
        public string FullName { get; set; } = string.Empty;

        [MaxLength(100)]
        public string? Email { get; set; }

        public StaffRole Role { get; set; }

        public bool IsActive { get; set; } = true;

        public bool MustChangePassword { get; set; } = false;

        public DateTime? LastLogin { get; set; }

        public DateTime CreatedAt { get; set; } = DateTime.Now;

        public int? CreatedByAdminId { get; set; }

        public int FailedLoginAttempts { get; set; }

        public DateTime? LockoutEnd { get; set; }
    }
}
