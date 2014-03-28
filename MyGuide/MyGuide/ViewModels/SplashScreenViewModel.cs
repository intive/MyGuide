using Caliburn.Micro;
using MyGuide.DataServices.Interfaces;
using MyGuide.Models;
using MyGuide.Services.Interfaces;
using System;
using System.Threading.Tasks;
using System.Windows.Navigation;

namespace MyGuide.ViewModels
{
    public class SplashScreenViewModel : ViewModelBase
    {
        public SplashScreenViewModel(INavigationService navigationService,
            IMessageDialogService messageDialogService, IDataService dataService)
            : base(navigationService, messageDialogService, dataService)
        {
        }

        public override async void OnNavigatedTo(NavigationMode navigationMode, bool isNewPageInstance)
        {
            bool exceptionOccured = false;
            string exceptionMessage = "";
            try
            {
                await _dataService.Initialize();
            }
            catch (LackOfDataException ex)
            {
                exceptionOccured = true;
                exceptionMessage = ex.Message;
            }

            if (exceptionOccured)
            {
                await _messageDialogService.ShowDialog("Error",
                    (string)String.Format("There is a problem with data. Please contact with us. \n{0}", exceptionMessage), "Ok", null);
                App.Current.Terminate();
            }
            await Task.Delay(2000);
            _navigation.UriFor<MainPageViewModel>().Navigate();
        }
    }
}