using Caliburn.Micro;
using MyGuide.Services.Interfaces;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MyGuide.ViewModels
{
    public class SightseeingPageViewModel : ViewModelBase
    {
        public SightseeingPageViewModel(INavigationService navigationService, IMessageDialogService messageDialogService)
            : base(navigationService, messageDialogService)
        {
        }
    }
}