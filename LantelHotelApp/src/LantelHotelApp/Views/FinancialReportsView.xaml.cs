using System.Windows;
using System.Windows.Controls;
using LantelHotelApp.ViewModels;

namespace LantelHotelApp.Views
{
    public partial class FinancialReportsView : UserControl
    {
        private readonly MainViewModel _vm;
        public FinancialReportsView(MainViewModel vm)
        {
            InitializeComponent(); _vm = vm;
            TxtRevenue.Text = _vm.Currency.FormatAmount(_vm.Financial.GetTotalRevenue());
            TxtAvgRate.Text = _vm.Currency.FormatAmount(_vm.Financial.GetAverageRoomRate());
            TxtOccRate.Text = $"{_vm.Financial.GetOccupancyRate():F1}%";
            TxtTotalBookings.Text = _vm.Financial.GetTotalBookings().ToString();
            DgRevenue.ItemsSource = _vm.Financial.GetRevenueBookings();
        }
        private void Back_Click(object sender, RoutedEventArgs e) => _vm.ShowDashboard();
    }
}
