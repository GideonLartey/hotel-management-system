using System.Linq;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Media;
using LantelHotelApp.ViewModels;

namespace LantelHotelApp.Views
{
    public partial class CheckInView : UserControl
    {
        private readonly MainViewModel _vm;
        public CheckInView(MainViewModel vm)
        {
            InitializeComponent(); _vm = vm;
            DgPending.ItemsSource = _vm.Bookings.GetActiveBookings().Where(b => !b.IsCheckedIn).ToList();
        }
        private void CheckIn_Click(object sender, RoutedEventArgs e)
        {
            var booking = _vm.Bookings.GetBookingByReference(TxtRef.Text.Trim());
            if (booking == null) { ShowStatus("Booking not found.", false); return; }
            if (!booking.IsPaymentCompleted)
            {
                var payResult = _vm.Bookings.ProcessPayment(booking.Id, booking.RemainingBalance);
                if (!payResult.Success) { ShowStatus(payResult.Message, false); return; }
            }
            var (success, message) = _vm.Bookings.CheckIn(booking.Id);
            ShowStatus(message, success);
            DgPending.ItemsSource = _vm.Bookings.GetActiveBookings().Where(b => !b.IsCheckedIn).ToList();
        }
        private void ShowStatus(string msg, bool ok) { TxtStatus.Text = msg; TxtStatus.Foreground = new SolidColorBrush(ok ? Color.FromRgb(74,222,128) : Color.FromRgb(255,107,107)); TxtStatus.Visibility = Visibility.Visible; }
        private void Back_Click(object sender, RoutedEventArgs e) => _vm.ShowDashboard();
    }
}
