using Caliburn.Micro;
using MyGuide.DataServices.Interfaces;
using MyGuide.Models;
using MyGuide.Services.Interfaces;

namespace MyGuide.ViewModels
{
    public class AboutZooPageViewModel : ViewModelBase
    {
        public AboutZooPageViewModel(INavigationService navigationService,
            IMessageDialogService messageDialogService, IDataService dataService)
            : base(navigationService, messageDialogService, dataService)
        {
        }
    }
}