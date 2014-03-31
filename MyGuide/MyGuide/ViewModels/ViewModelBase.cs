using Caliburn.Micro;
using MyGuide.DataServices.Interfaces;
using MyGuide.Models;
using MyGuide.Services.Interfaces;
using System.Windows.Navigation;

namespace MyGuide.ViewModels
{
    public class ViewModelBase : PropertyChangedBase
    {
        protected IDataService _dataService;
        protected ILog _log;
        protected IMessageDialogService _messageDialogService;
        protected INavigationService _navigation;

        public ViewModelBase()
        {
            _navigation = null;
        }

        public ViewModelBase(INavigationService navigationService,
            IMessageDialogService messageDialogService, IDataService dataService)
        {
            _navigation = navigationService;
            _dataService = dataService;
            _messageDialogService = messageDialogService;
            _log = LogManager.GetLog(this.GetType());
        }

        public virtual void OnNavigatedFrom(NavigationMode navigationMode)
        {
        }

        public virtual void OnNavigatedTo(NavigationMode navigationMode, bool isNewPageInstance)
        {
        }
    }
}