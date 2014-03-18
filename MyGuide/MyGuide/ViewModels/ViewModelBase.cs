using Caliburn.Micro;
using MyGuide.Models;
using MyGuide.Services.Interfaces;
using System.Windows.Navigation;

namespace MyGuide.ViewModels
{
    public class ViewModelBase : PropertyChangedBase
    {
        protected INavigationService _navigation;
        protected IMessageDialogService _messageDialogService;
        protected IDataServiceModel _dataServiceModel;

        public ViewModelBase()
        {
            _navigation = null;
            
        }

        public ViewModelBase(INavigationService navigationService,IMessageDialogService messageDialogService, IDataServiceModel dataServiceModel)
        {
            _navigation = navigationService;
            _dataServiceModel = dataServiceModel;
            _messageDialogService = messageDialogService;
        }

        public virtual void OnNavigatedFrom(NavigationMode navigationMode)
        {
        }

        public virtual void OnNavigatedTo(NavigationMode navigationMode, bool isNewPageInstance)
        {
        }
    }
}