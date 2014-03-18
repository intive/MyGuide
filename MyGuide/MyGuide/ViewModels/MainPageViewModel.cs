using Caliburn.Micro;
using MyGuide.Models;
using MyGuide.Resources;
using MyGuide.Services.Interfaces;
using System.ComponentModel;

namespace MyGuide.ViewModels
{
    public class MainPageViewModel : ViewModelBase
    {
        


        public MainPageViewModel()
           
        {
            if (Execute.InDesignMode)
                LoadDesignData();
        }

            

        public MainPageViewModel(
            INavigationService navigationService,
            IMessageDialogService messageDialogService, IDataServiceModel dataServiceModel)
            : base(navigationService, messageDialogService,dataServiceModel)
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

        public async void OnClose(CancelEventArgs args)
        {
            args.Cancel = true;
            bool exit = await _messageDialogService.ShowDialog(AppResources.ExitDlgTitle,
                AppResources.ExitDlgMessage, DialogType.YesNo);
            if (exit)
            {
                App.Current.Terminate();
            }
        }

        private void LoadDesignData()
        {
            WelcomeText = DesignData.LoremImpusGenerator.Generate(5);
        }
    }
}