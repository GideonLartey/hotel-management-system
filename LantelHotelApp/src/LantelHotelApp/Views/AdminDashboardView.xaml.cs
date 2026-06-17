using System.Windows;
using System.Windows.Controls;
using LantelHotelApp.ViewModels;

namespace LantelHotelApp.Views
{
    public partial class AdminDashboardView : UserControl
    {
        private readonly MainViewModel _vm;

        public AdminDashboardView(MainViewModel vm)
        {
            InitializeComponent();
            _vm = vm;
            LoadStats();
        }

        private void LoadStats()
        {
            TxtWelcome.Text = $"Welcome back, {_vm.Auth.CurrentUser?.FullName} • {_vm.Auth.CurrentUser?.Role}";
            TxtAvailableRooms.Text = _vm.Rooms.GetAvailableCount().ToString();
            TxtOccupancy.Text = $"{_vm.Financial.GetOccupancyRate():F1}%";
            TxtActiveBookings.Text = _vm.Financial.GetActiveBookings().ToString();
            TxtRevenue.Text = _vm.Currency.FormatAmount(_vm.Financial.GetTotalRevenue());

            // Hide nav buttons based on role
            BtnStaff.Visibility = _vm.Auth.HasPermission("StaffAccess") ? Visibility.Visible : Visibility.Collapsed;
            BtnFinancial.Visibility = _vm.Auth.HasPermission("FinancialReports") ? Visibility.Visible : Visibility.Collapsed;
        }

        private void Navigate_Click(object sender, RoutedEventArgs e)
        {
            if (sender is Button btn && btn.Tag is string target)
            {
                _vm.NavigateCommand.Execute(target);
            }
        }

        private void Logout_Click(object sender, RoutedEventArgs e)
        {
            _vm.LogoutCommand.Execute(null);
        }
    }
}
