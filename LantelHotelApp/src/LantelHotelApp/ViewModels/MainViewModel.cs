using System.Windows;
using System.Windows.Input;
using LantelHotelApp.Data;
using LantelHotelApp.Helpers;
using LantelHotelApp.Services;
using LantelHotelApp.Views;

namespace LantelHotelApp.ViewModels
{
    public class MainViewModel : BaseViewModel
    {
        private readonly HotelDbContext _context;
        private readonly NavigationService _navigation;

        public AuthService Auth { get; }
        public CurrencyService Currency { get; }
        public BookingService Bookings { get; }
        public RoomService Rooms { get; }
        public FinancialService Financial { get; }
        public HousekeepingService Housekeeping { get; }
        public RateService Rates { get; }

        private string _windowTitle = "Lantel Hotel Management System";
        public string WindowTitle
        {
            get => _windowTitle;
            set => SetProperty(ref _windowTitle, value);
        }

        private string _statusMessage = string.Empty;
        public string StatusMessage
        {
            get => _statusMessage;
            set => SetProperty(ref _statusMessage, value);
        }

        private string _currentUserDisplay = string.Empty;
        public string CurrentUserDisplay
        {
            get => _currentUserDisplay;
            set => SetProperty(ref _currentUserDisplay, value);
        }

        private bool _isLoggedIn;
        public bool IsLoggedIn
        {
            get => _isLoggedIn;
            set => SetProperty(ref _isLoggedIn, value);
        }

        public ICommand LogoutCommand { get; }
        public ICommand NavigateCommand { get; }

        public MainViewModel(HotelDbContext context, NavigationService navigation)
        {
            _context = context;
            _navigation = navigation;

            Auth = new AuthService(context);
            Currency = new CurrencyService(context);
            Bookings = new BookingService(context);
            Rooms = new RoomService(context);
            Financial = new FinancialService(context);
            Housekeeping = new HousekeepingService(context);
            Rates = new RateService(context);

            LogoutCommand = new RelayCommand(ExecuteLogout);
            NavigateCommand = new RelayCommand(ExecuteNavigate);
        }

        public NavigationService Navigation => _navigation;

        public void Initialize()
        {
            DbInitializer.Initialize(_context);

            if (Currency.IsFirstLaunch())
            {
                _navigation.NavigateTo(new CurrencySelectionView(this));
            }
            else
            {
                _navigation.NavigateTo(new LoginView(this));
            }
        }

        public void OnLoginSuccess()
        {
            IsLoggedIn = true;
            CurrentUserDisplay = $"{Auth.CurrentUser?.FullName} ({Auth.CurrentUser?.Role})";

            if (Auth.CurrentUser?.MustChangePassword == true)
            {
                _navigation.NavigateTo(new FirstLoginView(this));
            }
            else
            {
                _navigation.NavigateTo(new AdminDashboardView(this));
            }
        }

        public void ShowDashboard()
        {
            _navigation.NavigateTo(new AdminDashboardView(this));
        }

        private void ExecuteLogout()
        {
            Auth.Logout();
            IsLoggedIn = false;
            CurrentUserDisplay = string.Empty;
            StatusMessage = "Logged out successfully.";
            _navigation.NavigateTo(new LoginView(this));
        }

        private void ExecuteNavigate(object? parameter)
        {
            if (parameter is not string target) return;

            switch (target)
            {
                case "Dashboard":
                    _navigation.NavigateTo(new AdminDashboardView(this));
                    break;
                case "GuestPortal":
                    if (Auth.HasPermission("GuestPortal"))
                        _navigation.NavigateTo(new GuestPortalView(this));
                    else
                        StatusMessage = "Access denied.";
                    break;
                case "Bookings":
                    if (Auth.HasPermission("GuestPortal"))
                        _navigation.NavigateTo(new BookingsView(this));
                    else
                        StatusMessage = "Access denied.";
                    break;
                case "SearchRooms":
                    if (Auth.HasPermission("RoomManagement"))
                        _navigation.NavigateTo(new SearchRoomsView(this));
                    else
                        StatusMessage = "Access denied.";
                    break;
                case "CheckIn":
                    if (Auth.HasPermission("GuestPortal"))
                        _navigation.NavigateTo(new CheckInView(this));
                    else
                        StatusMessage = "Access denied.";
                    break;
                case "CheckOut":
                    if (Auth.HasPermission("GuestPortal"))
                        _navigation.NavigateTo(new CheckOutView(this));
                    else
                        StatusMessage = "Access denied.";
                    break;
                case "StaffAccess":
                    if (Auth.HasPermission("StaffAccess"))
                        _navigation.NavigateTo(new StaffAccessView(this));
                    else
                        StatusMessage = "Access denied.";
                    break;
                case "RoomManagement":
                    if (Auth.HasPermission("RoomManagement"))
                        _navigation.NavigateTo(new RoomManagementView(this));
                    else
                        StatusMessage = "Access denied.";
                    break;
                case "FinancialReports":
                    if (Auth.HasPermission("FinancialReports"))
                        _navigation.NavigateTo(new FinancialReportsView(this));
                    else
                        StatusMessage = "Access denied.";
                    break;
                case "Housekeeping":
                    if (Auth.HasPermission("Housekeeping"))
                        _navigation.NavigateTo(new HousekeepingView(this));
                    else
                        StatusMessage = "Access denied.";
                    break;
                case "Inventory":
                    if (Auth.HasPermission("Inventory"))
                        _navigation.NavigateTo(new InventoryView(this));
                    else
                        StatusMessage = "Access denied.";
                    break;
                case "Settings":
                    if (Auth.HasPermission("Settings"))
                        _navigation.NavigateTo(new SettingsView(this));
                    else
                        StatusMessage = "Access denied.";
                    break;
            }
        }
    }
}
