using Caliburn.Micro;
using MyGuide.Models;
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
       

        public MainPageViewModel()
        {
            if (Execute.InDesignMode)
                LoadDesignData();
        }

        public MainPageViewModel(INavigationService navigationService, IDataServiceModel dataServiceModel)
            : base(navigationService)
        {
           
            // Uncomment to use design time data as test data
            // LoadDesignData();
        }

        #region Properties

        private string _welcomeText;

        public string WelcomeText
        {
            get { return _welcomeText; }
            set { _welcomeText = value; NotifyOfPropertyChange(() => WelcomeText); }
        }

        #endregion Properties

        #region Commands

        public void ShowAboutZoo()
        {
            _navigation.UriFor<AboutZooPageViewModel>().Navigate();
        }

        public void ShowOptions()
        {
            _navigation.UriFor<OptionsPageViewModel>().Navigate();
        }

        public void ShowSightsee()
        {
            _navigation.UriFor<SightseeingPageViewModel>().Navigate();
        }

        public void ShowTravelDirections()
        {
        }

        #endregion Commands

        private void LoadDesignData()
        {
            WelcomeText = DesignData.LoremImpusGenerator.Generate(5);
        }
    }
}