using System.Linq;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Media;
using LantelHotelApp.Models.Enums;
using LantelHotelApp.ViewModels;

namespace LantelHotelApp.Views
{
    public partial class HousekeepingView : UserControl
    {
        private readonly MainViewModel _vm;
        public HousekeepingView(MainViewModel vm)
        {
            InitializeComponent(); _vm = vm;
            var needsCleaning = _vm.Housekeeping.GetRoomsNeedingCleaning();
            CmbRoom.ItemsSource = needsCleaning.Select(r => r.RoomNumber).ToList();
            if (CmbRoom.Items.Count > 0) CmbRoom.SelectedIndex = 0;
            TxtNeedsCleaning.Text = $"⚠ {needsCleaning.Count} room(s) need cleaning";
            DgLog.ItemsSource = _vm.Housekeeping.GetCleaningLog();
        }
        private void Record_Click(object sender, RoutedEventArgs e)
        {
            if (CmbRoom.SelectedItem == null) return;
            int roomNum = (int)CmbRoom.SelectedItem;
            var room = _vm.Rooms.GetAllRooms().FirstOrDefault(r => r.RoomNumber == roomNum);
            if (room == null) return;
            var (success, message) = _vm.Housekeeping.RecordCleaning(room.Id, TxtKeeper.Text.Trim(), null);
            TxtStatus.Text = message; TxtStatus.Foreground = new SolidColorBrush(success ? Color.FromRgb(74,222,128) : Color.FromRgb(255,107,107)); TxtStatus.Visibility = Visibility.Visible;
            var needsCleaning = _vm.Housekeeping.GetRoomsNeedingCleaning();
            CmbRoom.ItemsSource = needsCleaning.Select(r => r.RoomNumber).ToList();
            TxtNeedsCleaning.Text = $"⚠ {needsCleaning.Count} room(s) need cleaning";
            DgLog.ItemsSource = _vm.Housekeeping.GetCleaningLog();
        }
        private void Back_Click(object sender, RoutedEventArgs e) => _vm.ShowDashboard();
    }
}
