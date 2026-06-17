using System.Linq;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Media;
using LantelHotelApp.ViewModels;

namespace LantelHotelApp.Views
{
    public partial class InventoryView : UserControl
    {
        private readonly MainViewModel _vm;
        public InventoryView(MainViewModel vm)
        {
            InitializeComponent(); _vm = vm;
            Refresh();
            CmbItem.ItemsSource = _vm.Rooms.GetInventory().Select(i => i.ItemType).ToList();
            if (CmbItem.Items.Count > 0) CmbItem.SelectedIndex = 0;
        }
        private void AddStock_Click(object sender, RoutedEventArgs e)
        {
            if (CmbItem.SelectedItem == null || !int.TryParse(TxtQty.Text, out int qty)) return;
            var (success, message) = _vm.Rooms.AddInventoryStock((string)CmbItem.SelectedItem, qty);
            TxtStatus.Text = message; TxtStatus.Foreground = new SolidColorBrush(success ? Color.FromRgb(74,222,128) : Color.FromRgb(255,107,107)); TxtStatus.Visibility = Visibility.Visible;
            Refresh();
        }
        private void Refresh() { DgInventory.ItemsSource = _vm.Rooms.GetInventory(); }
        private void Back_Click(object sender, RoutedEventArgs e) => _vm.ShowDashboard();
    }
}
