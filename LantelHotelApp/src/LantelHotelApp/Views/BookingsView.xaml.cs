using System.Windows;
using System.Windows.Controls;
using LantelHotelApp.ViewModels;

namespace LantelHotelApp.Views
{
    public partial class BookingsView : UserControl
    {
        private readonly MainViewModel _vm;
        public BookingsView(MainViewModel vm) { InitializeComponent(); _vm = vm; DgBookings.ItemsSource = _vm.Bookings.GetAllBookings(); }
        private void Back_Click(object sender, RoutedEventArgs e) => _vm.ShowDashboard();
    }
}
