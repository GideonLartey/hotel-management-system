using System.Windows.Controls;
using LantelHotelApp.ViewModels;

namespace LantelHotelApp.Views
{
    public partial class CurrencySelectionView : UserControl
    {
        private readonly MainViewModel _vm;

        public CurrencySelectionView(MainViewModel vm)
        {
            InitializeComponent();
            _vm = vm;
        }

        private void GhCedi_Click(object sender, System.Windows.RoutedEventArgs e)
        {
            _vm.Currency.SetCurrency("GHS", "GH₵");
            _vm.Currency.MarkFirstLaunchComplete();
            _vm.Navigation.NavigateTo(new LoginView(_vm));
        }

        private void USD_Click(object sender, System.Windows.RoutedEventArgs e)
        {
            _vm.Currency.SetCurrency("USD", "$");
            _vm.Currency.MarkFirstLaunchComplete();
            _vm.Navigation.NavigateTo(new LoginView(_vm));
        }
    }
}
