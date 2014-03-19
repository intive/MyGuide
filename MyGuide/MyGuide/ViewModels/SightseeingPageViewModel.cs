using Caliburn.Micro;
using MyGuide.Models;
using MyGuide.Services.Interfaces;

namespace MyGuide.ViewModels
{
    public class SightseeingPageViewModel : ViewModelBase
    {
        public SightseeingPageViewModel(INavigationService navigationService,
            IMessageDialogService messageDialogService, IDataServiceModel dataServiceModel)
            : base(navigationService, messageDialogService, dataServiceModel)
        {
        }

        #region Commands
        //Uncomment all method and set some instructions when we'll implement appbar clickable
        //Remeber to add icons for appbar buttons!

        //public void ShowMap()
        //{
        //}

        //public void ShowAnimals()
        //{
        //}

        //public void ShowMap()
        //{
        //}

        //public void ShowInformation()
        //{
        //}

        //public void ShowGastronomy()
        //{
        //}

        //public void ShowHistory()
        //{
        //}

        //public void ShowPreferences()
        //{
        //}

        #endregion
    }
}