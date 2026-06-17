using System;
using System.Linq;
using System.Windows;
using System.Windows.Controls;
using LantelHotelApp.Models.Enums;
using LantelHotelApp.ViewModels;

namespace LantelHotelApp.Views
{
    public partial class GuestPortalView : UserControl
    {
        private readonly MainViewModel _vm;

        public GuestPortalView(MainViewModel vm)
        {
            InitializeComponent();
            _vm = vm;

            CmbRoomType.ItemsSource = Enum.GetValues<RoomType>().Select(r => r.GetDisplayName()).ToList();
            CmbRoomType.SelectedIndex = 0;
            CmbGuestType.ItemsSource = Enum.GetValues<GuestType>().Select(g => g.ToString()).ToList();
            CmbGuestType.SelectedIndex = 0;
            CmbSeason.ItemsSource = Enum.GetValues<SeasonType>().Select(s => s.ToString()).ToList();
            CmbSeason.SelectedIndex = 0;
            DpCheckIn.SelectedDate = DateTime.Today;
            DpCheckOut.SelectedDate = DateTime.Today.AddDays(1);
        }

        private void CreateBooking_Click(object sender, RoutedEventArgs e)
        {
            var roomType = (RoomType)CmbRoomType.SelectedIndex;
            var guestType = (GuestType)CmbGuestType.SelectedIndex;
            var season = (SeasonType)CmbSeason.SelectedIndex;

            if (!DpCheckIn.SelectedDate.HasValue || !DpCheckOut.SelectedDate.HasValue)
            {
                ShowStatus("Please select check-in and check-out dates.", false);
                return;
            }

            if (!int.TryParse(TxtAge.Text, out int age))
            {
                ShowStatus("Please enter a valid age.", false);
                return;
            }

            var availableRooms = _vm.Bookings.FindAvailableRooms(
                DpCheckIn.SelectedDate.Value, DpCheckOut.SelectedDate.Value, roomType);

            if (!availableRooms.Any())
            {
                ShowStatus("No rooms available for the selected type and dates.", false);
                return;
            }

            var room = availableRooms.First();

            var (success, message, booking) = _vm.Bookings.CreateBooking(
                TxtGuestId.Text.Trim(), TxtGuestName.Text.Trim(), age,
                TxtPhone.Text.Trim(), string.IsNullOrWhiteSpace(TxtEmail.Text) ? null : TxtEmail.Text.Trim(),
                room.Id, DpCheckIn.SelectedDate.Value, DpCheckOut.SelectedDate.Value,
                guestType, season);

            ShowStatus(message, success);

            if (success && booking != null)
            {
                ShowStatus($"{message}\nTotal: {_vm.Currency.FormatAmount(booking.TotalAmount)}\nRoom: {room.RoomNumber}", true);
            }
        }

        private void ShowStatus(string message, bool isSuccess)
        {
            TxtStatus.Text = message;
            TxtStatus.Foreground = isSuccess
                ? new System.Windows.Media.SolidColorBrush(System.Windows.Media.Color.FromRgb(74, 222, 128))
                : new System.Windows.Media.SolidColorBrush(System.Windows.Media.Color.FromRgb(255, 107, 107));
            TxtStatus.Visibility = Visibility.Visible;
        }

        private void Back_Click(object sender, RoutedEventArgs e)
        {
            _vm.ShowDashboard();
        }
    }
}
