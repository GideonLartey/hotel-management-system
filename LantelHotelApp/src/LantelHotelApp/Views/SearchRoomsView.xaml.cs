using System;
using System.Windows;
using System.Windows.Controls;
using LantelHotelApp.ViewModels;

namespace LantelHotelApp.Views
{
    public partial class SearchRoomsView : UserControl
    {
        private readonly MainViewModel _vm;
        public SearchRoomsView(MainViewModel vm) { InitializeComponent(); _vm = vm; DpFrom.SelectedDate = DateTime.Today; DpTo.SelectedDate = DateTime.Today.AddDays(1); }
        private void Search_Click(object sender, RoutedEventArgs e)
        {
            if (DpFrom.SelectedDate.HasValue && DpTo.SelectedDate.HasValue)
                DgRooms.ItemsSource = _vm.Bookings.FindAvailableRooms(DpFrom.SelectedDate.Value, DpTo.SelectedDate.Value);
        }
        private void Back_Click(object sender, RoutedEventArgs e) => _vm.ShowDashboard();
    }
}
