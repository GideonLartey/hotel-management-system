using System.Windows;
using System.Windows.Controls;
using System.Windows.Input;
using LantelHotelApp.ViewModels;

namespace LantelHotelApp.Views
{
    public partial class LoginView : UserControl
    {
        private readonly MainViewModel _vm;

        public LoginView(MainViewModel vm)
        {
            InitializeComponent();
            _vm = vm;
            TxtUsername.Focus();
        }

        private void Login_Click(object sender, RoutedEventArgs e)
        {
            PerformLogin();
        }

        private void TxtPassword_KeyDown(object sender, KeyEventArgs e)
        {
            if (e.Key == Key.Enter)
                PerformLogin();
        }

        private void PerformLogin()
        {
            string username = TxtUsername.Text.Trim();
            string password = TxtPassword.Password;

            if (string.IsNullOrEmpty(username) || string.IsNullOrEmpty(password))
            {
                ShowError("Please enter both username and password.");
                return;
            }

            var (success, message) = _vm.Auth.Login(username, password);

            if (success)
            {
                _vm.OnLoginSuccess();
            }
            else
            {
                ShowError(message);
                TxtPassword.Password = string.Empty;
                TxtPassword.Focus();
            }
        }

        private void ShowError(string message)
        {
            TxtError.Text = message;
            TxtError.Visibility = Visibility.Visible;
        }
    }
}
