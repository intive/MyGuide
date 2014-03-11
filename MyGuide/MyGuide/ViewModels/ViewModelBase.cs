using Caliburn.Micro;
using MyGuide.Services.Interfaces;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Navigation;

namespace MyGuide.ViewModels
{
    public class ViewModelBase : PropertyChangedBase
    {
        protected INavigationService _navigation;
        protected IMessageDialogService _messageDialogService;

        public ViewModelBase()
        {
            _navigation = null;
        }

        public ViewModelBase(INavigationService navigationService,IMessageDialogService messageDialogService)
        {
            _navigation = navigationService;
        }

        public virtual void OnNavigatedFrom(NavigationMode navigationMode)
        {
        }

        public virtual void OnNavigatedTo(NavigationMode navigationMode, bool isNewPageInstance)
        {
        }
    }
}