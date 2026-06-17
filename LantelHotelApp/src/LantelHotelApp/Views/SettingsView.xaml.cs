using System.Windows;
using System.Windows.Controls;
using System.Windows.Media;
using LantelHotelApp.ViewModels;

namespace LantelHotelApp.Views
{
    public partial class SettingsView : UserControl
    {
        private readonly MainViewModel _vm;
        public SettingsView(MainViewModel vm)
        {
            InitializeComponent(); _vm = vm;
            TxtCurrentCurrency.Text = $"Current: {_vm.Currency.GetCurrencySymbol()} ({_vm.Currency.GetCurrencyCode()})";
        }
        private void SwitchGhc_Click(object sender, RoutedEventArgs e)
        {
            _vm.Currency.SetCurrency("GHS", "GH₵");
            TxtCurrentCurrency.Text = "Current: GH₵ (GHS) ✓";
        }
        private void SwitchUsd_Click(object sender, RoutedEventArgs e)
        {
            _vm.Currency.SetCurrency("USD", "$");
            TxtCurrentCurrency.Text = "Current: $ (USD) ✓";
        }
        private void ChangePw_Click(object sender, RoutedEventArgs e)
        {
            var (success, message) = _vm.Auth.ChangePassword(TxtCurrentPw.Password, TxtNewPw.Password);
            TxtPwStatus.Text = message;
            TxtPwStatus.Foreground = new SolidColorBrush(success ? Color.FromRgb(74,222,128) : Color.FromRgb(255,107,107));
            TxtPwStatus.Visibility = Visibility.Visible;
            if (success) { TxtCurrentPw.Password = ""; TxtNewPw.Password = ""; }
        }
        private void Back_Click(object sender, RoutedEventArgs e) => _vm.ShowDashboard();
    }
}
