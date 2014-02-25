using Caliburn.Micro;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;

namespace MyGuide.ViewModels
{
    public class MainPageViewModel : ViewModelBase
    {
        // To get acces to OnNavigateTo/From override methods from base class
        // ex. public override void OnNavigatedTo(NavigationMode navigationMode, bool isNewPageInstance)

        public MainPageViewModel(INavigationService navigationService)
            : base(navigationService)
        {
        }

        public void ShowSightsee()
        {
        }

        public void ShowTickets()
        {
        }

        public void ShowTravelDirections()
        {
        }
    }
}