using System.ComponentModel;
using System.Windows;
using LantelHotelApp.Data;
using LantelHotelApp.Helpers;
using LantelHotelApp.ViewModels;

namespace LantelHotelApp
{
    public partial class MainWindow : Window
    {
        private readonly MainViewModel _viewModel;

        public MainWindow()
        {
            InitializeComponent();

            var context = new HotelDbContext();
            var navigation = new NavigationService();

            _viewModel = new MainViewModel(context, navigation);

            navigation.PropertyChanged += OnNavigationChanged;

            _viewModel.Initialize();
        }

        private void OnNavigationChanged(object? sender, PropertyChangedEventArgs e)
        {
            if (e.PropertyName == nameof(NavigationService.CurrentView))
            {
                var nav = sender as NavigationService;
                MainContent.Content = nav?.CurrentView;
            }
        }
    }
}