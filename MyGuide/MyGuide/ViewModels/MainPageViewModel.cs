using Caliburn.Micro;
using MyGuide.DataServices;
using MyGuide.DataServices.Interfaces;
using MyGuide.Models;
using MyGuide.Resources;
using MyGuide.Services.Interfaces;
using System.ComponentModel;
using System.Diagnostics;

namespace MyGuide.ViewModels
{
    public class MainPageViewModel : ViewModelBase
    {
        public MainPageViewModel()
        {
            if (Execute.InDesignMode)
                LoadDesignData();
        }

        public MainPageViewModel(INavigationService navigationService,
            IMessageDialogService messageDialogService, IDataService dataService, IOptionsService optionService)
            : base(navigationService, messageDialogService, dataService, optionService)
        {
            // Uncomment to use design time data as test data
            LoadDesignData();

#if (DEBUG)
            IsVisibleDebugOptionsItem = true;
#else
            IsVisibleDebugOptionsItem = false;
#endif
        }

        #region Properties

        private bool _isVisibleDebugOptionsItem;
        private string _welcomeText;

        public bool IsVisibleDebugOptionsItem
        {
            get { return _isVisibleDebugOptionsItem; }
            set { _isVisibleDebugOptionsItem = value; NotifyOfPropertyChange(() => IsVisibleDebugOptionsItem); }
        }

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

        public void ShowDebugOptions()
        {
            _navigation.UriFor<DebugOptionsPageViewModel>().Navigate();
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

        [Conditional("DEBUG")]
        private void SetDebugOptionsVisibleInDebugMode()
        {
            IsVisibleDebugOptionsItem = true;
        }
    }
}