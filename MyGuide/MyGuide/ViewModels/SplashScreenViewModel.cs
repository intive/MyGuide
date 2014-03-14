using Caliburn.Micro;
using MyGuide.Models;
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
        public SplashScreenViewModel(INavigationService navigationService, IDataServiceModel _dataServiceModel) : base (navigationService)
        {
            dataServiceModel = _dataServiceModel;
            
        }

        public override async void OnNavigatedTo(NavigationMode navigationMode, bool isNewPageInstance)
        {
            try
            {
                await dataServiceModel.Initialize();
            }
            catch(LackOfDataException ex)
            {
                MessageBox.Show(String.Format("There is a problem with data. Please contact with us. \n{0}",ex.Message));
            }
        } 
    }
}
