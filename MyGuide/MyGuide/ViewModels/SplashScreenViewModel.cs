using Caliburn.Micro;
using MyGuide.Models;
using MyGuide.Services.Interfaces;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Navigation;

namespace MyGuide.ViewModels
{
    public class SplashScreenViewModel : ViewModelBase
    {
        private IDataServiceModel dataServiceModel;
        private IMessageDialogService messageDialogService;
        private INavigationService navigationService;
        public SplashScreenViewModel(INavigationService _navigationService,
            IMessageDialogService _messageDialogService,IDataServiceModel _dataServiceModel) : base (_navigationService, _messageDialogService)
        {
            dataServiceModel = _dataServiceModel;
            messageDialogService = _messageDialogService;
            navigationService = _navigationService;
        }

        public override async void OnNavigatedTo(NavigationMode navigationMode, bool isNewPageInstance)
        {
            bool exceptionOccured = false;
            string exceptionMessage = "";
            try
            {
                await dataServiceModel.Initialize();
            }
            catch(LackOfDataException ex)
            {
                exceptionOccured = true;
                exceptionMessage = ex.Message;
            }
            
            if (exceptionOccured)
            {
                await messageDialogService.ShowDialog("Error",
                    (string)String.Format("There is a problem with data. Please contact with us. \n{0}", exceptionMessage), "Ok", null);
                App.Current.Terminate();
            }
            
            navigationService.UriFor<MainPageViewModel>().Navigate();
        } 
    }
}
