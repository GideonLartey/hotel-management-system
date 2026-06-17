using System;
using System.Collections.Generic;
using System.Linq;
using LantelHotelApp.Data;
using LantelHotelApp.Models;
using LantelHotelApp.Models.Enums;

namespace LantelHotelApp.Services
{
    public class AuthService
    {
        private readonly HotelDbContext _context;
        private const int MaxLoginAttempts = 5;
        private static readonly TimeSpan LockoutDuration = TimeSpan.FromMinutes(15);

        public AdminUser? CurrentUser { get; private set; }

        public AuthService(HotelDbContext context)
        {
            _context = context;
        }

        public (bool Success, string Message) Login(string username, string password)
        {
            var user = _context.AdminUsers.FirstOrDefault(u => u.Username == username);
            if (user == null)
                return (false, "Invalid username or password.");

            if (!user.IsActive)
                return (false, "This account has been deactivated. Contact your administrator.");

            if (user.LockoutEnd.HasValue && user.LockoutEnd > DateTime.Now)
            {
                var remaining = (user.LockoutEnd.Value - DateTime.Now).Minutes + 1;
                return (false, $"Account is locked. Try again in {remaining} minute(s).");
            }

            if (!BCrypt.Net.BCrypt.Verify(password, user.PasswordHash))
            {
                user.FailedLoginAttempts++;
                if (user.FailedLoginAttempts >= MaxLoginAttempts)
                {
                    user.LockoutEnd = DateTime.Now.Add(LockoutDuration);
                    user.FailedLoginAttempts = 0;
                    _context.SaveChanges();
                    return (false, "Too many failed attempts. Account locked for 15 minutes.");
                }
                _context.SaveChanges();
                return (false, "Invalid username or password.");
            }

            user.FailedLoginAttempts = 0;
            user.LockoutEnd = null;
            user.LastLogin = DateTime.Now;
            _context.SaveChanges();
            CurrentUser = user;

            if (user.MustChangePassword)
                return (true, "MUST_CHANGE_PASSWORD");

            return (true, $"Welcome, {user.FullName}!");
        }

        public void Logout()
        {
            CurrentUser = null;
        }

        public (bool Success, string Message) AddStaffAccess(string employeeId, string email, string fullName, StaffRole role)
        {
            if (CurrentUser?.Role != StaffRole.Manager)
                return (false, "Only managers can add staff access.");

            if (_context.AdminUsers.Any(u => u.EmployeeId == employeeId))
                return (false, "Employee ID already exists.");

            if (!string.IsNullOrEmpty(email) && _context.AdminUsers.Any(u => u.Email == email))
                return (false, "Email already registered.");

            string tempPassword = GenerateTemporaryPassword();
            string username = GenerateUsername(fullName);

            var newUser = new AdminUser
            {
                EmployeeId = employeeId,
                Username = username,
                PasswordHash = BCrypt.Net.BCrypt.HashPassword(tempPassword),
                FullName = fullName,
                Email = email,
                Role = role,
                IsActive = true,
                MustChangePassword = true,
                CreatedByAdminId = CurrentUser.Id,
                CreatedAt = DateTime.Now
            };

            _context.AdminUsers.Add(newUser);
            _context.SaveChanges();

            return (true, $"Staff access granted.\nUsername: {username}\nTemporary Password: {tempPassword}\nPlease share these credentials securely.");
        }

        public (bool Success, string Message) ChangePassword(string currentPassword, string newPassword)
        {
            if (CurrentUser == null)
                return (false, "No user is logged in.");

            if (!BCrypt.Net.BCrypt.Verify(currentPassword, CurrentUser.PasswordHash))
                return (false, "Current password is incorrect.");

            if (newPassword.Length < 8)
                return (false, "Password must be at least 8 characters long.");

            CurrentUser.PasswordHash = BCrypt.Net.BCrypt.HashPassword(newPassword);
            CurrentUser.MustChangePassword = false;
            _context.SaveChanges();

            return (true, "Password changed successfully.");
        }

        public (bool Success, string Message) ResetPassword(int userId)
        {
            if (CurrentUser?.Role != StaffRole.Manager)
                return (false, "Only managers can reset passwords.");

            var user = _context.AdminUsers.Find(userId);
            if (user == null)
                return (false, "User not found.");

            string tempPassword = GenerateTemporaryPassword();
            user.PasswordHash = BCrypt.Net.BCrypt.HashPassword(tempPassword);
            user.MustChangePassword = true;
            user.FailedLoginAttempts = 0;
            user.LockoutEnd = null;
            _context.SaveChanges();

            return (true, $"Password reset.\nNew Temporary Password: {tempPassword}");
        }

        public (bool Success, string Message) DeactivateStaff(int userId)
        {
            if (CurrentUser?.Role != StaffRole.Manager)
                return (false, "Only managers can deactivate staff.");

            var user = _context.AdminUsers.Find(userId);
            if (user == null) return (false, "User not found.");
            if (user.Id == CurrentUser.Id) return (false, "You cannot deactivate your own account.");

            user.IsActive = false;
            _context.SaveChanges();
            return (true, $"{user.FullName} has been deactivated.");
        }

        public (bool Success, string Message) ActivateStaff(int userId)
        {
            if (CurrentUser?.Role != StaffRole.Manager)
                return (false, "Only managers can activate staff.");

            var user = _context.AdminUsers.Find(userId);
            if (user == null) return (false, "User not found.");

            user.IsActive = true;
            _context.SaveChanges();
            return (true, $"{user.FullName} has been activated.");
        }

        public List<AdminUser> GetAllStaff()
        {
            return _context.AdminUsers.OrderBy(u => u.FullName).ToList();
        }

        public List<AdminUser> GetStaffByRole(StaffRole role)
        {
            return _context.AdminUsers.Where(u => u.Role == role && u.IsActive).ToList();
        }

        public bool HasPermission(string feature)
        {
            if (CurrentUser == null) return false;

            return feature switch
            {
                "GuestPortal" => CurrentUser.Role is StaffRole.Manager or StaffRole.Receptionist,
                "StaffAccess" => CurrentUser.Role == StaffRole.Manager,
                "RoomManagement" => CurrentUser.Role is StaffRole.Manager or StaffRole.Receptionist,
                "FinancialReports" => CurrentUser.Role == StaffRole.Manager,
                "Housekeeping" => CurrentUser.Role is StaffRole.Manager or StaffRole.Housekeeper,
                "Inventory" => CurrentUser.Role is StaffRole.Manager or StaffRole.Housekeeper,
                "Settings" => CurrentUser.Role == StaffRole.Manager,
                _ => false
            };
        }

        private static string GenerateTemporaryPassword()
        {
            const string chars = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnpqrstuvwxyz23456789!@#$";
            var random = new Random();
            return new string(Enumerable.Range(0, 10).Select(_ => chars[random.Next(chars.Length)]).ToArray());
        }

        private string GenerateUsername(string fullName)
        {
            string baseUsername = fullName.ToLower().Replace(" ", ".").Trim();
            if (baseUsername.Length > 20) baseUsername = baseUsername[..20];

            string username = baseUsername;
            int counter = 1;
            while (_context.AdminUsers.Any(u => u.Username == username))
            {
                username = $"{baseUsername}{counter}";
                counter++;
            }
            return username;
        }
    }
}
