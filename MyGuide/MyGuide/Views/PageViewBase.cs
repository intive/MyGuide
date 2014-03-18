using Microsoft.Phone.Controls;
using MyGuide.ViewModels;
using System;

namespace MyGuide.Views
{
    public class PageViewBase : PhoneApplicationPage
    {
        private bool _isNewPageInstance = false;

        public PageViewBase()
        {
            _isNewPageInstance = true;
        }

        protected override void OnNavigatedFrom(System.Windows.Navigation.NavigationEventArgs e)
        {
            base.OnNavigatedFrom(e);
            ViewModelBase viewModel = GetViewModel();
            viewModel.OnNavigatedFrom(e.NavigationMode);
        }

        protected override void OnNavigatedTo(System.Windows.Navigation.NavigationEventArgs e)
        {
            base.OnNavigatedTo(e);
            ViewModelBase viewModel = GetViewModel();
            viewModel.OnNavigatedTo(e.NavigationMode, _isNewPageInstance);
            _isNewPageInstance = false;
        }

        private ViewModelBase GetViewModel()
        {
            var viewModel = this.DataContext as ViewModelBase;
            if (viewModel != null)
            {
                return viewModel;
            }
            else
            {
                // Log that something went wrong
                throw new InvalidOperationException("Page " + this.GetType() + " does not contain proper ViewModel in DataContext");
            }
        }
    }
}
