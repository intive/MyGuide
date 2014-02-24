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
        public MainPageViewModel(INavigationService navigationService)
            : base(navigationService)
        {
        }

        // To get acces to OnNavigateTo/From override methods from base class
        // ex. public override void OnNavigatedTo(NavigationMode navigationMode, bool isNewPageInstance)
    }
}