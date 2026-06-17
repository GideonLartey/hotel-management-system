using System.Windows;
using System.Windows.Controls;
using LantelHotelApp.ViewModels;

namespace LantelHotelApp.Views
{
    public partial class RoomManagementView : UserControl
    {
        private readonly MainViewModel _vm;
        public RoomManagementView(MainViewModel vm)
        {
            InitializeComponent(); _vm = vm;
            TxtTotal.Text = _vm.Rooms.GetTotalCount().ToString();
            TxtAvail.Text = _vm.Rooms.GetAvailableCount().ToString();
            TxtOccup.Text = _vm.Rooms.GetOccupiedCount().ToString();
            DgRooms.ItemsSource = _vm.Rooms.GetAllRooms();
        }
        private void Back_Click(object sender, RoutedEventArgs e) => _vm.ShowDashboard();
    }
}
