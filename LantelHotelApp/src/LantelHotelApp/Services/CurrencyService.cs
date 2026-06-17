using LantelHotelApp.Data;
using LantelHotelApp.Models;

namespace LantelHotelApp.Services
{
    public class CurrencyService
    {
        private readonly HotelDbContext _context;
        private AppSettings? _cachedSettings;

        public CurrencyService(HotelDbContext context)
        {
            _context = context;
        }

        public string GetCurrencySymbol()
        {
            var settings = GetSettings();
            return settings.CurrencySymbol;
        }

        public string GetCurrencyCode()
        {
            var settings = GetSettings();
            return settings.CurrencyCode;
        }

        public string FormatAmount(double amount)
        {
            return $"{GetCurrencySymbol()} {amount:N2}";
        }

        public void SetCurrency(string code, string symbol)
        {
            var settings = GetSettings();
            settings.CurrencyCode = code;
            settings.CurrencySymbol = symbol;
            _context.SaveChanges();
            _cachedSettings = null;
        }

        public bool IsFirstLaunch()
        {
            var settings = GetSettings();
            return settings.IsFirstLaunch;
        }

        public void MarkFirstLaunchComplete()
        {
            var settings = GetSettings();
            settings.IsFirstLaunch = false;
            _context.SaveChanges();
            _cachedSettings = null;
        }

        private AppSettings GetSettings()
        {
            if (_cachedSettings == null)
            {
                _cachedSettings = _context.Settings.FirstOrDefault();
                if (_cachedSettings == null)
                {
                    _cachedSettings = new AppSettings();
                    _context.Settings.Add(_cachedSettings);
                    _context.SaveChanges();
                }
            }
            return _cachedSettings;
        }
    }
}
