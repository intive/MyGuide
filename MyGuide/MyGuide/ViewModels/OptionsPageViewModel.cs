using Caliburn.Micro;
using MyGuide.Models;
using MyGuide.Services.Interfaces;

namespace MyGuide.ViewModels
{
    public class OptionsPageViewModel : ViewModelBase
    {
        public OptionsPageViewModel(INavigationService navigationService,
            IMessageDialogService messageDialogService, IDataServiceModel dataServiceModel)
            : base(navigationService, messageDialogService, dataServiceModel)
        {
        }
    }
}