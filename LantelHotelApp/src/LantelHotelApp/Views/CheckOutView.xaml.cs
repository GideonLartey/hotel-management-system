using System.Linq;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Media;
using LantelHotelApp.ViewModels;

namespace LantelHotelApp.Views
{
    public partial class CheckOutView : UserControl
    {
        private readonly MainViewModel _vm;
        public CheckOutView(MainViewModel vm)
        {
            InitializeComponent(); _vm = vm;
            DgCheckedIn.ItemsSource = _vm.Bookings.GetActiveBookings().Where(b => b.IsCheckedIn && !b.IsCheckedOut).ToList();
        }
        private void CheckOut_Click(object sender, RoutedEventArgs e)
        {
            var booking = _vm.Bookings.GetBookingByReference(TxtRef.Text.Trim());
            if (booking == null) { ShowStatus("Booking not found.", false); return; }
            var (success, message) = _vm.Bookings.CheckOut(booking.Id);
            ShowStatus(message, success);
            DgCheckedIn.ItemsSource = _vm.Bookings.GetActiveBookings().Where(b => b.IsCheckedIn && !b.IsCheckedOut).ToList();
        }
        private void ShowStatus(string msg, bool ok) { TxtStatus.Text = msg; TxtStatus.Foreground = new SolidColorBrush(ok ? Color.FromRgb(74,222,128) : Color.FromRgb(255,107,107)); TxtStatus.Visibility = Visibility.Visible; }
        private void Back_Click(object sender, RoutedEventArgs e) => _vm.ShowDashboard();
    }
}
