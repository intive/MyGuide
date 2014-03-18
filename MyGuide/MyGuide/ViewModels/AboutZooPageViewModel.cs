using Caliburn.Micro;
using MyGuide.Models;
using MyGuide.Services.Interfaces;

namespace MyGuide.ViewModels
{
    public class AboutZooPageViewModel : ViewModelBase
    {
        public AboutZooPageViewModel(INavigationService navigationService,
            IMessageDialogService messageDialogService, IDataServiceModel dataServiceModel)
            : base(navigationService, messageDialogService, dataServiceModel)
        {
        }
    }
}