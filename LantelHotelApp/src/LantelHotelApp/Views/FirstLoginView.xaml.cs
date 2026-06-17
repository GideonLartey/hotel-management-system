using System.Windows;
using System.Windows.Controls;
using LantelHotelApp.ViewModels;

namespace LantelHotelApp.Views
{
    public partial class FirstLoginView : UserControl
    {
        private readonly MainViewModel _vm;

        public FirstLoginView(MainViewModel vm)
        {
            InitializeComponent();
            _vm = vm;
        }

        private void ChangePassword_Click(object sender, RoutedEventArgs e)
        {
            string current = TxtCurrentPassword.Password;
            string newPw = TxtNewPassword.Password;
            string confirm = TxtConfirmPassword.Password;

            if (newPw != confirm)
            {
                ShowError("Passwords do not match.");
                return;
            }

            var (success, message) = _vm.Auth.ChangePassword(current, newPw);

            if (success)
            {
                _vm.ShowDashboard();
            }
            else
            {
                ShowError(message);
            }
        }

        private void ShowError(string message)
        {
            TxtError.Text = message;
            TxtError.Visibility = Visibility.Visible;
        }
    }
}
