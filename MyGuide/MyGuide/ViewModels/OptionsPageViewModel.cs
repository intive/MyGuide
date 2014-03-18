using Caliburn.Micro;
using MyGuide.DataServices.Interfaces;
using MyGuide.Models;
using MyGuide.Services.Interfaces;

namespace MyGuide.ViewModels
{
    public class OptionsPageViewModel : ViewModelBase
    {
        public OptionsPageViewModel(INavigationService navigationService,
            IMessageDialogService messageDialogService, IDataService dataServiceModel)
            : base(navigationService, messageDialogService, dataServiceModel)
        {
        }
    }
}