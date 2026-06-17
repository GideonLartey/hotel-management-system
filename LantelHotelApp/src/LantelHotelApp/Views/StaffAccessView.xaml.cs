using System;
using System.Linq;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Media;
using LantelHotelApp.Models.Enums;
using LantelHotelApp.ViewModels;

namespace LantelHotelApp.Views
{
    public partial class StaffAccessView : UserControl
    {
        private readonly MainViewModel _vm;
        public StaffAccessView(MainViewModel vm)
        {
            InitializeComponent(); _vm = vm;
            CmbRole.ItemsSource = Enum.GetValues<StaffRole>().Select(r => r.ToString()).ToList();
            CmbRole.SelectedIndex = 1;
            RefreshStaff();
        }
        private void AddStaff_Click(object sender, RoutedEventArgs e)
        {
            var role = (StaffRole)CmbRole.SelectedIndex;
            var (success, message) = _vm.Auth.AddStaffAccess(TxtEmpId.Text.Trim(), TxtEmail.Text.Trim(), TxtFullName.Text.Trim(), role);
            TxtStatus.Text = message; TxtStatus.Foreground = new SolidColorBrush(success ? Color.FromRgb(74,222,128) : Color.FromRgb(255,107,107)); TxtStatus.Visibility = Visibility.Visible;
            if (success) { TxtEmpId.Text = ""; TxtFullName.Text = ""; TxtEmail.Text = ""; RefreshStaff(); }
        }
        private void RefreshStaff() { DgStaff.ItemsSource = _vm.Auth.GetAllStaff(); }
        private void Back_Click(object sender, RoutedEventArgs e) => _vm.ShowDashboard();
    }
}
